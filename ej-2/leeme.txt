Este conjunto fué editado, compilado y ejecutado exitosamente en linux kubuntu 9.04


Para compilar,

cliente: gcc client.c clstub.c -o client
servidor: gcc server.c svstub.c -o server

Para ejecutar
1. Ejecutar el server en una consola (sin argumentos)
2. Ejecutar el cliente en otra consola (por defecto se usa localhost:8888)
3. Si la conexión cliente-servidor es exitosa ambos procesos se otorgan una bienvenida.
4. Requerir la acción: leer o escribir y fijar los parámetros solicitados.
5. La lectura, lee del servidor el archivo especificado a un destino prefijado en disco local (alfa.txt)
6. La escritura, escribe en el servidor el archivo especificado desde un origen prefijado en disco local (beta.txt).
   Si el archivo no existe, se crea. Si existe, se le agregan los nuevos bytes.

R.López 22/05/09 