#!/usr/bin/env python3

import jinja2
import os
from http.cookies import SimpleCookie
from utils.utils import print_template, get_template
from utils.logger import Logger
from db.get_orm import get_orm

from session.session import is_authenticated

def main():
    template = get_template("index.html")
    logged_alumno = None

    user = is_authenticated("SD-CGI")

    logged_alumno = get_orm().get_by_id(user["legajo"]) if user else None
    is_logged_in = True if logged_alumno else False

    print_template(template.render(is_logged_in=is_logged_in,
                                   alumno=logged_alumno))


if __name__ == "__main__":
    main()
