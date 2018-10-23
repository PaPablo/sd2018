#!/usr/bin/env python3

import jinja2
import os
import cgi

from utils.utils import print_headers, get_template
from utils.logger import Logger

def post():
    """Loguear usuario"""
    form = cgi.FieldStorage()
    Logger.info(form)

def get():
    template = get_template("login.html")
    print(template.render())

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
