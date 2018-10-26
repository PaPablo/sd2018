#!/usr/bin/env python3

import jinja2
import os
from utils.utils import print_template, get_template
from utils.logger import Logger
from session.login import logout
from db.get_orm import get_orm

from session.session import is_authenticated

def main():
    template = get_template("index.html")

    user = is_authenticated("SD-CGI")
    logged_alumno = get_orm().get_by_id(user["legajo"]) if user else None
    try:
        is_logged_in = logged_alumno.are_credentials_valid(user)
    except AttributeError as e:
        print(logout())
        is_logged_in = False

    print_template(template.render(is_logged_in=is_logged_in,
                                   alumno=logged_alumno))


if __name__ == "__main__":
    main()
