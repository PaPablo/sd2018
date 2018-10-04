package cliente;

import utils.Timer;
import utils.Logger;
import exceptions.ClienteException;
import gui.VentanaReloj;

public class MainCliente {

    public static final int MAX_REQUESTS_FOR_SYNC = 5;

    public static void main(String[] args) {

        String host = "localhost";
        int requestsForSync = MAX_REQUESTS_FOR_SYNC;

        for (int i = 0; i < args.length; i++) {
            try {
                if (args[i].equals("--help")) {
                    printHelp();
                    System.exit(0);
                } 
                if (args[i].equals("-h") || args[i].equals("--host")) {
                    i++;
                    host = args[i];
                    continue;
                } 
                if (args[i].equals("-d") || args[i].equals("--debug")) {
                    System.out.println("*** DEBUG MODE ***");
                    GlobalState.addEnv("debug");
                    continue;
                } 
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Invocación incorrecta - faltan parámetros");
                printHelp();
                System.exit(1);
            }
        }

        Cliente cliente = null;
        try {
            cliente = new Cliente(host);
        } catch (ClienteException e) {
            System.out.println(String.format(
                        "No se pudo crear el cliente [%s]",
                        e));
            System.exit(1);
        }
        System.out.println(String.format(
                    "Cliente conectado con el host [%s]",
                    host));

        VentanaReloj ventana = new VentanaReloj();
        ventana.setVisible(true);

        ActualizadorReloj actualizadorReloj = new ActualizadorReloj(cliente);
        actualizadorReloj.addListener(ventana);
        actualizadorReloj.addListener(new Logger());

        ActualizadorDeriva actualizadorDeriva = new ActualizadorDeriva(
                cliente, requestsForSync);

        actualizadorReloj.start();
        actualizadorDeriva.start();
    }

    public static void printHelp() {
        System.out.println("MainCliente [-h|--host] [HOST] [-d|--debug]");
    }
}
