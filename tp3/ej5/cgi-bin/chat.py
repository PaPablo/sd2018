#!/usr/bin/env python3

import fcntl
import cgi
import os
import time


from utils.utils import get_template, print_headers
from utils.log import log
from session.session import is_authenticated
from session.login import login, logout


CHATFILE = "data/chat.txt"
USERSFILE = "data/users.txt"
COOKIE_NAME = "SD-CGI-CHAT"


def get_messages_from_lines(lines, starting=0):
    """Devuelve una lista de los mensajes con el usuario que lo escribió"""
    _messages = []
    for i, l in enumerate(lines):
        if i >= starting:
            l = l.replace("\n", "").replace("\r", "").split(":")
            l = [line.strip() for line in l]
            _messages.append({
                "user": l[0],
                "message": l[1]
            })

    return _messages

def get_main_page(template_name="index.html", user=None,
                  new_msg=None, starting=0):
    """Devuelve la página del chat"""
    while True:
        try:
            with open(CHATFILE, "a+") as f:
                # Bloquear archivo
                if new_msg and user:
                    f.write(f"{user}: {new_msg}\n")

                f.seek(0)
                fcntl.flock(f, fcntl.LOCK_EX | fcntl.LOCK_NB)

                messages = get_messages_from_lines(
                    f.readlines(), starting=starting)

                # Liberar archivo
                fcntl.flock(f, fcntl.LOCK_UN)
                return get_template(template_name).render(
                    user=user, users=get_all_users(), messages=messages)
        except BlockingIOError as e:
            log(f"BLOQUEADO [{e}]")
            time.sleep(0.1)

def get_user_or_create(username):
    """Devuelve el usuario registrado en el archivo o lo crea si no existe"""
    while True:
        try:
            with open(USERSFILE, "a+") as f:
                fcntl.flock(f, fcntl.LOCK_EX | fcntl.LOCK_NB)
                f.seek(0)
                for user in f.readlines():
                    user = user.replace("\n", "").replace("\r", "")
                    if user == username:
                        fcntl.flock(f, fcntl.LOCK_UN)
                        return user

                f.write(f"{username}\n")
                fcntl.flock(f, fcntl.LOCK_UN)
                return username
        except BlockingIOError as e:
            log(f"BLOQUEADO [{e}]")
            time.sleep(0.1)

def get_all_users():
    """Devuelve todos los usuarios registrados en el archivo"""
    users = []
    while True:
        try:
            with open(USERSFILE, "a+") as f:
                fcntl.flock(f, fcntl.LOCK_EX | fcntl.LOCK_NB)
                f.seek(0)
                for user in f.readlines():
                    user = user.replace("\n", "").replace("\r", "")
                    users.append(user)

                fcntl.flock(f, fcntl.LOCK_UN)
                return users
        except BlockingIOError as e:
            log(f"BLOQUEADO [{e}]")
            time.sleep(0.1)


def get():
    user = is_authenticated(COOKIE_NAME)
    form = cgi.FieldStorage()
    if user:
        starting = 0
        if form.getvalue("logout"):
            # Logout
            logout_cookie = logout(COOKIE_NAME)
            print(logout_cookie)
            return get_template("login.html").render()
        if form.getvalue("starting"):
            starting = int(form.getvalue("starting"))
        # Devolver el chat con los mensajes y los usuarios
        return get_main_page(user=user, starting=starting)
    else:
        # Mostrar pantalla de login
        return get_template("login.html").render()
        pass

def post():
    user = is_authenticated(COOKIE_NAME)
    form = cgi.FieldStorage()
    if user:
        # Recuperar el mensaje del form y guardarlo en el archivo
        message = form.getvalue("message")
        return get_main_page(user=user, new_msg=message)
    else:
        # Loguear
        username = form.getvalue("username")
        user = get_user_or_create(username)
        cookie = login(user, COOKIE_NAME)
        print(cookie)
        return get_main_page(user=user)

def main():
    req_method = os.getenv("REQUEST_METHOD")
    if req_method == "GET":
        res = get()
        print_headers()
        print(res)
    elif req_method == "POST":
        res = post()
        print_headers()
        print(res)

if __name__ == "__main__":
    main()
