class ORM(object):

    """Docstring for ORM. """

    def __init__(self, conn, table):
        self.conn = conn
        self.table = table

    def get_by_id(self, pk):
        """Devuelve un objeto según el ID de la tabla"""
        raise Exception("NOT IMPLEMENTED")

    def get_all(self):
        """Devuelve todos los objetos de la tabla"""
        cur = self.conn.cursor()

        return cur.execute("SELECT * FROM {}".format(
            self.table
        )).fetchall()
