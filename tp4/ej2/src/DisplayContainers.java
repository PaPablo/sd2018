package examples.DisplayContainers;

import java.util.*;

import jade.core.*;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.*;
import jade.content.*;
import jade.content.onto.basic.*;
import jade.content.lang.sl.*;
import jade.domain.JADEAgentManagement.*;
import jade.domain.mobility.*;


public class DisplayContainers extends Agent {

	
	private ArrayList<Location> containers = new ArrayList<Location>();
    private ArrayList<String> infos = new ArrayList<String>();
	private int cantidad_maxima_contenedores = 0;
	private int cant_contenedores = 0;	
    private long startTime;
	
	Location destino    = null;
	Location origen     = null;	
    

	public void setup() {
		System.out.println("Se crea al agente --> " + getName()+"\n");
				
		// Registramos el lenguaje y ontologia para la movilidad del agente.
		getContentManager().registerLanguage(new SLCodec());
	    getContentManager().registerOntology(MobilityOntology.getInstance());
		
	    origen = here();
        startTime = System.currentTimeMillis();
		System.out.println("Origen --> " + origen.getName()+"\n");		
				
		// registra el comportamiento deseado del agente
		addBehaviour( 
			new CyclicBehaviour(this) {
                // variable de maquina de estados del agente
				private int _state = 0; 

				public void action() {
					if (_state == 0) {												
                            containers = getContainers();
							verContainers(containers);
							cantidad_maxima_contenedores = containers.size();
							cant_contenedores = 0;

							if (cantidad_maxima_contenedores != 0)
								_state = 1;
							else
								_state = 2;  
					}
					switch(_state) {
						case 1:
							// ME MUDO A LA SIGUIENTE MAQUINA   
                            try {
                                destino = containers.get(cant_contenedores);
                                ++cant_contenedores;
                            
                                System.out.println(
                                            String.format(
                                            "Migración desde %s hacia %s", 
                                            here().getName(),
                                            destino.getName()));
                                try {
                                    Thread.sleep(2000);
                                    /*
                                     * ACÁ HACER LAS COSASSS
                                     */

                                    String info = String.format(
                                            "CONTAINER [%s]: %s",
                                            here().getName(),
                                            new SystemInfoCollector()
                                            .toString());
                                    infos.add(info);

                                    // Me voy
                                    doMove(destino);
                                } 
                                catch (Exception e) {
                                    System.out.println("fallo al moverse :/");
                                    e.getMessage();
                                }
                            } catch(IndexOutOfBoundsException e){
                                //Terminamos de recorrer todos los contenedores
                                _state++;
                            } finally {
                                break;
                            }
							
						case 2:							
                            System.out.println("*** MOSTAR INFORMACIONES RECOLECTADAS ***");
                            System.out.println(
                                        String.format(
                                        "Tiempo total de recorrido: %d ms",
                                        System.currentTimeMillis() - startTime));

                            for (String info : infos) {
                                System.out.println(info + "\n");
                            }

                            System.out.println(
                                    String.format("Eliminando agente en %s", 
                                        here().getName()));
							try {
								doDelete();
							} 
							catch (Exception e) {
								System.out.println("Falló al eliminar :/");
								e.getMessage();
							}
							break; 
					}
				}
			}
		);
	}
	



    /**
     * Obtiene los contenedores de la plataforma
     */
	protected ArrayList<Location> getContainers() {	 	
		getContentManager().registerLanguage(new SLCodec());
	    getContentManager().registerOntology(MobilityOntology.getInstance());
	    
	    ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
	    request.setLanguage(new SLCodec().getName());
	    
	    // Establecemos que MobilityOntology sea la ontologia de este mensaje.
	    request.setOntology(MobilityOntology.getInstance().getName());
	    
	    // Solicitamos a AMS una lista de containers disponibles
	    Action action = new Action(getAMS(), new QueryPlatformLocationsAction());
	    
        ArrayList<Location> _containers = new ArrayList<Location>();
	    try {
	      getContentManager().fillContent(request, action);
	      request.addReceiver(action.getActor());
	      send(request);
	 
	      // Filtramos los mensajes INFORM que llegan desde el AMS
	      MessageTemplate mt = MessageTemplate.and(
                  MessageTemplate.MatchSender(getAMS()),
                  MessageTemplate.MatchPerformative(ACLMessage.INFORM));
	 
	      ACLMessage resp = blockingReceive(mt);
	      ContentElement ce = getContentManager().extractContent(resp);
	      Result result = (Result) ce;
	      jade.util.leap.Iterator it = result.getItems().iterator();
          
	      // Almacena un ArrayList "Locations" de "Containers" 
          // donde puede moverse el agente movil.
	      while(it.hasNext()) {
		    _containers.add((Location) it.next());
	      }
	    }
	    catch(Exception ex) {
            ex.printStackTrace();	
        } finally {
            return _containers;
        }
	}	
	
    /**
     * Imprime los contenedores
     */
	protected void verContainers(ArrayList<Location> containers) {
	    System.out.println("******Containers disponibles: *******\n");
	    for(int i=0; i<containers.size(); i++) {
            String name = (containers.get(i)).getName();
            System.out.println(String.format("[%d] %s", i, name));
	    }
	    System.out.println("\n");
	 }
	
	
}	
