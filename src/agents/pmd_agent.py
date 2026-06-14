import subprocess
from pathlib import Path

from langchain_core.prompts import PromptTemplate

from agents.review_agent_base import BaseCodeReviewAgent


class PMDAgent(BaseCodeReviewAgent):
    tool_display_name = "PMD"
    tool_cmd_env_var = "PMD_CMD"
    default_tool_cmd = "bin/pmd-bin-7.24.0/bin/pmd"
    no_issue_markers = ("No problems found",)

    def __init__(self, model_name: str | None = None):
        super().__init__(model_name)
        self._ensure_pmd_installed()

    def _ensure_pmd_installed(self):
        # A pasta em que o script baixa e extrai o ZIP
        if not Path("bin/pmd-bin-7.24.0").exists():
            self.logger.info("PMD não encontrado na pasta bin/. Executando script de download...")
            script_path = Path("scripts/install_pmd.sh")
            if script_path.exists():
                subprocess.run([str(script_path)], check=True)
            else:
                self.logger.error("Script de instalação do PMD não encontrado em %s", script_path)

    def _build_tool_command(self, java_file_path: Path, temp_dir: str, tool_config: str) -> list[str]:
        return [
            self.tool_cmd,
            "check",
            "-d",
            temp_dir,
            "-f",
            "text",
            "-R",
            tool_config or "rulesets/java/quickstart.xml",
        ]

    def _determine_tool_config(self, pr_description: str, contributing_md: str) -> str:
        # a descricao do PR e o contributing nao estao sendo usados
        return "rulesets/java/quickstart.xml"

    def _build_fix_prompt(self) -> PromptTemplate:
        return PromptTemplate.from_template(
            "Você é um engenheiro de software experiente.\n"
            "Descrição do PR: {pr_description}\n"
            "Regras de Contribuição: {contributing_md}\n\n"
            "O PMD analisou o seguinte código Java...\n"
            "Código atual:\n```java\n{current_code}\n```\n\n"
            "Testes de regras de negócio (devem continuar passando):\n```java\n{generated_tests}\n```\n\n"
            "Achados do PMD (ou das falhas no teste):\n{analysis_output}\n\n"
            "Por favor, retorne o código Java modificado entre tags <CODE> e </CODE>. "
            "Se for NECESSÁRIO corrigir o teste devido a uma mudança de sintaxe no código principal (ex: renomeio de classe ou variável que o agente reclamou de letra minúscula/maiúscula), retorne TAMBÉM o código do teste corrigido entre tags <TEST> e </TEST>.\n\n"
            "Além disso, forneça uma breve explicação em português do porquê essas alterações foram feitas entre as tags <EXPLANATION> e </EXPLANATION>."
        )

    def _analysis_has_findings(self, analysis_output: str) -> bool:
        if "Falhas em testes detectadas:" in analysis_output:
            return True
        return bool(
            analysis_output.strip()
            and all(marker not in analysis_output for marker in self.no_issue_markers)
        )
