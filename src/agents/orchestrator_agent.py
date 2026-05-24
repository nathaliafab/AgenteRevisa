from typing import Dict, Any
from pathlib import Path

from agents.pmd_agent import PMDAgent
from agents.checkstyle_agent import CheckStyleAgent
from agents.spotbugs_agent import SpotBugsAgent
from utils import setup_logger, get_timestamped_output_path

class OrchestratorAgent:
    def __init__(self):
        self.logger = setup_logger(self.__class__.__name__)
        self.tool_display_name = "Orchestrator Agent"
        self.actual_model = "orchestrator"
        self.agents = [
            SpotBugsAgent(),
            PMDAgent(),
            CheckStyleAgent(),
        ]

    def run(
        self,
        pr_description: str,
        contributing_md: str,
        original_code: str,
        max_iterations: int = 3,
    ) -> Dict[str, Any]:
        current_code = original_code
        max_cycles = 5
        cycle = 0

        self.logger.info("Starting integration of all agents")
        self.logger.info("Max cycles: %d", max_cycles)

        final_report = "### Orchestrator Evaluation Report\n\n"

        while cycle < max_cycles:
            cycle += 1
            self.logger.info("--- Starting Cycle %d ---", cycle)
            code_before_cycle = current_code
            
            final_report += f"\n#### Cycle {cycle}\n\n"
            
            for agent in self.agents:
                self.logger.info("Running agent: %s", agent.tool_display_name)
                result = agent.run(
                    pr_description=pr_description,
                    contributing_md=contributing_md,
                    original_code=current_code,
                    max_iterations=max_iterations,
                    suppress_output=True,
                )
                current_code = result.get("current_code", current_code)
                
                report = result.get("final_report", "No report")
                final_report += f"**{agent.tool_display_name} Report:**\n{report}\n\n"

            if code_before_cycle == current_code:
                self.logger.info("No changes made in cycle %d. All agents are satisfied. Stopping.", cycle)
                final_report += f"\nNo changes made in cycle {cycle}. All agents are satisfied. Stopping.\n"
                break
        
        # Ensure output directory exists and use timestamped filename
        output_dir = Path("output")
        output_path = get_timestamped_output_path(output_dir, "orchestrator_output.md")

        output_content = f"{final_report}\n\n### Final Code\n\n```java\n{current_code}\n```\n"
        output_path.write_text(output_content, encoding="utf-8")
        self.logger.info("Arquivo de saida gerado em %s", output_path)

        return {
            "current_code": current_code,
            "final_report": final_report,
            "output_path": str(output_path),
        }
