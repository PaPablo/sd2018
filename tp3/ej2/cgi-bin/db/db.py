import sqlite3
import glob

class Connection:

    """Conexión con una BD sqlite3 mediante un Singleton"""

    DB_NAME = "alumnos.db"
    SCHEMA_DIR = "schema"
    __conn = None

    @staticmethod
    def get_conn():
        """Método estático para obtener la conexión"""
        if Connection.__conn is None:
            Connection()
        return Connection.__conn

    def __init__(self, db_name=DB_NAME):
        """Constructor de la conexión"""
        if Connection.__conn is not None:
            raise Exception("SINGLETON")

        _conn = sqlite3.connect(db_name)
        for sql_script in glob.iglob("{}/*.sql".format(self.SCHEMA_DIR)):
            query = open(sql_script, "r").read()
            _conn.execute(query)
        _conn.commit()
        Connection.__conn = _conn
