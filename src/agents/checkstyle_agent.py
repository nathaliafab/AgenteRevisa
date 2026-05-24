import re
from pathlib import Path

from langchain_core.prompts import PromptTemplate  # type: ignore[import-not-found]

from agents.review_agent_base import BaseCodeReviewAgent


class CheckStyleAgent(BaseCodeReviewAgent):
    tool_display_name = "CheckStyle"
    tool_cmd_env_var = "CHECKSTYLE_CMD"
    default_tool_cmd = "bin/checkstyle/checkstyle-10.12.7-all.jar"
    no_issue_markers = ("Audit done.",)

    def _build_tool_command(
        self, java_file_path: Path, temp_dir: str, tool_config: str
    ) -> list[str]:
        return [
            "java",
            "-jar",
            self.tool_cmd,
            "-c",
            tool_config or "/google_checks.xml",
            "-f",
            "plain",
            str(java_file_path),
        ]

    def _determine_tool_config(self, pr_description: str, contributing_md: str) -> str:
        """Usa o LLM para selecionar a configuração do CheckStyle baseada no PR e regras."""
        prompt = PromptTemplate.from_template(
            "Você é um especialista em CheckStyle.\n"
            "Analise a descrição do PR e as regras de contribuição fornecidas e decida qual configuração do CheckStyle usar.\n"
            "Escolha entre '/google_checks.xml' ou '/sun_checks.xml'.\n"
            "Se as regras mencionarem 'Google' ou não forem específicas, use '/google_checks.xml'.\n"
            "Se mencionarem 'Sun', use '/sun_checks.xml'.\n\n"
            "Descrição do PR: {pr_description}\n"
            "Regras de Contribuição: {contributing_md}\n\n"
            "Retorne APENAS o caminho do arquivo de configuração escolhido."
        )
        
        chain = prompt | self.llm
        response = chain.invoke({
            "pr_description": pr_description,
            "contributing_md": contributing_md
        })
        
        config = self._collect_llm_content(response)

        if "/sun_checks.xml" in config:
            return "/sun_checks.xml"
        return "/google_checks.xml"

    def _build_fix_prompt(self) -> PromptTemplate:
        return PromptTemplate.from_template(
            "Você é um engenheiro de software experiente.\n"
            "Descrição do PR: {pr_description}\n"
            "Regras de Contribuição: {contributing_md}\n\n"
            "O CheckStyle analisou o seguinte código Java...\n"
            "Código atual:\n```java\n{current_code}\n```\n\n"
            "Achados do CheckStyle:\n{analysis_output}\n\n"
            "Por favor, retorne SOMENTE o código Java corrigido, sem markdown ao redor, para que os problemas de estilo, padrões e boas práticas sejam resolvidos."
        )

    def _analysis_has_findings(self, analysis_output: str) -> bool:
        normalized_output = analysis_output.strip()
        if not normalized_output:
            return False

        if any(marker in normalized_output for marker in self.no_issue_markers):
            if re.search(r"\b[1-9]\d*\s+errors?\b", normalized_output, re.I):
                return True
            return "[WARN]" in normalized_output

        return bool(re.search(r":\d+:\d+:", normalized_output))