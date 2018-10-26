from session.session import set_auth_cookie, remove_auth_cookie

COOKIE_NAME = "SD-CGI"
DOMAIN = "localhost"

def login(user, password):
    """Genera la cookie de login"""
    return set_auth_cookie(COOKIE_NAME, DOMAIN, user, password)

def logout():
    """Genera la cookie de loguout (cookie expirada)"""
    return remove_auth_cookie(COOKIE_NAME, DOMAIN)
