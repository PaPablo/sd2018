import unittest
import os
from unittest.mock import patch
from hashlib import sha256
from pyquery import PyQuery

from db.db import Connection
from db.orm_alumnos import ORMAlumnos
from models.alumno import Alumno
import login


class TestLogin(unittest.TestCase):

    """Test case para el script de login."""

    DB_NAME="alumnos.test.db"
    FAKE_USER = {
        "legajo": 1,
        "password": sha256("UnaPasswordMuySegura".encode()).hexdigest()
    }

    def setUp(self):
        self.conn = Connection.get_conn(db_name=self.DB_NAME)
        self.orm = ORMAlumnos(self.conn)

    def tearDown(self):
        Connection._reset()
        try:
            os.remove(self.DB_NAME)
        except FileNotFoundError as e:
            pass

    def test_loguea_usuario_valido(self):
        with patch("login.get_dict_from_fieldstorage",
                   return_value=self.FAKE_USER):
            with patch("login.get_orm", return_value=self.orm):
                alumno = Alumno(
                    1, "Juancho", "m", 21,
                    sha256("UnaPasswordMuySegura".encode()).hexdigest())
                self.orm.create(alumno)
                pq = PyQuery(login.post())
                #splitear las lineas por el '\n' con "splitlines()"
                res = pq("div#form-success").text().lower().splitlines()
                self.assertEqual(res[0], "pudo ingresar correctamente")

    def test_no_loguea_usuario_invalido(self):
        with patch("login.get_dict_from_fieldstorage",
                   return_value=self.FAKE_USER):
            with patch("login.get_orm", return_value=self.orm):
                alumno = Alumno(
                    1, "Juancho", "m", 21,
                    sha256("OtraPassword".encode()).hexdigest())
                self.orm.create(alumno)
                pq = PyQuery(login.post())
                res = pq("div#form-success").text().lower().splitlines()
                self.assertListEqual(
                    res, [], msg="No se encontró el mensaje de login exitoso")
