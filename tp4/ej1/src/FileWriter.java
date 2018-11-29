import jade.core.*;
import java.io.*;
import jade.core.behaviours.CyclicBehaviour;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileWriter extends Agent
{
    private final int MAX_BUFFER = 256;

    String[] list;
    ContainerID destino = null;
    Location origen = null;
    FileInputStream in = null;
    FileOutputStream out = null;

    byte[] buffer = new byte[MAX_BUFFER];
    int length;

    public void setup()
    {
        Object[] arguments = getArguments();

        // Info necesaria
        String src_filename = (String) arguments[0];
        String[] _split = src_filename.split("/");
        String dst_filename = (String) arguments[1];
        String address;


        System.out.println("Se crea al agente --> " + getName());
        // inicializa origen y destino
        destino = new ContainerID("Container-1", null);

        if (arguments.length > 3) {
            address = (String)arguments[2];
            destino.setAddress(address);
        }

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
                            if (in == null){
                                in = new FileInputStream(src_filename);
                            }

                            System.out.println("Estado 1 agente llegó a destino, lee de archivo y regresa a --> " + origen.getID());

                            // Leemos del archivo
                            for (int i = 0; i < buffer.length; i++) 
                                buffer[i] = 0;

                            length = in.read(buffer);

                            System.out.println("Leidos " + length + " bytes.");
                            
                        } catch(Exception e){
                            e.printStackTrace();
                        }

                        try {
                            doMove(destino);
                            System.out.println("Despues de doMove en CyclicBehaviour de Estado 0 --> " + destino.getID());
                        } catch (Exception e) {
                            System.out.println("fallo al moverse al Container destino");
                            e.getMessage();
                        }
                        break;
                    case 1:
                        if (length > 0) {
                            try {
                                if (out == null){
                                    out = new FileOutputStream(dst_filename);
                                }
                                out.write(buffer);
                                System.out.println("Escritos " + length + " bytes.");
                            } catch(Exception e){
                                e.printStackTrace();
                            }

                            try {
                                doMove(destino);
                            } catch(Exception e){
                                e.printStackTrace();
                            }
                            _state--;
                        }
                        else {
                            System.out.println("No tengo más para leer");
                            _state++;
                        }


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
                        try {
                            in.close();
                            out.close();
                        } catch(Exception e){
                            e.printStackTrace();
                        }
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
                //try {
                    //Thread.sleep(5000);
                //} catch (InterruptedException ex) {
                    //Logger.getLogger(FileReader.class.getName()).log(Level.SEVERE, null, ex);
                //}
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


