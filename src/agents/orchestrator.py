from typing import Dict, Any
from pathlib import Path

from agents.pmd_agent import PMDAgent
from agents.checkstyle_agent import CheckStyleAgent
from agents.spotbugs_agent import SpotBugsAgent
from agents.test_generation_agent import TestGenerationAgent
from utils import setup_logger, get_timestamped_output_path, save_execution_output

class Orchestrator:
    def __init__(self):
        self.logger = setup_logger(self.__class__.__name__)
        self.tool_display_name = "Orchestrator"
        self.actual_model = "orchestrator"
        self.test_agent = TestGenerationAgent()
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
        file_name: str = "Unknown.java",
    ) -> Dict[str, Any]:
        self.logger.info("Gerando testes de integração baseados no código original do arquivo: %s", file_name)
        # Gera os testes base que servem de baseline
        generated_tests = self.test_agent.run(original_code=original_code, max_iterations=max_iterations)

        current_code = original_code
        max_cycles = 5
        cycle = 0

        self.logger.info("Starting integration of all agents")
        self.logger.info("Max cycles: %d", max_cycles)

        final_report = "### Orchestrator Evaluation Report\n\n"
        if generated_tests:
            final_report += "### Baseline Tests Generated\n```java\n" + generated_tests + "\n```\n\n"

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
                    generated_tests=generated_tests,
                    file_name=file_name,
                )
                current_code = result.get("current_code", current_code)
                generated_tests = result.get("generated_tests", generated_tests)
                
                report = result.get("final_report", "No report")
                final_report += f"**{agent.tool_display_name} Report:**\n{report}\n\n"

            if code_before_cycle == current_code:
                self.logger.info("No changes made in cycle %d. All agents are satisfied. Stopping.", cycle)
                final_report += f"\nNo changes made in cycle {cycle}. All agents are satisfied. Stopping.\n"
                break
        
        output_dir = Path("output")
        base_file = Path(file_name).stem
        
        output_path = save_execution_output(
            output_dir=output_dir,
            base_file=base_file,
            tool_name="orchestrator",
            report=final_report,
            current_code=current_code,
            test_code=generated_tests,
        )
        self.logger.info("Arquivo de saida gerado em %s", output_path)

        return {
            "current_code": current_code,
            "final_report": final_report,
            "output_path": str(output_path),
        }
