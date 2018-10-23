import unittest
import hashlib
import os

from db.db import Connection
from db.orm_alumnos import ORMAlumnos
from models.alumno import Alumno

from signup import user_exists, check_new_user

class TestSignup(unittest.TestCase):

    """Test case para la creación de un nuevo Alumno."""

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

    def test_anda(self):
       self.assertEqual(1, 1)

    def test_no_permite_alumnos_con_legajo_duplicado(self):
        alumno = Alumno(1, "Juancho", "m", 21,
                        hashlib.sha256(
                            "UnaPasswordMuySegura".encode()).hexdigest())
        self.orm.create(alumno)
        alumno_dict = {"legajo": 1}
        self.assertTrue(user_exists(alumno_dict, orm=self.orm))
        alumno_dict["legajo"] = 32
        self.assertFalse(user_exists(alumno_dict, orm=self.orm))

    def test_devuelve_error_si_alumno_existe(self):
        alumno = Alumno(1, "Juancho", "m", 21,
                        hashlib.sha256(
                            "UnaPasswordMuySegura".encode()).hexdigest())
        self.orm.create(alumno)
        alumno_dict = {"legajo": 1}
        errors = check_new_user(alumno_dict, orm=self.orm)
        usuario_ya_existe_msg = errors.get("usuario", None)
        self.assertIsNotNone(usuario_ya_existe_msg)
        alumno_dict = {"legajo": 322}
        errors = check_new_user(alumno_dict, orm=self.orm)
        usuario_ya_existe_msg = errors.get("usuario", None)
        self.assertIsNone(usuario_ya_existe_msg)
