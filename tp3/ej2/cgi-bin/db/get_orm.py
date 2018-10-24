def get_orm():
    """Devuelve el ORM a utilizar"""
    from db.db import Connection
    from db.orm_alumnos import ORMAlumnos
    return ORMAlumnos(Connection.get_conn())
