Errores encontrados para versiones de Linux Ubuntu superiores a 9.04
1) 	En el manejo de los sockets:
		Se declaraba un socket global (socket de escucha) que es luego utilizado como socket de atención en las funciones lo cual deriva en un "Bad File Descriptor" (revisar valor de la variable "Errno" mediante la llamada "error();" si socket < 0).

2)	Lógica en los Bucles:
		Tanto en el "rmt_read" como en el "rmt_write", la lógica de los bucles de control obligaba a pedir una cantidad de bytes esecíficos, menor al tamaño total del archivo, de lo contrario, ambos componentes quedan en un bucle infinito ya que la condicion "c_restantes > 0" nunca se vuelve falsa para salir del while.

P. Navarro, R. Huincalef, S. Almonacid	31/08/13



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


