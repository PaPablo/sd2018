1. Compilar con `make`
2. Levantar RMIregistry con `rmiregistry -J--class-path=bin/`
3. Ejecutar servidor con `java -cp bin/ rfs.MainServidor`
4. Ejecutar cliente con `java -cp bin/ rfs.MainCliente`
5. Limpiar `bin/` con `make clean`

> Los pasos 2-4 también se pueden correr sin las banderas del `CLASSPATH` (-cp), pero estando dentro de la carpeta bin/
