#!/usr/bin/env python3

import jinja2
from utils.utils import print_headers, get_template

def main():
    print_headers()
    template = get_template("login.html")
    user = {"username": "Luciano"}
    print(template.render(is_logged_in=False,
                          user=user))


if __name__ == "__main__":
    main()
