import os
import subprocess
import tempfile
import re
from abc import ABC, abstractmethod
from pathlib import Path
from typing import Dict, List, TypedDict

from langchain_core.prompts import PromptTemplate
from langchain_google_genai import ChatGoogleGenerativeAI
from langgraph.graph import END, StateGraph
from utils import setup_logger, get_timestamped_output_path


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
    suppress_output: bool
    generated_tests: str
    file_name: str 


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

    def _run_tests(self, java_file_path: Path, temp_dir: str, current_code: str, test_code: str) -> str:
        junit_jar = Path("bin/junit-platform-console-standalone.jar").absolute()
        if not junit_jar.exists():
            import urllib.request
            Path("bin").mkdir(exist_ok=True)
            try:
                urllib.request.urlretrieve("https://repo1.maven.org/maven2/org/junit/platform/junit-platform-console-standalone/1.10.0/junit-platform-console-standalone-1.10.0.jar", junit_jar)
            except Exception as e:
                return f"Erro ao baixar JUnit: {e}"

        test_file_path = Path(temp_dir) / "PRCodeTest.java"
        test_file_path.write_text(test_code, encoding="utf-8")
        
        comp_cmd = ["javac", "-d", temp_dir, "-cp", str(junit_jar), str(java_file_path), str(test_file_path)]
        comp_result = subprocess.run(comp_cmd, capture_output=True, text=True)
        if comp_result.returncode != 0:
            return f"Test Compilation Error:\n{comp_result.stderr}"

        run_cmd = ["java", "-jar", str(junit_jar), "-cp", temp_dir, "--scan-classpath"]
        run_result = subprocess.run(run_cmd, capture_output=True, text=True)
        if run_result.returncode != 0:
            return f"Test Execution Error:\n{run_result.stdout}\n{run_result.stderr}"
        
        return ""

    def _run_analysis_node(self, state: AgentState):
        self.logger.info(
            "Executando nó: RUN %s (Iteração %d)", 
            self.tool_display_name.upper(), 
            state['iterations']
        )

        current_code = state.get("current_code", state["original_code"])

        match = re.search(r"class\s+(\w+)\s*{", current_code)
        class_name = match.group(1) if match else "PRCode"

        with tempfile.TemporaryDirectory() as temp_dir:
            java_file_path = Path(temp_dir) / f"{class_name}.java"
            java_file_path.write_text(current_code, encoding="utf-8")

            analysis_output = self._run_command(
                self._build_tool_command(java_file_path, temp_dir, state["tool_config"])
            )

            if state.get("generated_tests") and not self._analysis_has_findings(analysis_output):
                test_output = self._run_tests(java_file_path, temp_dir, current_code, state["generated_tests"])
                if test_output:
                    analysis_output = f"Falhas em testes detectadas:\nO código falhou nos testes gerados. Corrija o código de forma a passar nos testes de regras de negócios mantendo a adequação estática:\n{test_output}".strip()

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
                "generated_tests": state.get("generated_tests", ""),
            }
        )

        content = self._collect_llm_content(response)

        code_match = re.search(r"<CODE>(.*?)</CODE>", content, re.DOTALL)
        test_match = re.search(r"<TEST>(.*?)</TEST>", content, re.DOTALL)

        if code_match:
            new_code = self._strip_code_fences(code_match.group(1))
        else:
            new_code = self._strip_code_fences(content)
            if "<TEST>" in new_code:
                new_code = new_code.split("<TEST>")[0].strip()

        new_test_code = state.get("generated_tests", "")
        if test_match:
            new_test_code = self._strip_code_fences(test_match.group(1))
            self.logger.info("O agente de correção alterou também a classe de testes.")
            indented_test = "\n".join(f"      | {line}" for line in new_test_code.splitlines())
            self.logger.info("Testes atualizados:\n\033[92m%s\033[0m", indented_test)

        new_history = state.get("fixes_history", []) + [
            {
                "iteration": str(state["iterations"]),
                "analysis_output": state["analysis_output"],
                "new_code": new_code,
            }
        ]

        return {"current_code": new_code, "generated_tests": new_test_code, "fixes_history": new_history}

    def _generate_report_node(self, state: AgentState):
        self.logger.info("Executando nó: GENERATE REPORT")

        report = f"### {self.tool_display_name} Evaluation Report\n\n"
        if not state["analysis_output"] or not self._analysis_has_findings(
            state["analysis_output"]
        ):
            report += (
                f"Status: Análise {self.tool_display_name} finalizada com sucesso. Nenhum erro encontrado.\n"
            )
        elif state["iterations"] >= state["max_iterations"]:
            report += (
                f"Status: Parcial. Atingiu max iterações ({state['max_iterations']}).\n"
            )
            report += (
                f"Últimos achados observados:\n```text\n{state['analysis_output']}\n```\n"
            )
        else:
            report += (
                f"Status: Parcial. Erros remanescentes ignorados porque o LLM determinou que não podem ser resolvidos apenas mudando o código.\n"
            )
            report += (
                f"Últimos achados ignorados:\n```text\n{state['analysis_output']}\n```\n"
            )

        report += (
            f"\nForam feitas {len(state['fixes_history'])} tentativas de correção no código.\n"
        )

        if state["fixes_history"]:
            report += "\n### Passos Intermediarios\n"
            for entry in state["fixes_history"]:
                report += (
                    f"\n#### Iteracao {entry['iteration']}\n"
                    "Achados:\n"
                    f"```text\n{entry['analysis_output']}\n```\n"
                    "Codigo Gerado:\n"
                    f"```java\n{entry['new_code']}\n```\n"
                )

        if state.get("suppress_output"):
            self.logger.info("suppress_output=True; skipping writing per-agent output file for %s", self.tool_display_name)
            return {"final_report": report}

        output_dir = Path("output")
        base_file = Path(state.get("file_name", "Unknown")).stem
        tool_name = self.tool_display_name.lower().replace(' ', '_')
        output_name = f"{base_file}_{tool_name}_output.md"
        
        output_path = get_timestamped_output_path(output_dir, output_name)
        output_content = (
            f"{report}\n\n### Final Code\n\n```java\n{state['current_code']}\n```\n"
        )
        output_path.write_text(output_content, encoding="utf-8")
        self.logger.info("Arquivo de saida gerado em %s", output_path)

        return {"final_report": report}

    def _should_continue(self, state: AgentState):
        has_findings = self._analysis_has_findings(state["analysis_output"])

        if not has_findings:
            self.logger.info("Decisão: Encerrar e gerar relatório. Nenhum erro encontrado.")
            return "generate_report"

        if state["iterations"] >= state["max_iterations"]:
            self.logger.info(
                "Decisão: Encerrar. Atingiu max iterações (%d).",
                state['max_iterations']
            )
            return "generate_report"
            
        # Acionar LLM para julgar se os achados são corrigíveis via código
        self.logger.info("Avaliando se os erros remanescentes podem ser resolvidos via código...")
        prompt = PromptTemplate.from_template(
            "You are an expert Java developer evaluating findings from a static analysis tool ({tool_name}).\n"
            "Here is the analysis output:\n{analysis_output}\n\n"
            "Considering the nature of these warnings/errors, can they be fixed by modifying the provided source code?\n"
            "Some warnings might be false positives or issues that cannot be resolved via code edits (e.g., missing specific project-level configs, unavailable global suppressions, or CI/CD environment complaints).\n"
            "Answer ONLY with YES if at least one finding can be fixed by altering the Java code.\n"
            "Answer ONLY with NO if none of the findings can be fixed by modifying the Java code."
        )
        chain = prompt | self.llm
        response = chain.invoke({
            "tool_name": self.tool_display_name, 
            "analysis_output": state["analysis_output"]
        })
        decision_text = self._collect_llm_content(response).strip().upper()

        if "YES" in decision_text:
            self.logger.info(
                "Decisão: Continuar corrigindo (Fix Code). LLM avaliou que erros são corrigíveis. Iterações: %d/%d",
                state['iterations'],
                state['max_iterations']
            )
            return "fix_code"

        self.logger.info("Decisão: Encerrar. LLM avaliou que os erros *NÃO* são corrigíveis mudando o código.")
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
        suppress_output: bool = False,
        generated_tests: str = "",
        file_name: str = "Unknown.java", 
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
            suppress_output=suppress_output,
            generated_tests=generated_tests,
            file_name=file_name,
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