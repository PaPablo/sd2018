## Cómo correr la aplicación

- Primero levantar la instancia de JADE

```
java -cp lib/jade.jar jade.Boot -gui -local-host <IP>
```

- Crear algún otro contenedor

```
java -cp lib/jade.jar jade.Boot -container -local-host <IP> [-container-name <NOMBRE>]
```

- Ejecutar agente

```
make
java -cp lib/jade.jar:bin/ jade.Boot -container -local-host <IP> [-container-name <NOMBRE>] display:examples.DisplayContainers.DisplayContainers
```
