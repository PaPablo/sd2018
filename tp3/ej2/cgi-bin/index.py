#!/usr/bin/env python3

import jinja2
from utils.utils import print_headers

# def print_headers():
    # print("Content-type: text/html")
    # print()

def main():
    print_headers()
    loader = jinja2.FileSystemLoader("./templates")
    ctx = jinja2.Environment(loader=loader)
    template = ctx.get_template("index.html")
    print(template.render(nombre="Luciano"))


if __name__ == "__main__":
    main()
