import unittest
import hashlib
import os

from db.db import Connection
from db.orm_alumnos import ORMAlumnos
from models.alumno import Alumno

class TestORMAlumnos(unittest.TestCase):

    """Test case para el ORM de Alumno."""

    DB_NAME="alumnos.test.db"

    def setUp(self):
        self.conn = Connection.get_conn(self.DB_NAME)
        self.cur = self.conn.cursor()
        self.orm = ORMAlumnos(self.conn)

    def tearDown(self):
        try:
            os.remove(self.DB_NAME)
        except FileNotFoundError as e:
            pass
        Connection._reset()

    def create_alumnos(self):
        password = hashlib.sha256("UnaPasswordReSegura".encode()).hexdigest()
        alumno = Alumno(1, "Juancho Pancho", "m", 30, password)
        self.orm.create(alumno)
        password = hashlib.sha256("OtraPasswordMuySegura".encode()).hexdigest()
        alumno = Alumno(2, "Speedy gonzalez", "f", 55, password)
        self.orm.create(alumno)

    def test_el_testcase_anda(self):
        self.assertEqual(1, 1)

    # def test_la_BD_esta_vacia(self):
        # alumnos = self.cur.execute("SELECT * FROM Alumno").fetchall()
        # self.assertEqual(len(alumnos), 0)

    # def test_alumno_se_carga_correctamente(self):
        # self.create_alumnos()
        # alumnos = self.cur.execute("SELECT * FROM Alumno").fetchall()
        # self.assertNotEqual(len(alumnos), 0)

    # def test_orm_devuelve_alumno(self):
        # self.create_alumnos()
        # alumno = self.orm.get_by_id(1)
        # self.assertEqual(alumno.nombre, "Juancho Pancho")

    # def test_orm_actualiza_correctamente(self):
        # self.create_alumnos()
        # alumno = self.orm.get_by_id(1)
        # alumno.nombre = "Pepe Luis"
        # self.orm.update(alumno)
        # alumno_actualizado = self.orm.get_by_id(1)
        # self.assertEqual(alumno_actualizado.nombre, "Pepe Luis")
