#!/usr/bin/env python3

import re
import jinja2
import os
from cgi import FieldStorage
from utils.utils import print_template, get_template
from utils.logger import Logger
from session.login import logout
from db.get_orm import get_orm
from session.session import is_authenticated

RANGE_REGEX = re.compile("^(\d+)-(\d+)$")

def get_min_max(s, regex=RANGE_REGEX):
    _min, _max = regex.findall(s)[0]
    return int(_min), int(_max)

def get_filter_functions():
    def _filter_nombre(alumno, nombre):
        cleaned_nombre = nombre.strip().lower().split()
        nombre_alumno = alumno.nombre.strip().lower().split()

        for n in cleaned_nombre:
            if n in nombre_alumno:
                return alumno
    def _filter_legajo(alumno, legajo):
        try:
            _min, _max = get_min_max(legajo)
            return alumno.legajo >= _min and alumno.legajo <= _max
        except IndexError as e:
            return alumno.legajo == int(legajo)
        except ValueError as e:
            return alumno
    def _filter_sexo(alumno, sexo):
        return alumno.sexo == sexo
    def _filter_edad(alumno, edad):
        try:
            _min, _max = get_min_max(edad)
            return alumno.edad >= _min and alumno.edad <= _max
        except IndexError as e:
            return alumno.edad == int(edad)
    return {
        "filtro-nombre": _filter_nombre,
        "filtro-legajo": _filter_legajo,
        "filtro-sexo": _filter_sexo,
        "filtro-edad": _filter_edad,
    }

def main():
    template = get_template("index.html")
    form = FieldStorage()

    user = is_authenticated("SD-CGI")
    logged_alumno = get_orm().get_by_id(user["legajo"]) if user else None
    try:
        is_logged_in = logged_alumno.are_credentials_valid(user)
    except AttributeError as e:
        print(logout())
        is_logged_in = False

    alumnos = get_orm().get_all()

    filter_functions = get_filter_functions()
    for field in form:
        func = filter_functions[field]
        alumnos = [alumno for alumno in alumnos if func(alumno, form[field].value)]

    # Logger.info(f"alumnos => {alumnos}")

    print_template(template.render(
        is_logged_in=is_logged_in,
        alumnos=alumnos,
        alumno=logged_alumno))


if __name__ == "__main__":
    main()
