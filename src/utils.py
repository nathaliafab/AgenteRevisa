import logging
import sys
import difflib
from pathlib import Path
from datetime import datetime

class BeautifulColorFormatter(logging.Formatter):
    """Custom formatter to make logs beautiful, aligned, and colorful."""
    GREY = "\033[90m"
    BLUE = "\033[94m"
    GREEN = "\033[92m"
    YELLOW = "\033[93m"
    RED = "\033[91m"
    BOLD_RED = "\033[1;91m"
    CYAN = "\033[96m"
    RESET = "\033[0m"
    BOLD = "\033[1m"
    
    LEVEL_COLORS = {
        logging.DEBUG: (GREY, "DEBUG"),
        logging.INFO: (GREEN, "INFO "),
        logging.WARNING: (YELLOW, "WARN "),
        logging.ERROR: (RED, "ERROR"),
        logging.CRITICAL: (BOLD_RED, "CRIT "),
    }
    
    def format(self, record: logging.LogRecord) -> str:
        orig_msg = record.getMessage()
        msg = orig_msg
        
        # Custom formatting for known structural messages
        if msg.startswith("Executando nó:"):
            node_name = msg.replace("Executando nó:", "").strip()
            msg = f"{self.BOLD}{self.BLUE}➤ {node_name}{self.RESET}"
        elif "Starting Cycle" in msg:
            cycle_str = msg.strip("- ")
            msg = f"{self.BOLD}{self.YELLOW}═══ {cycle_str} ══════════════════════════════════════════════════{self.RESET}"
        elif msg.startswith("Decisão:"):
            decision_text = msg.replace("Decisão:", "").strip()
            msg = f"{self.BOLD}{self.GREEN}✔ Decisão:{self.RESET} {decision_text}"
        elif msg.startswith("Starting agent execution") or msg.startswith("Starting integration"):
            msg = f"{self.BOLD}{self.CYAN}🚀 {msg}{self.RESET}"
        
        # Extract timestamp and format level/logger
        ts = self.GREY + datetime.fromtimestamp(record.created).strftime("%H:%M:%S") + self.RESET
        
        color, level_str = self.LEVEL_COLORS.get(record.levelno, (self.RESET, record.levelname))
        colored_level = f"{color}{self.BOLD}{level_str}{self.RESET}"
        
        # Pad logger name to a consistent width
        padded_name = f"{record.name:<18}"
        colored_name = f"{self.CYAN}{padded_name}{self.RESET}"
        
        # Build the final header
        prefix = f"[{ts}] [{colored_level}] [{colored_name}]"
        
        # Handle structured blocks vs standard multiline vs single-line
        if orig_msg.startswith("\n") or orig_msg.startswith("┌") or "┌──" in orig_msg:
            title = f"{self.BOLD}{self.YELLOW}📊 DETALHES:{self.RESET}"
            if "ALTERACAO DETECTADA" in orig_msg or "ALTERAÇÃO" in orig_msg:
                title = f"{self.BOLD}{self.YELLOW}⚡ REVISÃO DE CÓDIGO (ALTERAÇÃO):{self.RESET}"
            elif "TEST EXECUTION SUMMARY" in orig_msg:
                title = f"{self.BOLD}{self.GREEN}🧪 RESULTADO DOS TESTES:{self.RESET}"
            elif "ACHADOS DA ANÁLISE" in orig_msg:
                title = f"{self.BOLD}{self.RED}🔍 CONVENÇÕES / ERROS DETECTADOS:{self.RESET}"
                
            formatted_lines = [f"{prefix} {title}"]
            for line in orig_msg.strip("\n").splitlines():
                formatted_lines.append(f"    {line}")
            return "\n".join(formatted_lines)
            
        lines = msg.splitlines()
        formatted_lines = []
        if lines:
            formatted_lines.append(f"{prefix} {lines[0]}")
            indent = " " * 4 + f"{self.GREY}│{self.RESET} "
            for line in lines[1:]:
                formatted_lines.append(f"{indent}{line}")
                
        formatted_message = "\n".join(formatted_lines)
        
        # Handle exceptions if present
        if record.exc_info:
            if not record.exc_text:
                record.exc_text = self.formatException(record.exc_info)
        if record.exc_text:
            indent = " " * 4 + f"{self.GREY}│{self.RESET} "
            indented_exc = "\n".join(f"{indent}{line}" for line in record.exc_text.splitlines())
            formatted_message += f"\n{indented_exc}"
            
        return formatted_message


def setup_logger(name: str = "AgenteRevisa") -> logging.Logger:
    """Configura o logger padrão para o projeto com formatação limpa e colorida."""
    logger = logging.getLogger(name)
    logger.setLevel(logging.INFO)
    
    # Limpa handlers anteriores para evitar duplicações e garantir novo formato
    if logger.handlers:
        logger.handlers.clear()
        
    handler = logging.StreamHandler(sys.stdout)
    handler.setFormatter(BeautifulColorFormatter())
    logger.addHandler(handler)
        
    return logger


def get_timestamped_output_path(output_dir: Path, base_name: str) -> Path:
    """
    Generate a timestamped output filename and avoid overwriting existing files.
    
    If a file with the same timestamp already exists, appends a sequence number.
    
    Args:
        output_dir: Output directory (will be created if not exists)
        base_name: Base filename without timestamp (e.g., "orchestrator_output.md")
    
    Returns:
        Path object pointing to the unique timestamped file
    
    Example:
        get_timestamped_output_path(Path("output"), "my_report.md")
        -> output/my_report_20260524_102522.md (or with _1, _2 if collision)
    """
    output_dir.mkdir(parents=True, exist_ok=True)
    
    # Split filename and extension
    parts = base_name.rsplit(".", 1)
    name = parts[0]
    ext = f".{parts[1]}" if len(parts) > 1 else ""
    
    # Build timestamped filename
    ts = datetime.now().strftime("%Y%m%d_%H%M%S")
    timestamped_name = f"{name}_{ts}{ext}"
    output_path = output_dir / timestamped_name
    
    # Avoid collisions by appending sequence number
    seq = 1
    while output_path.exists():
        output_path = output_dir / f"{name}_{ts}_{seq}{ext}"
        seq += 1
    
    return output_path


def get_colored_diff(old_code: str, new_code: str) -> str:
    """Generates a colored unified diff for terminal display."""
    diff_lines = list(difflib.unified_diff(
        old_code.splitlines(),
        new_code.splitlines(),
        fromfile="Before",
        tofile="After",
        lineterm=""
    ))
    
    colored_lines = []
    for line in diff_lines:
        if line.startswith("+") and not line.startswith("+++"):
            colored_lines.append(f"\033[92m{line}\033[0m")  # Green for addition
        elif line.startswith("-") and not line.startswith("---"):
            colored_lines.append(f"\033[91m{line}\033[0m")  # Red for deletion
        elif line.startswith("@@"):
            colored_lines.append(f"\033[36m{line}\033[0m")  # Cyan for metadata
        else:
            colored_lines.append(line)
            
    return "\n".join(colored_lines)


def get_markdown_diff(old_code: str, new_code: str) -> str:
    """Generates a standard unified diff for Markdown file."""
    diff_lines = list(difflib.unified_diff(
        old_code.splitlines(),
        new_code.splitlines(),
        fromfile="Before",
        tofile="After",
        lineterm=""
    ))
    return "\n".join(diff_lines)


def log_test_summary(
    logger,
    file_name: str,
    comp_returncode: int,
    comp_stderr: str,
    run_returncode: int | None = None,
    run_stdout: str | None = None,
) -> None:
    """Logs a beautifully styled test execution summary box."""
    BOLD = "\033[1m"
    RESET = "\033[0m"
    GREEN = "\033[92m"
    RED = "\033[91m"
    
    summary_lines = []
    summary_lines.append("┌── TEST EXECUTION SUMMARY ─────────────────────────────────────────────────────")
    summary_lines.append(f"│ {BOLD}File:{RESET} {file_name}")
    
    if comp_returncode != 0:
        summary_lines.append(f"│ {BOLD}Compilation:{RESET} {RED}FAILED ✗{RESET}")
        first_err = comp_stderr.strip().splitlines()[0] if comp_stderr else "Unknown compilation error"
        summary_lines.append(f"│ {BOLD}Error:{RESET} {first_err}")
    else:
        summary_lines.append(f"│ {BOLD}Compilation:{RESET} {GREEN}SUCCESSFUL ✓{RESET}")
        
        if run_returncode is not None:
            if run_returncode != 0:
                summary_lines.append(f"│ {BOLD}Tests:{RESET} {RED}FAILED ✗{RESET}")
                if run_stdout:
                    for line in run_stdout.splitlines():
                        if any(marker in line.lower() for marker in ["failed", "failures", "error", "failure"]):
                            summary_lines.append(f"│   {line.strip()}")
            else:
                summary_lines.append(f"│ {BOLD}Tests:{RESET} {GREEN}PASSED ✓{RESET}")
                if run_stdout:
                    for line in run_stdout.splitlines():
                        if any(marker in line.lower() for marker in ["successful", "failed", "tests found"]):
                            summary_lines.append(f"│   {line.strip()}")
                            
    summary_lines.append("└───────────────────────────────────────────────────────────────────────────────")
    logger.info("\n" + "\n".join(summary_lines))


def handle_modification(
    label: str,
    old_text: str,
    new_text: str,
    explanation: str,
    logger
) -> str:
    """
    Prints the colored diff to the terminal, and returns the Markdown representation
    containing the explanation and the diff.
    """
    explanation_clean = explanation.strip() if explanation else "Nenhuma explicação fornecida pelo LLM."
    
    # Generate terminal colored diff
    colored_diff = get_colored_diff(old_text, new_text)
    
    # Format modification box beautifully
    indented_diff_lines = []
    for line in colored_diff.splitlines():
        indented_diff_lines.append(f"│ {line}")
    indented_diff = "\n".join(indented_diff_lines)
    
    yellow_box = (
        f"\n┌── ALTERACAO DETECTADA: {label.upper()} ──────────────────────────────────────────\n"
        f"│ {BeautifulColorFormatter.BOLD}Breve Explicacao:{BeautifulColorFormatter.RESET} {explanation_clean}\n"
        f"├───────────────────────────────────────────────────────────────────────────────\n"
        f"{indented_diff}\n"
        f"└───────────────────────────────────────────────────────────────────────────────\n"
    )
    
    # Print via logger instead of raw print
    logger.info(yellow_box)
    
    # Generate Markdown representation
    md_diff = get_markdown_diff(old_text, new_text)
    md_report = (
        f"#### Alteração no {label}\n"
        f"**Explicação do LLM:** {explanation_clean}\n\n"
        f"```diff\n{md_diff}\n```\n"
    )
    return md_report

