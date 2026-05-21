import os
import subprocess
import tempfile
from abc import ABC, abstractmethod
from pathlib import Path
from typing import Dict, List, TypedDict

from langchain_core.prompts import PromptTemplate
from langchain_google_genai import ChatGoogleGenerativeAI
from langgraph.graph import END, StateGraph
from utils import setup_logger


__all__ = ["AgentState", "BaseCodeReviewAgent", "PromptTemplate"]


class AgentState(TypedDict):
    pr_description: str
    contributing_md: str
    original_code: str
    current_code: str
    analysis_output: str
    fixes_history: List[Dict[str, str]]
    iterations: int
    max_iterations: int
    final_report: str
    tool_config: str


class BaseCodeReviewAgent(ABC):
    tool_display_name = "Code Review Tool"
    tool_cmd_env_var = ""
    default_tool_cmd = ""
    no_issue_markers: tuple[str, ...] = ()

    def __init__(self, model_name: str | None = None):
        self.logger = setup_logger(self.__class__.__name__)
        config_model = os.getenv("REVIEW_MODEL", "gemini-3-flash-preview")
        self.actual_model = model_name or config_model

        self.tool_cmd = os.getenv(self.tool_cmd_env_var, self.default_tool_cmd)
        self.llm = ChatGoogleGenerativeAI(
            model=self.actual_model, api_key=os.getenv("GOOGLE_API_KEY")
        )
        self.app = self._build_graph()
        # self.app.get_graph().print_ascii() - tem que ter grandalf instalado pra printar o grafo

    def _collect_llm_content(self, response) -> str:
        content = response.content
        if isinstance(content, list):
            content = "".join(
                [c.get("text", "") if isinstance(c, dict) else str(c) for c in content]
            )
        elif not isinstance(content, str):
            content = str(content)

        return content.strip()

    def _strip_code_fences(self, content: str) -> str:
        cleaned = content.strip()
        if cleaned.startswith("```java"):
            cleaned = cleaned[7:]
        if cleaned.startswith("```"):
            cleaned = cleaned[3:]
        if cleaned.endswith("```"):
            cleaned = cleaned[:-3]
        return cleaned.strip()

    def _run_command(self, command_parts: list[str]) -> str:
        command = subprocess.list2cmdline(command_parts)
        result = subprocess.run(command, shell=True, capture_output=True, text=True)

        output_parts = [part.strip() for part in [result.stdout, result.stderr] if part]
        if output_parts:
            return "\n".join(output_parts).strip()

        return f"Command finished with exit code {result.returncode}".strip()

    def _log_analysis_output(self, analysis_output: str) -> None:
        """Formata e imprime a saída da análise padronizada (Ciano e indentado)."""
        indented_output = "\n".join(f"      | {line}" for line in analysis_output.splitlines())
        colored_output = f"\033[96m{indented_output}\033[0m"
        self.logger.info("Resultados da Análise do %s:\n%s", self.tool_display_name, colored_output)

    def _run_analysis_node(self, state: AgentState):
        self.logger.info(
            "Executando nó: RUN %s (Iteração %d)", 
            self.tool_display_name.upper(), 
            state['iterations']
        )

        current_code = state.get("current_code", state["original_code"])

        with tempfile.TemporaryDirectory() as temp_dir:
            java_file_path = Path(temp_dir) / "PRCode.java"
            java_file_path.write_text(current_code, encoding="utf-8")

            analysis_output = self._run_command(
                self._build_tool_command(java_file_path, temp_dir, state["tool_config"])
            )

            self._log_analysis_output(analysis_output)

        return {
            "analysis_output": analysis_output.strip(),
            "current_code": current_code,
            "iterations": state["iterations"] + 1,
        }

    def _select_config_node(self, state: AgentState):
        """Usa o LLM para selecionar a configuração correta da ferramenta."""
        self.logger.info("Executando nó: SELECT %s CONFIG", self.tool_display_name.upper())
        
        config = self._determine_tool_config(state["pr_description"], state["contributing_md"])
        return {"tool_config": config}

    def _fix_code_node(self, state: AgentState):
        self.logger.info(
            "Executando nó: FIX CODE - Acionando LLM para corrigir erros de %s...",
            self.tool_display_name
        )

        prompt = self._build_fix_prompt()
        chain = prompt | self.llm
        response = chain.invoke(
            {
                "pr_description": state["pr_description"],
                "contributing_md": state["contributing_md"],
                "current_code": state["current_code"],
                "analysis_output": state["analysis_output"],
                "tool_display_name": self.tool_display_name,
            }
        )

        new_code = self._strip_code_fences(self._collect_llm_content(response))

        new_history = state.get("fixes_history", []) + [
            {
                "iteration": str(state["iterations"]),
                "analysis_output": state["analysis_output"],
                "new_code": new_code,
            }
        ]

        return {"current_code": new_code, "fixes_history": new_history}

    def _generate_report_node(self, state: AgentState):
        self.logger.info("Executando nó: GENERATE REPORT")

        report = f"### {self.tool_display_name} Evaluation Report\n\n"
        if not state["analysis_output"] or not self._analysis_has_findings(
            state["analysis_output"]
        ):
            report += (
                f"Status: Análise {self.tool_display_name} finalizada com sucesso. Nenhum erro encontrado.\n"
            )
        else:
            report += (
                f"Status: Parcial. Atingiu max iterações ({state['max_iterations']}).\n"
            )
            report += (
                f"Últimos achados observados:\n```text\n{state['analysis_output']}\n```\n"
            )

        report += (
            f"\nForam feitas {len(state['fixes_history'])} tentativas de correção no código.\n"
        )

        return {"final_report": report}

    def _should_continue(self, state: AgentState):
        has_findings = self._analysis_has_findings(state["analysis_output"])

        if has_findings and state["iterations"] < state["max_iterations"]:
            self.logger.info(
                "Decisão: Continuar corrigindo (Fix Code). Iterações: %d/%d",
                state['iterations'],
                state['max_iterations']
            )
            return "fix_code"

        self.logger.info(
            "Decisão: Encerrar e gerar relatório (Generate Report). Achados=%s",
            has_findings
        )
        return "generate_report"

    def _build_graph(self):
        workflow = StateGraph(AgentState)

        workflow.add_node("select_config", self._select_config_node)
        workflow.add_node("run_analysis", self._run_analysis_node)
        workflow.add_node("fix_code", self._fix_code_node)
        workflow.add_node("generate_report", self._generate_report_node)

        workflow.set_entry_point("select_config")
        workflow.add_edge("select_config", "run_analysis")
        
        workflow.add_conditional_edges(
            "run_analysis",
            self._should_continue,
            {"fix_code": "fix_code", "generate_report": "generate_report"},
        )
        workflow.add_edge("fix_code", "run_analysis")
        workflow.add_edge("generate_report", END)

        return workflow.compile()

    def run(
        self,
        pr_description: str,
        contributing_md: str,
        original_code: str,
        max_iterations: int = 3,
    ) -> AgentState:
        initial_state = AgentState(
            pr_description=pr_description,
            contributing_md=contributing_md,
            original_code=original_code,
            current_code=original_code,
            analysis_output="",
            fixes_history=[],
            iterations=0,
            max_iterations=max_iterations,
            final_report="",
            tool_config="",
        )
        return self.app.invoke(initial_state)

    @abstractmethod
    def _build_tool_command(
        self, java_file_path: Path, temp_dir: str, tool_config: str
    ) -> list[str]:
        raise NotImplementedError

    @abstractmethod
    def _determine_tool_config(self, pr_description: str, contributing_md: str) -> str:
        raise NotImplementedError

    @abstractmethod
    def _build_fix_prompt(self) -> PromptTemplate:
        raise NotImplementedError

    @abstractmethod
    def _analysis_has_findings(self, analysis_output: str) -> bool:
        raise NotImplementedError