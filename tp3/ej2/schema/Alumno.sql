CREATE TABLE IF NOT EXISTS Alumno (
    legajo integer PRIMARY KEY CHECK(legajo > 0 AND legajo <= 99999999),
    nombre varchar(70),
    sexo varchar(1) CHECK(sexo == "h" OR sexo == "m"),
    edad integer CHECK(edad > 0 AND edad <= 99),
    password varchar(64)
);
