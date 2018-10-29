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
