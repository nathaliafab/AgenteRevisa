import logging
import sys

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
