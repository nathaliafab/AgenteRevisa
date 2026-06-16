import argparse
import sys
from pathlib import Path
from dotenv import load_dotenv

from agents.pmd_agent import PMDAgent
from agents.checkstyle_agent import CheckStyleAgent
from agents.spotbugs_agent import SpotBugsAgent
from agents.orchestrator import Orchestrator
from utils import setup_logger

# Load environment variables from .env
load_dotenv()
logger = setup_logger()

PR_DESCRIPTION = "Add user management."
CONTRIBUTING_MD = "Use 'CamelCase' and follow Google Java Style."

AGENT_MAP = {
    "pmd": PMDAgent,
    "checkstyle": CheckStyleAgent,
    "spotbugs": SpotBugsAgent,
}

PROJECT_ROOT = Path(__file__).resolve().parent.parent
DEFAULT_INPUT_DIR = PROJECT_ROOT / "input" 


def parse_arguments() -> argparse.Namespace:
    """Parses command line arguments."""
    parser = argparse.ArgumentParser(description="Java Code Review Agent.")

    parser.add_argument(
        "--tool",
        choices=list(AGENT_MAP.keys()) + ["all"],
        default="all",
        help="Analysis tool to be used (default: all)",
    )

    parser.add_argument(
        "--orchestrator",
        action="store_true",
        help="Wraps the selected tool in the Orchestrator (implied if --tool=all)",
    )

    parser.add_argument(
        "--max-iter",
        type=int,
        default=3,
        help="Maximum number of iterations the agent can perform (default: 3)",
    )

    parser.add_argument(
        "--folder",
        type=str,
        default="",
        help="Subdirectory inside input/ to look for files (e.g., pmd, checkstyle)",
    )

    group = parser.add_mutually_exclusive_group(required=True)
    group.add_argument(
        "--all",
        action="store_true",
        help="Process all .java files in the target directory",
    )
    group.add_argument(
        "--file",
        type=str,
        help="Comma-separated list of files to process (e.g., file1,file2.java)",
    )

    return parser.parse_args()


def _build_agent(args: argparse.Namespace):
    """Factory function to build the selected analysis agent."""
    tool = args.tool.strip().lower()
    
    # Se a tool for "all" OU a flag --orchestrator foi passada, o Orchestrator assume
    use_orchestrator = args.orchestrator or tool == "all"

    # Se pediu "all", retorna o Orchestrator padrão (que internamente carrega todas)
    if tool == "all":
        return Orchestrator()

    # Busca a classe da tool específica
    agent_class = AGENT_MAP.get(tool)
    if not agent_class:
        valid_agents = ", ".join(f"'{k}'" for k in list(AGENT_MAP.keys()) + ["all"])
        raise ValueError(f"Invalid agent: '{tool}'. Use {valid_agents}.")

    single_agent = agent_class() # type: ignore

    if use_orchestrator:
        return Orchestrator(agents=[single_agent])
    
    return single_agent


def get_target_files(args: argparse.Namespace) -> list[Path]:
    """Resolves which files to process based on CLI arguments."""
    # Resolve o diretório alvo: input/ ou input/sua_subpasta
    target_dir = DEFAULT_INPUT_DIR / args.folder if args.folder else DEFAULT_INPUT_DIR

    if not target_dir.exists() or not target_dir.is_dir():
        logger.error(f"Input directory not found: {target_dir}")
        sys.exit(1)

    if args.all:
        return list(target_dir.glob("*.java"))

    if args.file:
        files = []
        file_names = [f.strip() for f in args.file.split(",")]
        for name in file_names:
            if not name.endswith(".java"):
                name += ".java"

            file_path = target_dir / name
            if file_path.exists() and file_path.is_file():
                files.append(file_path)
            else:
                logger.warning(f"File not found or invalid: {file_path}")
        return files

    return []


def main():
    args = parse_arguments()
    files_to_process = get_target_files(args)

    if not files_to_process:
        logger.warning("No valid Java files found to process. Exiting.")
        sys.exit(0)

    agent = _build_agent(args)

    logger.info("Starting agent execution")
    logger.info("Agent: %s", agent.__class__.__name__)
    logger.info("Model: %s", getattr(agent, "actual_model", "Unknown"))
    logger.info("Tool: %s", getattr(agent, "tool_display_name", "Unknown"))
    logger.info("Max Iterations: %d", args.max_iter)
    logger.info("Files to process: %d", len(files_to_process))

    for file_path in files_to_process:
        logger.info("\n" + "=" * 40)
        logger.info(f"Processing: {file_path.name}")
        logger.info("=" * 40)

        try:
            java_code = file_path.read_text(encoding="utf-8")

            result = agent.run(
                pr_description=PR_DESCRIPTION,
                contributing_md=CONTRIBUTING_MD,
                original_code=java_code,
                max_iterations=args.max_iter,
                file_name=file_path.name,
            )

            print(f"\n--- Results for {file_path.name} ---")
            print(result.get("final_report", "No final report generated."))
            print("\n--- Current Code ---")
            print(result.get("current_code", "No code returned."))

        except Exception as e:
            logger.error(f"Failed to process {file_path.name}: {e}")

if __name__ == "__main__":
    main()