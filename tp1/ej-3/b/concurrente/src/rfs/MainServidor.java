package rfs;

public class MainServidor {

	public static void main(String[] args) {
		try {
			RFSServerStub server = new RFSServerStub(8080);
			
			server.listen();
			
		} catch (Exception e) {
			System.out.println("MainServidor: SE PINCHÓ TODO");
			e.printStackTrace();
		}
	}

}
