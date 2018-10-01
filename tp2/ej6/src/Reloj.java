import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Reloj extends UnicastRemoteObject 
    implements IReloj {
    
    public Reloj() throws RemoteException {
        super();
    }

    public long now() {
        long horaMilis = System.currentTimeMillis();
        System.out.println(String.format(
                    "[Servidor] La hora es [%d]",
                    horaMilis));
        return horaMilis;
    }
}
