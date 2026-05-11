from pmd_agent import PMDAgent
from dotenv import load_dotenv

load_dotenv()  # Carrega as variáveis de ambiente do .env


def main():
    pmd_agent = PMDAgent(model_name="gemini-3-flash-preview")

    resultado = pmd_agent.run(
        pr_description="Adicionar manipulação de usuários.",
        contributing_md="Use 'CamelCase'.",
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
