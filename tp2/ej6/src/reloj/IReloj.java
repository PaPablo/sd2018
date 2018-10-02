package reloj;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interfase para implementar un reloj
 */
public interface IReloj extends Remote {
    public long now() throws RemoteException;
}
