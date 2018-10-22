import os
import logging

LOG_FORMAT = "%(levelname)s - %(message)s"

logging.basicConfig(level=logging.INFO, format=LOG_FORMAT)
Logger = logging.getLogger(os.path.basename(__file__))
