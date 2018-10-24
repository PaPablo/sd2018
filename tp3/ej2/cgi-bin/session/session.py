from os import environ
from datetime import datetime, timedelta
from http.cookies import SimpleCookie

# Sacado de acá:
# https://recursospython.com/codigos-de-fuente/cookies-autenticacion-cgi/

def _build_cookie(name, domain, username, password, expiration_days):
    """
    Build a SimpleCookie object and returns the HTTP
    Set-Cookie header.
    """
    cookie = SimpleCookie()

    # Username and Password
    cookie[name] = str(username) + "|" + str(password)

    # Expiration.
    expires = datetime.now() + timedelta(days=expiration_days)

    # Morsel objects
    cookie[name]["domain"] = domain
    cookie[name]["path"] = "/"
    cookie[name]["expires"] = expires.strftime("%a, %d-%b-%Y "
                                               "%H:%M:%S PST")

    # Return HTTP header (Set-Cookie).
    return cookie


def get_auth_cookies(name):
    """
    Return a 2-tuple (username, password) if there are
    available cookies. None otherwise.
    """
    # Check if there's any cookie available.
    if "HTTP_COOKIE" in environ:
        cookie = SimpleCookie(environ["HTTP_COOKIE"])
        # Look for our cookie
        if name in cookie:
            # Maybe it's empty
            if cookie[name].value:
                return tuple(cookie[name].value.split("|"))
    return None


def is_authenticated(name):
    """
    Returns a dict if the HTTP cookies are set. None otherwise.
    """
    try:
        # Retrieve cookies data
        cookie_username, cookie_password = get_auth_cookies(name)
        return {"legajo": cookie_username, "password": cookie_password}
    except TypeError:
        # No cookies available
        return None


def remove_auth_cookie(name, domain):
    """
    Remove a HTTP auth cookie.
    """
    # -1 removes the cookie
    return _build_cookie(name, domain, "", "", -1)


def set_auth_cookie(name, domain, username, password, expiration_time=7):
    """
    Set a HTTP auth cookie.
    """
    return _build_cookie(name, domain, username, password, expiration_time)
