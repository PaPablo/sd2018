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


from utils.utils import \
    get_template, print_headers, _get_last_line, file_is_empty
from utils.log import log
from session.session import get_cookie_value, parse_cookie
from session.login import login, logout


CHATFILE = "data/chat.txt"
SESSIONFILE = "data/session.csv"
USERSFILE = "data/users.txt"
COOKIE_NAME = "SD-CGI-CHAT"

def _get_session_fields():
    """Devuelve los campos del archivo de sesión"""
    return ["id", "user", "last_line"]

def get_session(session_id, session_filename):
    """Devuelve una sesión a partir de un id"""
    while True:
        try:
            _id, last_line = session_id.split("|")
            with open(session_filename) as f:
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

def create_session(username, session_filename):
    """Crea una sesión y la escribe en el archivo"""
    _file_exists = os.path.exists(session_filename)
    _session = {
        "id": str(uuid4()),
        "user": username,
        "last_line": _get_last_line(CHATFILE)["count"]+1
    }
    while True:
        try:
            with open(session_filename) as f:
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

            return add_session(_session, session_filename)
        except BlockingIOError as e:
            time.sleep(0.1)
        except FileNotFoundError as e:
            return add_session(_session, session_filename)

def add_session(session, session_filename):
    """Escribe una nueva sesión en el archivo de sesiones"""
    _file_exists = os.path.exists(session_filename)
    while True:
        try:
            with open(session_filename, "a+") as f:
                # Bloquear archivo
                fcntl.flock(f, fcntl.LOCK_EX | fcntl.LOCK_NB)
                writer = csv.DictWriter(f, fieldnames=_get_session_fields())
                if not _file_exists or file_is_empty(session_filename):
                    writer.writeheader()
                writer.writerow(session)
                # Liberar archivo
                fcntl.flock(f, fcntl.LOCK_UN)
            return session
        except BlockingIOError as e:
            time.sleep(0.1)

def delete_session(session, session_filename):
    """Elimina una sesión del archivo de sesiones"""
    while True:
        try:
            _tmp = NamedTemporaryFile(mode="w", delete=False)
            with open(session_filename) as f, _tmp:
                reader = csv.DictReader(f, fieldnames=_get_session_fields())
                writer = csv.DictWriter(
                    _tmp, fieldnames=_get_session_fields())
                writer.writeheader()
                next(reader)
                for r in reader:
                    if r["id"] == session["id"]:
                        continue
                    writer.writerow(r)
            shutil.move(_tmp.name, session_filename)
            subprocess.call(f"chmod 644 {session_filename}".split())
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
                for l in f.readlines()[starting:]:
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


def get_active_users(session_filename):
    """Devuelve los usuarios conectados"""
    while True:
        try:
            with open(session_filename) as f:
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

def get():
    session_id = get_cookie_value(COOKIE_NAME)
    _id, last_line = parse_cookie(session_id)
    session = get_session(_id, SESSIONFILE)
    form = cgi.FieldStorage()

    if session:
        # El usuario está logueado
        user = session["user"]
        starting = 0
        if form.getvalue("logout"):
            # Se quiere desloguear
            delete_session(session, SESSIONFILE)
            logout_cookie = logout(COOKIE_NAME)
            print(logout_cookie)
            return get_template("login.html").render()
        if form.getvalue("starting"):
            starting = int(form.getvalue("starting"))

        # El cliente envía la cantidad de mensajes que ya tiene
        # La sesión se tiene que actualizar haciendo
        # session["last_line"] += starting
        # Y devolverle al cliente los mensajes que no vio todavia

        # Actualizar cantidad de mensajes que se le enviaron
        delete_session(session, SESSIONFILE)
        session["last_line"] = str(
            int(session["last_line"]) + starting
        )
        add_session(session, SESSIONFILE)

        messages = get_messages(int(session["last_line"]))
        # Devolver el chat con los mensajes y los usuarios
        return get_template("index.html").render(
            user=user,
            messages=messages,
            users=get_active_users(SESSIONFILE)
        )
    else:
        # No está logueado - mostrar pantalla de login
        log(f"get() - NO LOGUEADO")
        logout_cookie = logout(COOKIE_NAME)
        print(logout_cookie)
        return get_template("login.html").render()

def post():
    session_id = get_cookie_value(COOKIE_NAME)
    _id, last_line = parse_cookie(session_id)
    session = get_session(_id, SESSIONFILE)
    form = cgi.FieldStorage()
    if session:
        # El usuario está logueado
        # Recuperar el mensaje del form y guardarlo en el archivo
        user = session["user"]
        message = form.getvalue("message")
        add_message(user, message)
        return get_template("index.html").render(
            user=user,
            messages=get_messages(),
            users=get_active_users(SESSIONFILE)
        )
    else:
        # No está logueado - crear sesión y loguear
        log(f"post() - NO LOGUEADO")
        username = form.getvalue("username")
        session = create_session(username, SESSIONFILE)
        if not session:
            # El username está siendo usado - no se puede loguear
            return get_template("login.html").render(errors={
                "nickname": f"El nickname '{username}' ya está siendo usado"
            })
        cookie = login(
            f"{session['id']}|{session['last_line']}", COOKIE_NAME)
        print(cookie)
        messages = get_messages(session["last_line"])
        return get_template("index.html").render(
            user=session["user"],
            messages=messages,
            users=get_active_users(SESSIONFILE)
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
