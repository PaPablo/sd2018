#!/usr/bin/env python3

import os
import cgi
import jinja2

from utils.utils import print_headers, get_template, get_dict_from_fieldstorage
from utils.logger import Logger
from db.db import Connection
from db.orm_alumnos import ORMAlumnos
from models.alumno import Alumno

MAX_LENGTH_NOMBRE=70
MAX_LENGTH_LEGAJO=8
MAX_LENGTH_EDAD=2

FIELD_CONSTRAINTS = {
    "max_nombre": MAX_LENGTH_NOMBRE,
    "max_legajo": MAX_LENGTH_LEGAJO,
    "max_edad": MAX_LENGTH_EDAD,
}

ORM = ORMAlumnos(Connection.get_conn())

def get_user_constraints():
    """Devuelve las distintas condiciones que tienen que cumplir los campos de un nuevo usuario"""

    #Devuelve un dict de dicts
    # El dict mayor tiene como clave los id de los inputs,
    # y cada sub dict tiene una funcion de validacion ("function"),
    # y un mensaje ("message")
    return {
        "nombre": {
            "function": lambda n: len(n)<=MAX_LENGTH_NOMBRE,
            "message": "Nombre demasiado largo. {} caracteres como máximo".format(
                MAX_LENGTH_NOMBRE)},
        "legajo": {
            "function": lambda l: len(str(l))<=MAX_LENGTH_LEGAJO,
            "message": "Legajo incorrecto. {} dígitos como máximo".format(
                MAX_LENGTH_EDAD)},
        "edad": {
            "function": lambda e: len(str(e))<=MAX_LENGTH_EDAD,
            "message": "Edad incorrecta. {} dígitos como máximo".format(
                MAX_LENGTH_EDAD
            )},
    }
    return {}

def user_exists(user):
    """Verifica que el usuario no exista previamente"""
    alumnos = ORM.get_all()
    for alumno in alumnos:
        if int(alumno.legajo) == int(user["legajo"]):
            return True
    return False

def check_new_user(user):
    """Verifica que los datos recibidos son correctos para crear un nuevo usuario"""

    errors = {}
    constraints = get_user_constraints()
    for field, value in user.items():
        try:
            constraint = constraints[field]
            is_correct = constraint["function"](value)
            errors[field] = constraint["message"] if not is_correct else None
        except KeyError:
            continue

    errors["usuario"] = "Legajo ya registrado" if user_exists(user) else None

    return {k:v for k, v in errors.items() if v}

def has_errors(errors):
    return len(errors.items()) > 0

def post():
    """Crear usuario"""

    # Acá hay que chequear que los datos que vienen del formulario
    # sean correctos (cumplen con las condiciones necesarias)
    form = cgi.FieldStorage()
    user = get_dict_from_fieldstorage(form)
    Logger.info(user)

    template = get_template("signup.html")

    errors = check_new_user(user)
    Logger.info("errors => {}".format(errors))
    if not has_errors(errors):
        Logger.info("USUARIO VALIDO - CREAR!")
        alumno = Alumno.from_dict(user)
        ORM.create(alumno)
        Logger.info(f"Usuario creado con éxito [{alumno}]")
        print(template.render(user_created=True))
    else:
        Logger.info("USUARIO NO ES VALIDO - NO CREAR :(")
        print(template.render(FIELD_CONSTRAINTS, errors=errors, user=user))


def get():
    """Devolver la página correspondiente"""
    template = get_template("signup.html")
    print(template.render(FIELD_CONSTRAINTS))

def main():
    print_headers()
    req_method = os.getenv("REQUEST_METHOD")
    Logger.info("EL METODO ES [{}]".format(req_method))
    if req_method == "GET":
        return get()
    elif req_method == "POST":
        return post()

if __name__ == "__main__":
    main()
