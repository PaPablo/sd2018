package reloj;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import utils.DateFormatter;

public class Reloj extends UnicastRemoteObject 
    implements IReloj {
    
    public Reloj() throws RemoteException {
        super();
    }

    public long now() {
        long horaMilis = System.currentTimeMillis() + 20000;
        System.out.println(String.format(
                    "[Servidor] La hora es %s [%d]",
                    DateFormatter.getTimeFromMillis(horaMilis),
                    horaMilis));
        return horaMilis;
    }
}
