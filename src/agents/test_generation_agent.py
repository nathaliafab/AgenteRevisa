import os
import subprocess
import tempfile
import re
from pathlib import Path
from typing import TypedDict

from langchain_core.prompts import PromptTemplate
from langchain_google_genai import ChatGoogleGenerativeAI
from langgraph.graph import END, StateGraph
from utils import setup_logger

class TestAgentState(TypedDict):
    original_code: str
    test_code: str
    test_output: str
    iterations: int
    max_iterations: int

class TestGenerationAgent:
    def __init__(self, model_name: str | None = None):
        self.logger = setup_logger(self.__class__.__name__)
        config_model = os.getenv("REVIEW_MODEL", "gemini-3-flash-preview")
        self.actual_model = model_name or config_model

        self.llm = ChatGoogleGenerativeAI(
            model=self.actual_model, api_key=os.getenv("GOOGLE_API_KEY")
        )
        self.app = self._build_graph()

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

    def _generate_or_run_tests_node(self, state: TestAgentState):
        self.logger.info("Executando nó: RUN TESTS (Iteração %d)", state['iterations'])

        original_code = state["original_code"]
        test_code = state.get("test_code", "")

        if not test_code:
            self.logger.info("Gerando testes iniciais...")
            prompt = PromptTemplate.from_template(
                "Crie uma classe de teste JUnit 5, chamada PRCodeTest (sem pacote), para a seguinte classe Java garantindo "
                "que as regras de negócio implícitas sejam testadas e o JUnit a rode com sucesso.\n"
                "Retorne APENAS o código fonte da classe de testes usando org.junit.jupiter.api.Test e org.junit.jupiter.api.Assertions.\n\n"
                "Código Original:\n```java\n{code}\n```"
            )
            chain = prompt | self.llm
            response = chain.invoke({"code": original_code})
            test_code = self._strip_code_fences(self._collect_llm_content(response))
            
            indented_test = "\n".join(f"      | {line}" for line in test_code.splitlines())
            self.logger.info("Testes gerados:\n\033[92m%s\033[0m", indented_test)
            
            return {
                "test_code": test_code,
                "iterations": state["iterations"],
            }

        with tempfile.TemporaryDirectory() as temp_dir:
            match = re.search(r"class\s+(\w+)\s*{", original_code)
            class_name = match.group(1) if match else "PRCode"
            
            java_file_path = Path(temp_dir) / f"{class_name}.java"
            java_file_path.write_text(original_code, encoding="utf-8")

            test_output = self._run_tests(java_file_path, temp_dir, original_code, test_code)

        if test_output:
            self.logger.error("Testes falharam.")
        else:
            self.logger.info("Sucesso! Os testes foram aprovados no código original.")

        return {
            "test_code": test_code,
            "test_output": test_output.strip(),
            "iterations": state["iterations"] + 1,
        }

    def _fix_tests_node(self, state: TestAgentState):
        self.logger.info("Executando nó: FIX TESTS - Acionando LLM para reformular testes inconsistentes...")

        prompt = PromptTemplate.from_template(
            "Você é um engenheiro de QA especialista em Java e JUnit 5.\n"
            "A seguir, tem-se a classe fonte que nunca pode ser alterada:\n```java\n{original_code}\n```\n\n"
            "E a classe de teste atual que você criou:\n```java\n{test_code}\n```\n\n"
            "Os testes falharam com a seguinte saída:\n{test_output}\n\n"
            "Por favor, reformule e corrija PRCodeTest para que os testes passem consistentemente na classe fonte acima.\n"
            "Retorne SOMENTE o código Java puro da classe de teste."
        )
        chain = prompt | self.llm
        response = chain.invoke(
            {
                "original_code": state["original_code"],
                "test_code": state["test_code"],
                "test_output": state["test_output"],
            }
        )

        new_test_code = self._strip_code_fences(self._collect_llm_content(response))

        indented_test = "\n".join(f"      | {line}" for line in new_test_code.splitlines())
        self.logger.info("Testes alterados e reformulados:\n\033[92m%s\033[0m", indented_test)

        return {"test_code": new_test_code}

    def _should_continue(self, state: TestAgentState):
        if not state.get("test_code"):
            return "run_tests"

        if not state["test_output"]:
            return END

        if state["iterations"] >= state["max_iterations"]:
            return END
            
        return "fix_tests"

    def _build_graph(self):
        workflow = StateGraph(TestAgentState)

        workflow.add_node("run_tests", self._generate_or_run_tests_node)
        workflow.add_node("fix_tests", self._fix_tests_node)

        workflow.set_entry_point("run_tests")
        
        workflow.add_conditional_edges(
            "run_tests",
            self._should_continue,
            {"run_tests": "run_tests", "fix_tests": "fix_tests", END: END},
        )
        workflow.add_edge("fix_tests", "run_tests")

        return workflow.compile()

    def run(self, original_code: str, max_iterations: int = 3) -> str:
        initial_state = TestAgentState(
            original_code=original_code,
            test_code="",
            test_output="",
            iterations=0,
            max_iterations=max_iterations,
        )
        final_state = self.app.invoke(initial_state)
        return final_state.get("test_code", "")
