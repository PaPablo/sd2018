import jade.core.Runtime;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.*;

class Main {
    public static void main(String[] args) {

        if (args.length < 2){
            System.out.println("Necesito más comandos");
            System.exit(0);
        }

        String command,src,dst;

        
        if (args[0] == "help") {
            System.out.println("Argumentos posibles:");
            System.out.println("write <source-file> <destination-file>");
            System.out.println("read <source-file> <destination-file>");
            System.exit(0);
        } 
        else if (args.length < 3){
            System.exit(0);
        }
       // Get Command
        command = args[0];

        src = args[1];
        dst = args[2];


        // Get a hold on JADE runtime
        Runtime rt = Runtime.instance();
        // Create a default profile
        Profile p = new ProfileImpl();
        // Create a new non-main container, connecting to the default
        // main container (i.e. on this host, port 1099)
        ContainerController cc = rt.createAgentContainer(p);
        // Create a new agent, a DummyAgent
        // and pass it a reference to an Object

        Object reference = new Object();
        Object agentArgs[] = new Object[2];

        agentArgs[0] = src;
        agentArgs[1] = dst;

        try {
            //AgentController dummy = cc.createNewAgent("inProcess",
                    //"FileReader", agentArgs);
            String agent = null;

            if (command.equals("write")) {
                agent = "FileWriter";
            }
            else if (command.equals("read")) {
                agent = "FileReader";
            }
            else {
                System.out.println("Operación no válida");
                System.out.println("Operaciones posibles: read | write");
                System.exit(0);
            }

            // Fire up the agent
            AgentController dummy = cc.createNewAgent("inProcess",
                    agent, agentArgs);

            dummy.start();
        } catch(Exception e){
            e.printStackTrace();
        }

    }
}

