#!/usr/bin/env python3

import jinja2
import os
import cgi
from datetime import datetime
from http.cookies import SimpleCookie

from utils.utils import print_template, get_template, get_dict_from_fieldstorage
from utils.logger import Logger
from db.get_orm import get_orm
from models.alumno import FIELD_CONSTRAINTS

def post():
    """Loguear usuario"""
    form = cgi.FieldStorage()
    Logger.info(form)
    user = get_dict_from_fieldstorage(form)

    alumno = get_orm().get_by_id(user["legajo"])
    template = get_template("login.html")
    correct_login = False
    try:
        correct_login = alumno.are_credentials_valid(user)
    except AttributeError as e:
        pass
    if correct_login:
        Logger.info("CREDENCIALES VÁLIDAS => LOGIN :)")
        return(template.render(is_logged_in=True, user=alumno))
    else:
        Logger.info("CREDENCIALES INVÁLIDAS => NOOOO LOGIN D:")
        errors = {"usuario": "Usuario o contraseña inválidos"}
        return(template.render(
            FIELD_CONSTRAINTS, errors=errors, user=user))

def get():
    template = get_template("login.html")
    return(template.render(FIELD_CONSTRAINTS))

def main():
    req_method = os.getenv("REQUEST_METHOD")
    Logger.info("EL METODO ES [{}]".format(req_method))
    if req_method == "GET":
        print_template(get())
    elif req_method == "POST":
        print_template(post())


if __name__ == "__main__":
    main()
