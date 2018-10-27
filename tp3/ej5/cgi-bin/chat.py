#!/usr/bin/env python3

import fcntl
import cgi
import os
import logging

LOG_FORMAT = "%(levelname)s - %(message)s"

logging.basicConfig(level=logging.INFO, format=LOG_FORMAT)
log = logging.getLogger(os.path.basename(__file__))

CHATFILE = "data/chat.txt"

def get_template(template_name, templates_dir="./templates"):
    import jinja2
    loader = jinja2.FileSystemLoader(templates_dir)
    ctx = jinja2.Environment(loader=loader)
    return ctx.get_template(template_name)

def main():
    print("Content-type: text/html")
    print()

    form = cgi.FieldStorage()
    log.info(form)

    with open(CHATFILE, "a+") as f:
        # Bloquear archivo
        f.write(f"{form.getvalue('message')}\n")
        f.seek(0)
        fcntl.flock(f, fcntl.LOCK_EX | fcntl.LOCK_NB)
        messages = []
        for l in f.readlines():
            l = l.replace("\n", "").replace("\r", "")
            messages.append(l)

        print(get_template("index.html").render(messages=messages))
        # Liberar archivo
        fcntl.flock(f, fcntl.LOCK_UN)

    print()

if __name__ == "__main__":
    main()
