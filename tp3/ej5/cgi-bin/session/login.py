from session.session import set_auth_cookie, remove_auth_cookie

def login(user, name="COOKIE_NAME"):
    """Genera la cookie de login"""
    return set_auth_cookie(name, user)

def logout(name="COOKIE_NAME"):
    """Genera la cookie de loguout (cookie expirada)"""
    return remove_auth_cookie(name)
