import argparse
from agents.pmd_agent import PMDAgent
from agents.checkstyle_agent import CheckStyleAgent
from dotenv import load_dotenv

load_dotenv()  # Carrega as variáveis de ambiente do .env


def _build_agent(agent_name: str):
    normalized = agent_name.strip().lower()
    if normalized == "pmd":
        return PMDAgent(model_name="gemini-3-flash-preview")
    if normalized == "checkstyle":
        return CheckStyleAgent(model_name="gemini-2.5-flash")

    raise ValueError(
        f"Agente inválido: '{agent_name}'. Use 'pmd' ou 'checkstyle'."
    )


def main():
    parser = argparse.ArgumentParser(description="Agente de Revisão de Código Java.")
    parser.add_argument(
        "--tool",
        choices=["pmd", "checkstyle"],
        default="pmd",
        help="Ferramenta de análise a ser utilizada (padrão: pmd)",
    )
    
    args = parser.parse_args()
    agent = _build_agent(args.tool)

    resultado = agent.run(
        pr_description="Adicionar manipulação de usuários.",
        contributing_md="Use 'CamelCase' e siga o Google Java Style.",
        original_code="""public class Main {
    public void metodoError() {
        try {
            int a = 1 / 0;
        } catch (Exception e) {
            // catch vazio proposital!
        }
    }
}""",
        max_iterations=3,
    )

    print(resultado["final_report"])
    print(resultado["current_code"])


if __name__ == "__main__":
    main()
