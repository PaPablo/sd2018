from db.orm import ORM
from models.alumno import Alumno

class ORMAlumnos(ORM):

    """ORM para obtener alumnos"""

    PK_FIELD = "legajo"

    def __init__(self, conn, table="Alumno"):
        super().__init__(conn, table)

    def get_by_id(self, pk):
        cur = self.conn.cursor()
        alumno = cur.execute("SELECT * FROM {} WHERE {} = {}".format(
            self.table,
            self.PK_FIELD,
            pk
        )).fetchone()
        cur.close()

        return Alumno.from_row(alumno)

    def create(self, alumno):
        """Guarda un nuevo alumno en la BD"""
        cur = self.conn.cursor()

        query = "INSERT INTO {} VALUES (?,?,?,?,?)".format(
            self.table
        ), tuple(alumno.as_dict().values())

        cur.execute(query)
        cur.close()
        self.conn.commit()

    def update(self, alumno):
        """Actualiza un alumno existente en la BD"""
        cur = self.conn.cursor()
        fields = []
        for k, v in alumno.as_dict().items():
            fields.append("{}='{}'".format(k, v))

        query = "UPDATE {table} SET {update_query} WHERE {pk_field} = {pk}".format(
            table=self.table,
            update_query=",".join(fields),
            pk_field=self.PK_FIELD,
            pk=getattr(alumno, self.PK_FIELD)
        )

        #TODO: OJO QUE ESTARÍA PISANDO CLAVES AL MODIFICAR
        cur.execute(query)

        cur.close()
        self.conn.commit()
