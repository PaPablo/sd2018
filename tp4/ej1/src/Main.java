import jade.core.Runtime;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.*;

class Main {
    public static void main(String[] args) {
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
        Object agentArgs[] = new Object[1];

        agentArgs[0]= new String("/home/pablo/archivito.txt");
        try {
            //AgentController dummy = cc.createNewAgent("inProcess",
                    //"FileReader", agentArgs);
            AgentController dummy = cc.createNewAgent("inProcess",
                    "FileWriter", agentArgs);
            // Fire up the agent
            dummy.start();
        } catch(Exception e){
            e.printStackTrace();
        }

    }
}

