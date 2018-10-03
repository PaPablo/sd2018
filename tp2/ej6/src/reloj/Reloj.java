package reloj;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import utils.DateFormatter;

public class Reloj extends UnicastRemoteObject 
    implements IReloj {
    
    private int offset;
    public Reloj(int offset) throws RemoteException {
        super();
        this.offset = offset;
        System.out.println(String.format(
                    "Reloj creado con un offset de [%d]",
                    offset
                    ));
    }

    public long now() {
        long horaMilis = System.currentTimeMillis() + this.offset;
        System.out.println(String.format(
                    "[Servidor] La hora es %s [%d]",
                    DateFormatter.getTimeFromMillis(horaMilis),
                    horaMilis));
        return horaMilis;
    }
}
