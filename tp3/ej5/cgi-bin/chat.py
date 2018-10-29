#!/usr/bin/env python3

import fcntl
import cgi
import os


from utils.utils import get_template, print_headers
from session.session import is_authenticated
from session.login import login, logout


CHATFILE = "data/chat.txt"
USERSFILE = "data/users.txt"
COOKIE_NAME = "SD-CGI-CHAT"


def get_main_page(template_name="index.html", user=None, new_msg=None):
    """Devuelve la página del chat"""
    messages = []
    with open(CHATFILE, "a+") as f:
        # Bloquear archivo
        if new_msg and user:
            f.write(f"{user}: {new_msg}\n")
        f.seek(0)
        fcntl.flock(f, fcntl.LOCK_EX | fcntl.LOCK_NB)
        for l in f.readlines():
            l = l.replace("\n", "").replace("\r", "").split(":")
            l = [line.strip() for line in l]
            messages.append({
                "user": l[0],
                "message": l[1]
            })
        # Liberar archivo
        fcntl.flock(f, fcntl.LOCK_UN)
    return get_template(template_name).render(
        user=user, users=get_all_users(), messages=messages)

def get_user_or_create(username):
    """Devuelve el usuario registrado en el archivo o lo crea si no existe"""
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

def get_all_users():
    """Devuelve todos los usuarios registrados en el archivo"""
    users = []
    with open(USERSFILE, "a+") as f:
        fcntl.flock(f, fcntl.LOCK_EX | fcntl.LOCK_NB)
        f.seek(0)
        for user in f.readlines():
            user = user.replace("\n", "").replace("\r", "")
            users.append(user)

        fcntl.flock(f, fcntl.LOCK_UN)
        return users


def get():
    user = is_authenticated(COOKIE_NAME)
    form = cgi.FieldStorage()
    if user:
        if form.getvalue("logout"):
            # Logout
            logout_cookie = logout(COOKIE_NAME)
            print(logout_cookie)
            return get_template("login.html").render()
        # Devolver el chat con los mensajes y los usuarios
        return get_main_page(user=user)
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
