#!/usr/bin/env python3

import jinja2
import os
from http.cookies import SimpleCookie
from utils.utils import print_template, get_template
from utils.logger import Logger

def main():
    cookie = SimpleCookie(os.getenv("HTTP_COOKIE"))
    Logger.info(f"Cookie => {cookie}")
    template = get_template("index.html")
    user = None
    print_template(template.render(user=user))


if __name__ == "__main__":
    main()
