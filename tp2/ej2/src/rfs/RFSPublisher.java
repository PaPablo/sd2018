package rfs;

import java.net.MalformedURLException;

import java.rmi.Naming;
import java.rmi.RemoteException;

public class RFSPublisher {

    public RFSPublisher(String host, int port, String serviceName) 
            throws RemoteException, MalformedURLException {
        String rname = String.format(
                "//%s:%d/%s",
                host,
                port,
                serviceName);

        System.out.println(String.format("Publicando el RFS en %s",
                    rname));
        Naming.rebind(rname, new Server());
    }
}
