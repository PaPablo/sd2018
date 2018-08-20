package rfs;

public class MainServidor {

	public static void main(String[] args) {
		try {
			ServidorTCP server = new ServidorRFS(8080);
			
			server.listen();
			
		} catch (Exception e) {
			System.out.println("SE PINCHÓ TODO");
			System.out.println(e);
		}
	}

}
