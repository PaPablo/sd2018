#!/usr/bin/env python3

import os
import cgi
import jinja2

from utils.utils import print_template, get_template, get_dict_from_fieldstorage
from utils.logger import Logger
from models.alumno import Alumno, FIELD_CONSTRAINTS
from db.get_orm import get_orm

def get_user_constraints():
    """Devuelve las distintas condiciones que tienen que cumplir los campos de un nuevo usuario"""

    #Devuelve un dict de dicts
    # El dict mayor tiene como clave los id de los inputs,
    # y cada sub dict tiene una funcion de validacion ("function"),
    # y un mensaje ("message")
    return {
        "nombre": {
            "function": lambda n: len(n) <= FIELD_CONSTRAINTS["max_nombre"],
            "message": "Nombre demasiado largo. {} caracteres como máximo".format(
                FIELD_CONSTRAINTS["max_nombre"])},
        "legajo": {
            "function": lambda l: len(str(l)) <= FIELD_CONSTRAINTS["max_legajo"],
            "message": "Legajo incorrecto. {} dígitos como máximo".format(
                FIELD_CONSTRAINTS["max_legajo"])},
        "edad": {
            "function": lambda e: len(str(e)) <= FIELD_CONSTRAINTS["max_edad"],
            "message": "Edad incorrecta. {} dígitos como máximo".format(
                FIELD_CONSTRAINTS["max_edad"]
            )},
    }

def user_exists(user):
    """Verifica que el usuario no exista previamente"""
    alumnos = get_orm().get_all()
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
        get_orm().create(alumno)
        Logger.info(f"Usuario creado con éxito [{alumno}]")
        return template.render(user_created=True)
    else:
        Logger.info("USUARIO NO ES VALIDO - NO CREAR :(")
        return template.render(FIELD_CONSTRAINTS, errors=errors, user=user)


def get():
    """Devolver la página correspondiente"""
    template = get_template("signup.html")
    return template.render(FIELD_CONSTRAINTS)

def main():
    req_method = os.getenv("REQUEST_METHOD")
    Logger.info("EL METODO ES [{}]".format(req_method))
    if req_method == "GET":
        print_template(get())
    elif req_method == "POST":
        print_template(post())

if __name__ == "__main__":
    main()
