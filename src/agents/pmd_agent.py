from pathlib import Path

from langchain_core.prompts import PromptTemplate

from agents.review_agent_base import BaseCodeReviewAgent


class PMDAgent(BaseCodeReviewAgent):
    tool_display_name = "PMD"
    tool_cmd_env_var = "PMD_CMD"
    default_tool_cmd = "bin/pmd-bin-7.24.0/bin/pmd"
    no_issue_markers = ("No problems found",)

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
            "Achados do PMD:\n{analysis_output}\n\n"
            "Por favor, retorne SOMENTE o código Java corrigido, sem markdown ao redor, para que os erros do PMD sejam resolvidos."
        )

    def _analysis_has_findings(self, analysis_output: str) -> bool:
        return bool(
            analysis_output.strip()
            and all(marker not in analysis_output for marker in self.no_issue_markers)
        )
