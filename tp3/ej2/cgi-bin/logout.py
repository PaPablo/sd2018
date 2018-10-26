#!/usr/bin/env python3

import jinja2
from utils.utils import get_template, print_template
from utils.logger import Logger
from session.login import logout

def main():

    #Remover cookie
    logout_cookie = logout()
    Logger.info(f"LOGOUT COOKIE => {logout_cookie}")
    print(logout_cookie)
    template = get_template("logout.html")
    print_template(template.render())


if __name__ == "__main__":
    main()
