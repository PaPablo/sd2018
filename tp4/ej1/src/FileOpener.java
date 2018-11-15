package jfs;
import jade.core.*;
import java.io.*;
import jade.core.behaviours.CyclicBehaviour;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileOpener extends Agent
{
    String strdir = "/tmp";                // Dir to list
    String[] list;
    ContainerID destino = null;
    Location origen = null;
    public void setup()
    {
        Object[] arguments = getArguments();
        for (int i = 0; i < arguments.length; i++) {
            System.out.println(arguments[i].toString());
        }

        System.out.println("Se crea al agente --> " + getName());
        // inicializa origen y destino
        destino = new ContainerID("Container-1", null);
        //destino = new ContainerID("Analia", null);
        System.out.println("Destino --> " + destino.getID());
        origen = here();
        System.out.println("Origen --> " + origen.getID());
        // registra el comportamiento deseado del agente
        addBehaviour(new CyclicBehaviour(this){
            public void action() {
                switch(_state){
                    case 0:
                        // Comienza la migración del agente al destino
                        _state++;
                        System.out.println("Estado 0 Comienza la migración del agente al destino --> " + destino.getID());
                        try {
                            doMove(destino);
                            System.out.println("Despues de doMove en CyclicBehaviour de Estado 0 --> " + destino.getID());
                        } catch (Exception e) {
                            System.out.println("fallo al moverse al Container destino");
                            e.getMessage();
                        }
                        break;
                    case 1:
                        // el agente llegó al destino, recupera el directorio y regresa
                        _state++;
                        System.out.println("Estado 1 agente llegó a destino, recupera directorio y regresa a --> " + origen.getID());
                        try {
                            File dir = new File(strdir);
                            list = dir.list();
                            System.out.println("recuperó el directorio en " + here().getID());
                            for (int i = 0; i < list.length; i++)
                                System.out.println(i + ": " + list[i]);
                        } catch (Exception e) {
                            System.out.println("Falló al recuperar directorio dir.list()");
                            e.getMessage();
                        }
                        // regresa al origen e imprime el directorio
                        try {
                            System.out.println("regresando a --> " + origen.getID());
                            doMove(origen);
                            System.out.println("despues de domove en CyclicBehaviour estado 1 --> " + here().getID());
                        } catch (Exception e) {
                            System.out.println("Falla al mover al regresar al origen");
                            e.getMessage();
                        }
                        break;
                    case 2:
                        // Regresó al origen, imprime el directorio y destruye al agente
                        System.out.println("Estado 2 Regresó a origen, imprime directorio y destruye agente --> " + here().getID());
                        for (int i = 0; i < list.length; i++)
                            System.out.println(i + ": " + list[i]);
                        // destruye al agente

                        System.out.println("destruye al agente --> " + getName());
                        doDelete();
                        break;
                    default:
                        myAgent.doDelete();
                }
            }
            private int _state = 0; // variable de máquina de estados del agente
        });

        // registra un comportamiento dummy a los efectos de verificar concurrencia y movilidad
        addBehaviour(new CyclicBehaviour(this){

            public void action()
            {
                // arranca muestra cartel, duerme 5 segundos y muestra otro cartel
                _contador++;
                System.out.println("Behaviour dummy antes de dormir ciclo--> " + _contador + " --> " + here().getID());
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ListingAgent.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.out.println("Behaviour dummy despues de dormir ciclo--> " + _contador + " --> " + here().getID());
            }

            private int _contador = 0; // cuenta la cantidad de ciclos en que se ejecuta el comportamiento

        });
    }

    // Luego de ser movido el agente ejecuta este código
    protected void afterMove() {
        System.out.println("Siempre ejecuta afterMove cuando al llegar --> " + here().getID());
    }
}


