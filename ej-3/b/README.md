## Implementación de RFS en Java

### 20/8/2018

Lo que se me ocurre para implementar el RFS en Java es diseñar un pequeño protocolo de capa de aplicación, con dos comandos:

- `ESCRIBIR <archivo>`
- `LEER <archivo>`

Ahora bien, la onda que se me ocurre sería la siguiente. El servidor sería un servidor **sin estado**. Una transacción (la lectura de un archivo remoto, o la escritura de un archivo en el servidor remoto) se resuelve en una única conexión TCP, el servidor no mantienen ninguna información de los clientes 

- En el caso de la escritura, el cliente le indica el comando `ESCRIBIR` y el nombre del archivo que deberá escribir el servidor. Si está todo OK (el servidor le contestó con un `OK`), el cliente ahora le manda el contenido del archivo al servidor. En caso de que el mensaje con el comando no sea correcto, el servidor contestará con un `NO OK`.
- En el caso de la lectura, el cliente le indica el comando `LEER` y el nombre del archivo que desea leer (tiene que ser un archivo que tenga disponible el servidor, no). Si está todo OK, el servidor le envía el contenido del archivo solicitado. En caso de que el mensaje con el comando no sea correcto o el archivo indicado no exista en el servidor, el servidor contestará con un `NO OK`.

Entonces, cada transacción cliente-servidor se resuelve en **tres pasos**

1. El cliente se conecta, e indica el comando y archivo
2. El servidor contesta `OK` o `NO OK`
3a. Si el cliente quiere escribir, entonces le manda el contenido del archivo y el servidor lo escribe
3a. Si el cliente quiere leer, entonces el servidor le manda el contenido del archivo deseado.

---

En la carpeta `src/` hay un paquete `gui`, y otro `rfs`

- `gui` por lo pronto contiene un archivo que define la GUI para el Cliente. Es una ventanita con dos botones, uno para leer archivos y otro para escribir
- `rfs` tiene las clases que implementan la lógica y las clases con los `main`. Hay unas clases bases como `ServidorTCP` y `ClienteTCP` que tienen la funcionalidad básica para establecer una conexión TCP.
