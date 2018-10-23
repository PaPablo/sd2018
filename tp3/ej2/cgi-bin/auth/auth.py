def are_credentials_valid(registered_alumno, form):
    """Verifica que las credenciales sean de un alumno registrado"""
    if registered_alumno is None:
        return False

    return registered_alumno.password == form["password"]
