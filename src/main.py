import argparse
from agents.pmd_agent import PMDAgent
from agents.checkstyle_agent import CheckStyleAgent
from agents.spotbugs_agent import SpotBugsAgent
from dotenv import load_dotenv
from utils import setup_logger

load_dotenv()  # Carrega as variáveis de ambiente do .env
logger = setup_logger()


def _build_agent(agent_name: str):
    normalized = agent_name.strip().lower()
    if normalized == "pmd":
        return PMDAgent()
    if normalized == "checkstyle":
        return CheckStyleAgent()
    if normalized == "spotbugs":
        return SpotBugsAgent()

    raise ValueError(
        f"Agente inválido: '{agent_name}'. Use 'pmd', 'checkstyle' ou 'spotbugs'."
    )


def main():
    parser = argparse.ArgumentParser(description="Agente de Revisão de Código Java.")
    parser.add_argument(
        "--tool",
        choices=["pmd", "checkstyle", "spotbugs"],
        default="spotbugs",
        help="Ferramenta de análise a ser utilizada (padrão: spotbugs)",
    )
    
    args = parser.parse_args()
    agent = _build_agent(args.tool)

    MAX_ITER = 3
    logger.info("Iniciando execução do agente")
    logger.info(f"Agente: %s", agent.__class__.__name__)
    logger.info(f"Modelo: %s", agent.actual_model)
    logger.info(f"Ferramenta: %s", agent.tool_display_name)
    logger.info(f"Max Iterations: %d", MAX_ITER)

    resultado = agent.run(
        pr_description="Adicionar manipulação de usuários.",
        contributing_md="Use 'CamelCase' e siga o Google Java Style.",
        # Problemas intencionais no código Java abaixo:
        # 1. Classe começa com letra minúscula (userManager)
        # 2. Método começa com letra maiúscula (DoSomething)
        # 3. Imports não utilizados (ArrayList) e atributos que quebram convenção de nome (STATUS)
        # 4. Exceção com catch vazio (swallowed exception) e divisão por zero
        # 5. Estrutura for aninhada (Deep Nesting / complexidade desnecessária) com if(true)
        # 6. Variáveis e métodos não utilizados e não alcançados (unusedMethod, unused variable)
        # 7. Segurança: Acesso sem checagem gerando NullPointerException óbvio (items.get(0).length()) onde items é null local.
        # 8. Concorrência: Formatter estático não thread-safe (SimpleDateFormat).
        original_code="""import java.util.List;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.Date;

public class userManager {
    private int STATUS = 0;
    
    // SimpleDateFormat NÃO é thread-safe. Marcado como static final, compartilhar em multi-thread causa exceções ocultas.
    private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    public void DoSomething(List<String> items) {
        int variableA = 10;
        
        try {
            int calc = variableA / 0;
        } catch (Exception ex) {
            // catch vazio
        }
        
        // Exemplo claro de potencial NullPointerException:
        List<String> mockItems = null;
        if (mockItems.get(0).length() > 0) {
            System.out.println("Has items");
        }
        
        for(int i = 0; i < 10; i++) {
            for(int j = 0; j < 10; j++) {
                if (true) {
                    System.out.println("nested loop no dia: " + formatter.format(new Date()));
                }
            }
        }
    }
    
    private void unusedMethod() {
        String test = "unused";
    }
}""",
        max_iterations=MAX_ITER,
    )

    print(resultado["final_report"])
    print(resultado["current_code"])


if __name__ == "__main__":
    main()
