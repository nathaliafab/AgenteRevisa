import argparse
from dotenv import load_dotenv

from agents.pmd_agent import PMDAgent
from agents.checkstyle_agent import CheckStyleAgent
from agents.spotbugs_agent import SpotBugsAgent
from utils import setup_logger

# Load environment variables from .env
load_dotenv()
logger = setup_logger()

PR_DESCRIPTION = "Add user management."
CONTRIBUTING_MD = "Use 'CamelCase' and follow Google Java Style."

# Intentional issues in the Java code below:
# 1. Class starts with a lowercase letter (userManager)
# 2. Method starts with an uppercase letter (DoSomething)
# 3. Unused imports (ArrayList) and attributes breaking naming conventions (STATUS)
# 4. Swallowed exception with an empty catch and division by zero
# 5. Deep nesting / unnecessary complexity with if(true)
# 6. Unused variables and methods (unusedMethod, unused variable)
# 7. Security: Unchecked access causing obvious NullPointerException (items.get(0).length())
# 8. Concurrency: Static formatter is not thread-safe (SimpleDateFormat).
JAVA_MOCK_CODE = """import java.util.List;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.Date;

public class userManager {
    private int STATUS = 0;
    
    // SimpleDateFormat is NOT thread-safe. Marked as static final, sharing it across threads causes hidden exceptions.
    private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    public void DoSomething(List<String> items) {
        int variableA = 10;
        
        try {
            int calc = variableA / 0;
        } catch (Exception ex) {
            // empty catch
        }
        
        // Clear example of potential NullPointerException:
        List<String> mockItems = null;
        if (mockItems.get(0).length() > 0) {
            System.out.println("Has items");
        }
        
        for(int i = 0; i < 10; i++) {
            for(int j = 0; j < 10; j++) {
                if (true) {
                    System.out.println("nested loop on day: " + formatter.format(new Date()));
                }
            }
        }
    }
    
    private void unusedMethod() {
        String test = "unused";
    }
}"""

AGENT_MAP = {
    "pmd": PMDAgent,
    "checkstyle": CheckStyleAgent,
    "spotbugs": SpotBugsAgent,
}


def parse_arguments() -> argparse.Namespace:
    """Parses command line arguments."""
    parser = argparse.ArgumentParser(description="Java Code Review Agent.")

    parser.add_argument(
        "--tool",
        choices=list(AGENT_MAP.keys()),
        default="spotbugs",
        help="Analysis tool to be used (default: spotbugs)",
    )

    parser.add_argument(
        "--max-iter",
        type=int,
        default=3,
        help="Maximum number of iterations the agent can perform (default: 3)",
    )

    return parser.parse_args()


def _build_agent(agent_name: str):
    """Factory function to build the selected analysis agent."""
    normalized = agent_name.strip().lower()
    agent_class = AGENT_MAP.get(normalized)

    if not agent_class:
        valid_agents = ", ".join(f"'{k}'" for k in AGENT_MAP.keys())
        raise ValueError(f"Invalid agent: '{agent_name}'. Use {valid_agents}.")

    return agent_class()


def main():
    args = parse_arguments()
    agent = _build_agent(args.tool)

    logger.info("Starting agent execution")
    logger.info("Agent: %s", agent.__class__.__name__)
    logger.info("Model: %s", agent.actual_model)
    logger.info("Tool: %s", agent.tool_display_name)
    logger.info("Max Iterations: %d", args.max_iter)

    result = agent.run(
        pr_description=PR_DESCRIPTION,
        contributing_md=CONTRIBUTING_MD,
        original_code=JAVA_MOCK_CODE,
        max_iterations=args.max_iter,
    )

    print(result.get("final_report", "No final report generated."))
    print(result.get("current_code", "No code returned."))


if __name__ == "__main__":
    main()
