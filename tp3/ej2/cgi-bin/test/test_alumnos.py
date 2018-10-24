import unittest
from hashlib import sha256

from models.alumno import Alumno

class TestAlumnos(unittest.TestCase):

    """Test case para la clase Alumno."""

    def setUp(self):
        password = sha256("UnaPasswordMuySegura".encode()).hexdigest()
        self.alumno = Alumno(1, "Juancho", "m", 30, password)

    def tearDown(self):
        pass

    def test_anda(self):
        self.assertEqual(1, 1)

    def test_genera_un_alumno_bien_desde_un_dict(self):
        password = sha256("UnaPasswordMuySegura".encode()).hexdigest()
        alumno_dict = {
            "legajo": 1,
            "nombre": "Juancho",
            "sexo": "m",
            "edad": 30,
            "password": password
        }

        alumno = Alumno.from_dict(alumno_dict)
        self.assertIsNotNone(alumno)
        self.assertEqual(self.alumno.nombre, alumno.nombre)

    def test_valida_contrasenia(self):
        password = sha256("UnaPasswordMuySegura".encode()).hexdigest()
        alumno = {"legajo": 1, "password": password}
        self.assertTrue(self.alumno.are_credentials_valid(alumno))
        password = sha256("OtraPassword".encode()).hexdigest()
        alumno = {"legajo": 1, "password": password}
        self.assertFalse(self.alumno.are_credentials_valid(alumno))

