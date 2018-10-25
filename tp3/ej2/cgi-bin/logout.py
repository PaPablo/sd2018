#!/usr/bin/env python3

import jinja2
from utils.utils import get_template, print_template
from utils.logger import Logger
from session.session import remove_auth_cookie

def main():

    cookie = remove_auth_cookie("SD-CGI", "localhost")
    Logger.info(f"LOGOUT COOKIE => {cookie}")
    print(cookie)
    template = get_template("logout.html")
    print_template(template.render())


if __name__ == "__main__":
    main()
