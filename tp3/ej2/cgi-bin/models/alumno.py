FIELD_CONSTRAINTS = {
    "max_nombre": 70,
    "max_legajo": 8,
    "max_edad": 2,
}

class Alumno(object):

    """Clase para representar el alumno"""

    def __init__(self, legajo, nombre, sexo, edad, password):
        self.legajo = legajo
        self.nombre = nombre
        self.sexo = sexo
        self.edad = edad
        self.password = password

    def __repr__(self):
        return f"Alumno(\
{self.legajo}, {self.nombre}, {self.sexo}, {self.edad}, {self.password})"

    @classmethod
    def from_row(cls, row):
        """Devuelve un Alumno a partir de una tupla"""
        try:
            return cls(*row)
        except TypeError as e:
            return None

    @classmethod
    def from_dict(cls, dikt):
        try:
            return cls(
                dikt["legajo"],
                dikt["nombre"],
                dikt["sexo"],
                dikt["edad"],
                dikt["password"],
            )
        except KeyError as e:
            return None
