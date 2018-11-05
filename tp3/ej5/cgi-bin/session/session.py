from os import environ
from datetime import datetime, timedelta
from http.cookies import SimpleCookie

# Sacado de acá:
# https://recursospython.com/codigos-de-fuente/cookies-autenticacion-cgi/

def _build_cookie(name, username, expiration_days):
    """
    Build a SimpleCookie object and returns the HTTP
    Set-Cookie header.
    """
    cookie = SimpleCookie()

    # Username and Password
    cookie[name] = str(username)

    # Expiration.
    expires = datetime.now() + timedelta(days=expiration_days)

    # Morsel objects
    cookie[name]["path"] = "/"
    cookie[name]["expires"] = expires.strftime("%a, %d-%b-%Y "
                                               "%H:%M:%S PST")

    # Return HTTP header (Set-Cookie).
    return cookie


def get_auth_cookies(name):
    """
    Return a username if there are
    available cookies. None otherwise.
    """
    # Check if there's any cookie available.
    if "HTTP_COOKIE" in environ:
        cookie = SimpleCookie(environ["HTTP_COOKIE"])
        # Look for our cookie
        if name in cookie:
            # Maybe it's empty
            if cookie[name].value:
                return cookie[name].value
    return None


def get_cookie_value(name):
    """
    Returns a dict if the HTTP cookies are set. None otherwise.
    """
    try:
        # Retrieve cookies data
        return get_auth_cookies(name)
    except TypeError:
        # No cookies available
        return None


def remove_auth_cookie(name):
    """
    Remove a HTTP auth cookie.
    """
    # -1 removes the cookie
    return _build_cookie(name, "", -1)


def set_auth_cookie(name, username, expiration_time=7):
    """
    Set a HTTP auth cookie.
    """
    return _build_cookie(name, username, expiration_time)
