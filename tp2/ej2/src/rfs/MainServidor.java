package rfs;

import java.rmi.registry.Registry;

public class MainServidor {

	public static void main(String[] args) {
		try {
            String host = "localhost";
            int port = Registry.REGISTRY_PORT;
            String serviceName = "RFS";

            RFSPublisher publisher = 
                new RFSPublisher(host, port, serviceName);
			
		} catch (Exception e) {
			System.out.println("MainServidor: SE PINCHÓ TODO");
			e.printStackTrace();
		}
	}

}
