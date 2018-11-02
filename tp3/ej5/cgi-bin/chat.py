#!/usr/bin/env python3

import fcntl
import cgi
import csv
import os
import time
import shutil
import subprocess
from uuid import uuid4
from tempfile import NamedTemporaryFile


from utils.utils import get_template, print_headers
from utils.log import log
from session.session import get_cookie_value
from session.login import login, logout


CHATFILE = "data/chat.txt"
SESSIONFILE = "data/session.csv"
USERSFILE = "data/users.txt"
COOKIE_NAME = "SD-CGI-CHAT"

def _get_last_line(filename):
    """Devuelve la última línea de un archivo"""
    try:
        with open(filename) as f:
            # Bloquear archivo
            fcntl.flock(f, fcntl.LOCK_EX | fcntl.LOCK_NB)
            lines = f.read().splitlines()
            # Liberar archivo
            fcntl.flock(f, fcntl.LOCK_UN)
            return {
                "count": len(lines),
                "line": lines[-1]
            }
    except FileNotFoundError as e:
        return {
            "count": 0,
            "line": None
        }

def _get_session_fields():
    """Devuelve los campos del archivo de sesión"""
    return ["id", "user", "last_line"]

def file_is_empty(filename):
    try:
        return os.path.getsize(filename) <= 0
    except OSError as e:
        return True

def get_session(session_id):
    """Devuelve la sesión de id `session_id`"""
    while True:
        try:
            _id, last_line = session_id.split("|")
            with open(SESSIONFILE) as f:
                reader = csv.DictReader(f)
                for r in reader:
                    if r["id"] == _id:
                        return {
                            "id": r["id"],
                            "user": r["user"],
                            "last_line": last_line
                        }
                return None
        except BlockingIOError as e:
            time.sleep(0.1)
        except (FileNotFoundError,
                KeyError,
                ValueError,
                AttributeError) as e:
            return None

def create_session(username):
    """Crea una sesión para el usuario `username`"""
    _file_exists = os.path.exists(SESSIONFILE)
    _session = {
        "id": str(uuid4()),
        "user": username,
        "last_line": _get_last_line(CHATFILE)["count"]
    }
    while True:
        try:
            with open(SESSIONFILE) as f:
                # Bloquear archivo
                fcntl.flock(f, fcntl.LOCK_EX | fcntl.LOCK_NB)
                reader = csv.DictReader(f)
                for row in reader:
                    if row["user"] == username:
                        # El usuario ya inició sesión
                        fcntl.flock(f, fcntl.LOCK_UN)
                        return None
                # Liberar archivo
                fcntl.flock(f, fcntl.LOCK_UN)

            return add_session(_session)
        except BlockingIOError as e:
            time.sleep(0.1)
        except FileNotFoundError as e:
            return add_session(_session)

def add_session(session):
    """Escribe una nueva sesión"""
    _file_exists = os.path.exists(SESSIONFILE)
    while True:
        try:
            with open(SESSIONFILE, "a+") as f:
                # Bloquear archivo
                fcntl.flock(f, fcntl.LOCK_EX | fcntl.LOCK_NB)
                writer = csv.DictWriter(f, fieldnames=_get_session_fields())
                if not _file_exists or file_is_empty(SESSIONFILE):
                    writer.writeheader()
                writer.writerow(session)
                # Liberar archivo
                fcntl.flock(f, fcntl.LOCK_UN)
            return session
        except BlockingIOError as e:
            time.sleep(0.1)

def delete_session(session):
    """Elimina una sesión"""
    while True:
        try:
            _tmp = NamedTemporaryFile(mode="w", delete=False)
            with open(SESSIONFILE) as f, _tmp:
                reader = csv.DictReader(f, fieldnames=_get_session_fields())
                writer = csv.DictWriter(_tmp, fieldnames=_get_session_fields())
                writer.writeheader()
                next(reader)
                for r in reader:
                    if r["id"] == session["id"]:
                        continue
                    writer.writerow(r)
            shutil.move(_tmp.name, SESSIONFILE)
            subprocess.call(f"chmod 644 {SESSIONFILE}".split())
            break
        except BlockingIOError as e:
            time.sleep(0.1)

def get_messages(starting=0):
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
        except (IndexError, FileNotFoundError) as e:
            return []

def add_message(user=None, msg=None):
    """Agrega un nuevo mensaje al chat"""
    if user is None or msg is None:
        return
    while True:
        try:
            with open(CHATFILE, "a+") as f:
                # Bloquear archivo
                fcntl.flock(f, fcntl.LOCK_EX | fcntl.LOCK_NB)

                f.write(f"{user}: {msg}\n")

                # Liberar archivo
                fcntl.flock(f, fcntl.LOCK_UN)
                break

        except BlockingIOError as e:
            time.sleep(0.1)


def get_active_users():
    """Devuelve los usuarios conectados"""
    while True:
        try:
            with open(SESSIONFILE) as f:
                # Bloquear archivo
                fcntl.flock(f, fcntl.LOCK_EX | fcntl.LOCK_NB)
                reader = csv.DictReader(f, fieldnames=_get_session_fields())
                next(reader)
                users = []
                for r in reader:
                    users.append(r["user"])
                # Liberar archivo
                fcntl.flock(f, fcntl.LOCK_UN)
                return users

        except BlockingIOError as e:
            time.sleep(0.1)
# def get_messages_from_lines(lines, starting=0):
    # """Devuelve una lista de los mensajes con el usuario que lo escribió"""
    # _messages = []
    # for i, l in enumerate(lines):
        # if i >= starting:
            # l = l.replace("\n", "").replace("\r", "").split(":")
            # l = [line.strip() for line in l]
            # _messages.append({
                # "user": l[0],
                # "message": l[1]
            # })

    # return _messages

# def get_main_page(template_name="index.html", user=None,
                  # new_msg=None, starting=0):
    # """Devuelve la página del chat"""
    # while True:
        # try:
            # with open(CHATFILE, "a+") as f:
                # # Bloquear archivo
                # fcntl.flock(f, fcntl.LOCK_EX | fcntl.LOCK_NB)

                # if new_msg and user:
                    # f.write(f"{user}: {new_msg}\n")

                # f.seek(0)

                # messages = get_messages_from_lines(
                    # f.readlines(), starting=starting)

                # # Liberar archivo
                # fcntl.flock(f, fcntl.LOCK_UN)
                # return get_template(template_name).render(
                    # user=user, users=get_all_users(), messages=messages)
        # except BlockingIOError as e:
            # log(f"BLOQUEADO [{e}]")
            # time.sleep(0.1)

# def get_user_or_create(username):
    # """Devuelve el usuario registrado en el archivo o lo crea si no existe"""
    # while True:
        # try:
            # with open(USERSFILE, "a+") as f:
                # fcntl.flock(f, fcntl.LOCK_EX | fcntl.LOCK_NB)
                # f.seek(0)
                # for user in f.readlines():
                    # user = user.replace("\n", "").replace("\r", "")
                    # if user == username:
                        # fcntl.flock(f, fcntl.LOCK_UN)
                        # return user

                # f.write(f"{username}\n")
                # fcntl.flock(f, fcntl.LOCK_UN)
                # return username
        # except BlockingIOError as e:
            # log(f"BLOQUEADO [{e}]")
            # time.sleep(0.1)

# def get_all_users():
    # """Devuelve todos los usuarios registrados en el archivo"""
    # users = []
    # while True:
        # try:
            # with open(USERSFILE, "a+") as f:
                # fcntl.flock(f, fcntl.LOCK_EX | fcntl.LOCK_NB)
                # f.seek(0)
                # for user in f.readlines():
                    # user = user.replace("\n", "").replace("\r", "")
                    # users.append(user)

                # fcntl.flock(f, fcntl.LOCK_UN)
                # return users
        # except BlockingIOError as e:
            # log(f"BLOQUEADO [{e}]")
            # time.sleep(0.1)


def get():
    session_id = get_cookie_value(COOKIE_NAME)
    session = get_session(session_id)
    form = cgi.FieldStorage()
    if session:
        user = session["user"]
        log(f"get() - LOGUEADO => {user}")
        starting = 0
        if form.getvalue("logout"):
            # Logout
            delete_session(session)
            logout_cookie = logout(COOKIE_NAME)
            print(logout_cookie)
            return get_template("login.html").render()
        if form.getvalue("starting"):
            starting = int(form.getvalue("starting"))

        # El cliente envía la cantidad de mensajes que ya tiene
        # La sesión se tiene que actualizar haciendo
        # session["last_line"] += starting
        # Y devolverle al cliente los mensajes que no vio todavia
        # que no sé cómo sacarlos, je

        delete_session(session)
        session["last_line"] = str(
            int(session["last_line"]) + starting
        )
        add_session(session)

        messages = get_messages(int(session["last_line"]))
        # Devolver el chat con los mensajes y los usuarios
        return get_template("index.html").render(
            user=user,
            messages=messages,
            users=get_active_users()
        )
    else:
        # Mostrar pantalla de login
        log(f"get() - NO LOGUEADO")
        logout_cookie = logout(COOKIE_NAME)
        print(logout_cookie)
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
        add_message(user, message)
        return get_template("index.html").render(
            user=user,
            messages=get_messages(),
            users=get_active_users()
        )
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
        cookie = login(f"{session['id']}|{session['last_line']}", COOKIE_NAME)
        print(cookie)
        messages = get_messages(session["last_line"])
        return get_template("index.html").render(
            user=session["user"],
            messages=messages,
            users=get_active_users()
        )

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
