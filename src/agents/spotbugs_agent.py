import os
import subprocess
import tempfile
import re
from pathlib import Path

from langchain_core.prompts import PromptTemplate

from agents.review_agent_base import BaseCodeReviewAgent, AgentState


class SpotBugsAgent(BaseCodeReviewAgent):
    tool_display_name = "SpotBugs"
    tool_cmd_env_var = "SPOTBUGS_CMD"
    default_tool_cmd = "bin/spotbugs/bin/spotbugs"
    # Spotbugs imprime estatísticas quando limpo. Podemos ajustar de acordo com a saída real.
    no_issue_markers = ("Warnings generated: 0", "Total bugs: 0", "exit code 0")

    def __init__(self, model_name: str | None = None):
        super().__init__(model_name)
        self._ensure_spotbugs_installed()

    def _ensure_spotbugs_installed(self):
        if not Path("bin/spotbugs").exists():
            self.logger.info("SpotBugs não encontrado na pasta bin/. Executando script de download...")
            script_path = Path("scripts/install_spotbugs.sh")
            if script_path.exists():
                subprocess.run([str(script_path)], check=True)
            else:
                self.logger.error("Script de instalação do SpotBugs não encontrado em %s", script_path)

    def _build_tool_command(self, java_file_path: Path, temp_dir: str, tool_config: str) -> list[str]:
        return [
            self.tool_cmd,
            "-textui",
            "-effort:max",
            "-low",
            "-sourcepath", temp_dir,
            temp_dir
        ]

    def _determine_tool_config(self, pr_description: str, contributing_md: str) -> str:
        # Configuração fixa de prioridade para SpotBugs (High, Med, Low bugs)
        return "-textui"

    def _run_analysis_node(self, state: AgentState):
        self.logger.info(
            "Executando nó: RUN %s (Iteração %d)", 
            self.tool_display_name.upper(), 
            state['iterations']
        )

        current_code = state.get("current_code", state["original_code"])

        with tempfile.TemporaryDirectory() as temp_dir:
            # O SpotBugs analisa arquivos compilaos (.class). Por isso precisamos compilar antes.
            # E o nome do arquivo .java de classes públicas deve casar com o nome da classe.
            match = re.search(r'class\s+(\w+)\s*{', current_code)
            class_name = match.group(1) if match else "PRCode"
            
            java_file_path = Path(temp_dir) / f"{class_name}.java"
            java_file_path.write_text(current_code, encoding="utf-8")

            self.logger.debug("Compilando %s para análise via SpotBugs", java_file_path.name)
            
            # 1. Compilar o código Java para extrair o Bytecode (.class)
            compile_cmd = ["javac", str(java_file_path)]
            comp_result = subprocess.run(compile_cmd, capture_output=True, text=True)
            
            if comp_result.returncode != 0:
                # Devolver também o erro de compilação como achado pro LLM consertar
                analysis_output = f"Compilation Error:\n{comp_result.stderr}"
            else:
                # 2. Executar o SpotBugs no diretório
                cmd = self._build_tool_command(java_file_path, temp_dir, state.get("tool_config", ""))
                analysis_output = self._run_command(cmd)

            self._log_analysis_output(analysis_output)

        return {
            "analysis_output": analysis_output.strip(),
            "current_code": current_code,
            "iterations": state["iterations"] + 1,
        }

    def _build_fix_prompt(self) -> PromptTemplate:
        return PromptTemplate.from_template(
            "Você é um engenheiro de software especialista em segurança de software e performance.\n"
            "Descrição do PR: {pr_description}\n"
            "Regras de Contribuição: {contributing_md}\n\n"
            "O agregador (Compilador + SpotBugs) analisou o seguinte código Java e buscou por NullPointers, Race Conditions e Erros Estruturais.\n"
            "Código atual:\n```java\n{current_code}\n```\n\n"
            "Achados do SpotBugs (ou da compilação):\n{analysis_output}\n\n"
            "Por favor, retorne SOMENTE o código Java corrigido, sem markdown ao redor, para que as vulnerabilidades de concorrência, possíveis quedas de sistema por NullPointer e problemas estruturais sejam sanados."
        )

    def _analysis_has_findings(self, analysis_output: str) -> bool:
        normalized = analysis_output.strip()
        if not normalized:
            return False
            
        if "Compilation Error" in normalized:
            return True
            
        # Considera que tudo está ok se achar o marker de 0 bugs
        if any(marker in normalized for marker in self.no_issue_markers):
            return False
            
        return True