#!/usr/bin/env python3

import fcntl
import cgi
import csv
import os
import time
from uuid import uuid4


from utils.utils import get_template, print_headers
from utils.log import log
from session.session import get_cookie_value
from session.login import login, logout


CHATFILE = "data/chat.txt"
SESSIONFILE = "data/session.txt"
USERSFILE = "data/users.txt"
COOKIE_NAME = "SD-CGI-CHAT"

def _get_last_line(filename):
    """Devuelve la última línea de un archivo"""
    with open(filename) as f:
        # Bloquear archivo
        fcntl.flock(f, fcntl.LOCK_EX | fcntl.LOCK_NB)
        lines = f.read().splitlines()
        # Liberar archivo
        fcntl.flock(f, fcntl.LOCK_UN)
        return {
            "count": len(lines)-1,
            "line": lines[-1]
        }

def get_session(session_id):
    """Devuelve la sesión de id `session_id`"""
    while True:
        try:
            with open(SESSIONFILE) as f:
                reader = csv.DictReader(f)
                for r in reader:
                    if r["id"] == session_id:
                        return {
                            "id": r["id"],
                            "user": r["user"],
                            "last_line": r["last_line"]
                        }
                return None
        except BlockingIOError as e:
            time.sleep(0.1)

def create_session(username):
    """Crea una sesión para el usuario `username`"""
    _file_exists = os.path.exists(SESSIONFILE)
    while True:
        try:
            with open(SESSIONFILE, "a+") as f:
                # Bloquear archivo
                fcntl.flock(f, fcntl.LOCK_EX | fcntl.LOCK_NB)
                writer = csv.DictWriter(f, fieldnames=["id","user","last_line"])
                if not _file_exists:
                    writer.writeheader()
                f.seek(0)
                reader = csv.DictReader(f)
                for row in reader:
                    if row["user"] == username:
                        fcntl.flock(f, fcntl.LOCK_UN)
                        return None
                _session = {
                    "id": str(uuid4()),
                    "user": username,
                    "last_line": _get_last_line(CHATFILE)["count"]
                }
                writer.writerow(_session)
                # Liberar archivo
                fcntl.flock(f, fcntl.LOCK_UN)
                return _session
        except BlockingIOError as e:
            time.sleep(0.1)


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

def get_messages(starting):
    """Devuelve los mensajes en la forma {user, message}"""
    while True:
        try:
            with open(CHATFILE) as f:
                # Bloquear archivo
                fcntl.flock(f, fcntl.LOCK_EX | fcntl.LOCK_NB)
                _messages = []
                for i, l in enumerate(f.readlines()):
                    if i >= starting:
                        l = l.replace("\n", "").replace("\r", "").split(":")
                        l = [line.strip() for line in l]
                        _messages.append({
                            "user": l[0],
                            "message": l[1]
                        })
                # Liberar archivo
                fcntl.flock(f, fcntl.LOCK_UN)
                return _messages
        except BlockingIOError as e:
            time.sleep(0.1)
        except IndexError as e:
            return []

def add_message(user=None, msg=None):
    """Agrega un nuevo mensaje al chat"""
    if not user and not msg:
        return
    while True:
        try:
            with open(CHATFILE, "a+") as f:
                # Bloquear archivo
                fcntl.flock(f, fcntl.LOCK_EX | fcntl.LOCK_NB)

                f.write(f"{user}: {msg}")

                # Liberar archivo
                fcntl.flock(f, fcntl.LOCK_UN)

        except BlockingIOError as e:
            time.sleep(0.1)

def get_main_page(template_name="index.html", user=None,
                  new_msg=None, starting=0):
    """Devuelve la página del chat"""
    while True:
        try:
            with open(CHATFILE, "a+") as f:
                # Bloquear archivo
                fcntl.flock(f, fcntl.LOCK_EX | fcntl.LOCK_NB)

                if new_msg and user:
                    f.write(f"{user}: {new_msg}\n")

                f.seek(0)

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
    session_id = get_cookie_value(COOKIE_NAME)
    session = get_session(session_id)
    form = cgi.FieldStorage()
    log(f"session_id => {session_id}")
    log(f"session => {session}")
    log(f"form => {form}")
    if session:
        user = session["user"]
        log(f"get() - LOGUEADO => {user}")
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
        log(f"get() - NO LOGUEADO")
        return get_template("login.html").render()

def post():
    session_id = get_cookie_value(COOKIE_NAME)
    session = get_session(session_id)
    form = cgi.FieldStorage()
    if session:
        # Recuperar el mensaje del form y guardarlo en el archivo
        user = session["user"]
        log(f"post() - LOGUEADO => {user}")
        message = form.getvalue("message")
        return get_main_page(user=user, new_msg=message)
    else:
        # Loguear
        log(f"post() - NO LOGUEADO")
        username = form.getvalue("username")
        # user = get_user_or_create(username)
        session = create_session(username)
        if not session:
            return get_template("login.html").render(errors={
                "nickname": f"El nickname '{username}' ya está siendo usado"
            })
        log(f"session => {session}")
        cookie = login(session["id"], COOKIE_NAME)
        log(f"cookie => {cookie}")
        print(cookie)
        return get_main_page(user=session["user"])

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
