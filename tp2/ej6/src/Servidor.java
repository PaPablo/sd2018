import java.rmi.Naming;
import java.rmi.registry.Registry;

public class Servidor {

    public Servidor(String host) {
        try {
            String relojRname = String.format(
                    "//%s:%d/Reloj", 
                    host,
                    Registry.REGISTRY_PORT);
            Naming.rebind(relojRname, new Reloj());
        } catch(Exception e){
            e.printStackTrace();
        }
    }

}
