def print_headers():
    """Imprime headers HTTP para HTML"""
    print("Content-Type: text/html")
    print()

def get_template(template_name, templates_dir="./templates"):
    """Devuelve un objeto Template de Jinja"""
    import jinja2
    loader = jinja2.FileSystemLoader(templates_dir)
    ctx = jinja2.Environment(loader=loader)
    return ctx.get_template(template_name)

def _get_last_line(filename):
    """Devuelve la última línea de un archivo"""
    import fcntl
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

def file_is_empty(filename):
    import os
    try:
        return os.path.getsize(filename) <= 0
    except OSError as e:
        return True
