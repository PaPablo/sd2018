package servidor;

import java.rmi.Naming;
import java.rmi.registry.Registry;

import reloj.Reloj;

public class Servidor {

    public Servidor(String host, int offset) {
        try {
            String relojRname = String.format(
                    "//%s:%d/Reloj", 
                    host,
                    Registry.REGISTRY_PORT);
            Naming.rebind(relojRname, new Reloj(offset));
        } catch(Exception e){
            e.printStackTrace();
        }
    }

}
