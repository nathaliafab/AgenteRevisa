import logging
import sys
import re
from pathlib import Path
from datetime import datetime

def setup_logger(name: str = "AgenteRevisa") -> logging.Logger:
    """Configura o logger padrão para o projeto."""
    logger = logging.getLogger(name)
    
    if not logger.handlers:
        logger.setLevel(logging.INFO)
        
        # Formato: [2024-05-14 10:00:00] [INFO] [AgenteRevisa] Mensagem
        formatter = logging.Formatter(
            '[%(asctime)s] [%(levelname)s] [%(name)s] %(message)s',
            datefmt='%Y-%m-%d %H:%M:%S'
        )
        
        # Handler para o console
        handler = logging.StreamHandler(sys.stdout)
        handler.setFormatter(formatter)
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


def save_execution_output(
    output_dir: Path,
    base_file: str,
    tool_name: str,
    report: str,
    current_code: str,
    test_code: str,
) -> Path:
    """
    Saves the execution results in a timestamped folder inside output_dir.
    Creates 3 files:
    - 1 Markdown report (report content + final code)
    - 1 Java file with the altered/final code (named base_file.java)
    - 1 Java file with the tests from the last iteration (named after test class, defaulting to PRCodeTest.java)
    
    Returns:
        Path to the generated markdown report file
    """
    output_dir.mkdir(parents=True, exist_ok=True)
    
    base_file_stem = Path(base_file).stem
    
    # Generate timestamp for folder name (avoiding collisions)
    ts = datetime.now().strftime("%Y%m%d_%H%M%S")
    folder_name = f"{base_file_stem}_{ts}"
    folder_path = output_dir / folder_name
    
    seq = 1
    while folder_path.exists():
        folder_path = output_dir / f"{base_file_stem}_{ts}_{seq}"
        seq += 1
        
    folder_path.mkdir(parents=True, exist_ok=True)
    
    # 1. Save Markdown Report
    output_name = f"{base_file_stem}_{tool_name}_output.md"
    markdown_path = folder_path / output_name
    markdown_content = f"{report}\n\n### Final Code\n\n```java\n{current_code}\n```\n"
    markdown_path.write_text(markdown_content, encoding="utf-8")
    
    # 2. Save Altered Java Code
    java_file_name = f"{base_file_stem}.java"
    java_path = folder_path / java_file_name
    java_path.write_text(current_code or "", encoding="utf-8")
    
    # 3. Save Test Java Code
    # Identify test class name from test_code or default to PRCodeTest.java
    test_file_name = "PRCodeTest.java"
    if test_code:
        match = re.search(r"class\s+(\w+)", test_code)
        if match:
            test_file_name = f"{match.group(1)}.java"
    
    test_path = folder_path / test_file_name
    # If no test code, write a placeholder comment
    test_file_content = test_code if test_code else "// No tests were generated in this execution.\n"
    test_path.write_text(test_file_content, encoding="utf-8")
    
    return markdown_path

