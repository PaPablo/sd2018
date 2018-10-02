package cliente;

import utils.Timer;
import utils.Logger;
import exceptions.ClienteException;
import gui.VentanaReloj;

public class MainCliente {
    public static void main(String[] args) {

        VentanaReloj ventana = new VentanaReloj();
        ventana.setVisible(true);
        String host = "localhost";
        int requestsForSync = 5;
        for (int i = 0; i < args.length; i++) {
            try {
                if (args[i].equals("-h") || args[i].equals("--host")) {
                    i++;
                    host = args[i];
                } 
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Invocación incorrecta - faltan parámetros");
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

        ActualizadorReloj actualizadorReloj = new ActualizadorReloj(cliente);
        actualizadorReloj.addListener(ventana);
        actualizadorReloj.addListener(new Logger());

        ActualizadorDeriva actualizadorDeriva = new ActualizadorDeriva(
                cliente, requestsForSync);

        actualizadorReloj.start();
        actualizadorDeriva.start();
        //
        //Timer timer = new Timer();
        //long localClock, patternClock = 0;
        //for (int i = 0; i < 10; i++) {
            //try {
                //timer.start();
                //localClock = System.currentTimeMillis();
                //patternClock = cliente.getPatternTime();
                //System.out.println(String.format(
                            //"Reloj local: %d\nReloj del servidor: %d\nDeriva: %d\nRTT: %d",
                            //localClock,
                            //patternClock, 
                            //(localClock-patternClock),
                            //timer.getPartial()/2
                            //));
                //ventana.updateText(cliente.getTimeFromMillis(localClock));
                //Thread.sleep(1000);
            //} catch (InterruptedException e) {

            //} catch (ClienteException e) {
                //System.out.println(String.format(
                            //"No se pudo realizar la operación [%s]",
                            //e));
                //System.exit(1);
            //}
        //}
    }
}