import os
import subprocess
import tempfile
from typing import TypedDict, List, Dict
from langgraph.graph import StateGraph, END
from langchain_google_genai import ChatGoogleGenerativeAI
from langchain_core.prompts import PromptTemplate


class AgentState(TypedDict):
    pr_description: str
    contributing_md: str
    original_code: str
    current_code: str
    pmd_errors: str
    fixes_history: List[Dict[str, str]]
    iterations: int
    max_iterations: int
    final_report: str


class PMDAgent:
    def __init__(self, model_name: str = "gemini-3-flash-preview"):
        self.pmd_cmd = os.getenv("PMD_CMD", "bin/pmd-bin-7.24.0/bin/pmd")
        self.llm = ChatGoogleGenerativeAI(
            model=model_name, api_key=os.getenv("GOOGLE_API_KEY")
        )
        self.app = self._build_graph()
        # self.app.get_graph().print_ascii() - tem que ter grandalf instalado para printar o grafo

    def _run_pmd_node(self, state: AgentState):
        """Executa a ferramenta PMD no código Java atual."""

        print(f"\n[DEBUG] Executando nó: RUN PMD (Iteração {state['iterations']})")

        current_code = state.get("current_code", state["original_code"])

        # Cria um diretório temporário para escrever o código Java e rodar o PMD
        with tempfile.TemporaryDirectory() as temp_dir:
            java_file_path = os.path.join(temp_dir, "PRCode.java")
            with open(java_file_path, "w") as f:
                f.write(current_code)

            # O comando PMD esperado
            command = f"{self.pmd_cmd} check -d {temp_dir} -f text -R rulesets/java/quickstart.xml"

            try:
                result = subprocess.run(
                    command, shell=True, capture_output=True, text=True
                )
                pmd_output = result.stdout

                print(f"[DEBUG] PMD Output:\n{pmd_output}")

            except Exception as e:
                pmd_output = str(e)

        return {
            "pmd_errors": pmd_output.strip(),
            "current_code": current_code,
            "iterations": state["iterations"] + 1,
        }

    def _fix_code_node(self, state: AgentState):
        """Usa um LLM para propor alterações e corrigir os erros do PMD."""

        print("[DEBUG] Executando nó: FIX CODE - Acionando LLM para corrigir erros...")

        prompt = PromptTemplate.from_template(
            "Você é um engenheiro de software experiente.\n"
            "Descrição do PR: {pr_description}\n"
            "Regras de Contribuição: {contributing_md}\n\n"
            "O PMD analisou o seguinte código Java...\n"
            "Código atual:\n```java\n{current_code}\n```\n\n"
            "Erros encontrados pelo PMD:\n{pmd_errors}\n\n"
            "Por favor, retorne SOMENTE o código Java corrigido, sem markdown ao redor, para que os erros do PMD sejam resolvidos."
        )

        chain = prompt | self.llm
        response = chain.invoke(
            {
                "pr_description": state["pr_description"],
                "contributing_md": state["contributing_md"],
                "current_code": state["current_code"],
                "pmd_errors": state["pmd_errors"],
            }
        )

        content = response.content
        if isinstance(content, list):
            content = "".join(
                [c.get("text", "") if isinstance(c, dict) else str(c) for c in content]
            )
        elif not isinstance(content, str):
            content = str(content)

        new_code = content.strip()

        # Se o modelo botar marcação markdown, removemos
        if new_code.startswith("\`\`\`java"):
            new_code = new_code[7:]
        if new_code.startswith("\`\`\`"):
            new_code = new_code[3:]
        if new_code.endswith("\`\`\`"):
            new_code = new_code[:-3]

        new_history = state.get("fixes_history", []) + [
            {
                "iteration": state["iterations"],
                "pmd_errors": state["pmd_errors"],
                "new_code": new_code.strip(),
            }
        ]

        return {"current_code": new_code.strip(), "fixes_history": new_history}

    def _generate_report_node(self, state: AgentState):
        """Gera o relatório final para devolver ao usuário."""

        print("[DEBUG] Executando nó: GENERATE REPORT")

        report = "### PMD Evaluation Report\n\n"
        if not state["pmd_errors"]:
            report += (
                "Status: Análise PMD finalizada com sucesso. Nenhum erro encontrado.\n"
            )
        else:
            report += (
                f"Status: Parcial. Atingiu max iterações ({state['max_iterations']}).\n"
            )
            report += (
                f"Últimos erros observados:\n```text\n{state['pmd_errors']}\n```\n"
            )

        report += f"\nForam feitas {len(state['fixes_history'])} tentativas de correção no código.\n"

        return {"final_report": report}

    def _should_continue(self, state: AgentState):
        """Decide se devemos tentar corrigir ou parar baseando nos erros e iterações."""

        print("[DEBUG] Avaliando transição (should_continue)...")

        has_errors = (
            len(state["pmd_errors"]) > 0
            and "No problems found" not in state["pmd_errors"]
        )

        if has_errors and state["iterations"] <= state["max_iterations"]:
            print(
                f"[DEBUG] Decisão: Continuar corrigindo (Fix Code). Iterações: {state['iterations']}/{state['max_iterations']}"
            )
            return "fix_code"

        print(
            f"[DEBUG] Decisão: Encerrar e gerar relatório (Generate Report). Erros={has_errors}"
        )
        return "generate_report"

    def _build_graph(self):
        """Constrói e compila o grafo do agente com o LangGraph."""
        workflow = StateGraph(AgentState)

        # Adiciona os Nós
        workflow.add_node("run_pmd", self._run_pmd_node)
        workflow.add_node("fix_code", self._fix_code_node)
        workflow.add_node("generate_report", self._generate_report_node)

        # Define o fluxo
        workflow.set_entry_point("run_pmd")
        workflow.add_conditional_edges(
            "run_pmd",
            self._should_continue,
            {"fix_code": "fix_code", "generate_report": "generate_report"},
        )
        workflow.add_edge("fix_code", "run_pmd")
        workflow.add_edge("generate_report", END)

        return workflow.compile()

    def run(
        self,
        pr_description: str,
        contributing_md: str,
        original_code: str,
        max_iterations: int = 3,
    ) -> AgentState:
        """Executa o agente para um dado código."""
        initial_state = AgentState(
            pr_description=pr_description,
            contributing_md=contributing_md,
            original_code=original_code,
            current_code=original_code,
            pmd_errors="",
            fixes_history=[],
            iterations=0,
            max_iterations=max_iterations,
            final_report="",
        )
        return self.app.invoke(initial_state)
