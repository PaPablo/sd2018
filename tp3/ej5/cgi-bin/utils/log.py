def log(log):
    import logging, os
    LOG_FORMAT = "%(levelname)s - %(message)s"
    logging.basicConfig(level=logging.INFO, format=LOG_FORMAT)
    logger = logging.getLogger(os.path.basename(__file__))
    logger.info(log)
