def print_headers():
    print("Content-type: text/html")
    print()

def get_template(template_name, templates_dir="./templates"):
    import jinja2
    loader = jinja2.FileSystemLoader(templates_dir)
    ctx = jinja2.Environment(loader=loader)
    return ctx.get_template(template_name)

def get_dict_from_fieldstorage(data):
    _dict = {}
    for field in data:
        _dict[field] = data[field].value

    return _dict
