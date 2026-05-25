import logging
import sys
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
