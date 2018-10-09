CREATE TABLE cuentas(
    id integer PRIMARY KEY, 
    titular character(30) NOT NULL,
    fecha_creacion timestamp with time zone DEFAULT now(),
	bloqueada boolean, 
    saldo real
);

/*
 * Estos ALTER y GRANT van comentados porque al contenedor no le gusta :/ 
 */
-- create role jtatest LOGIN password 'jtatest';
-- ALTER TABLE public.cuentas OWNER TO postgres;
-- GRANT SELECT,INSERT ON TABLE cuentas TO jtatest;

INSERT INTO cuentas (id,titular,bloqueada,saldo) 
  VALUES (1, 'Cliente 1',True,0); 
INSERT INTO cuentas (id,titular,bloqueada,saldo) 
  VALUES (2, 'Cliente 2',True,1000);
INSERT INTO cuentas (id,titular,bloqueada,saldo) 
  VALUES (3, 'Cliente 3',False,2000);
INSERT INTO cuentas (id,titular,bloqueada,saldo) 
  VALUES (4, 'Cliente 4',False,3000);
INSERT INTO cuentas (id,titular,bloqueada,saldo) 
  VALUES (5, 'Cliente 5',False,4000); 

SELECT * FROM cuentas;
