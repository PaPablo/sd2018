package rfs;

import java.io.IOException;
import java.net.*;

public abstract class ServidorTCP {
	
	private int port;
	
	/**
	 * Clase que modela un servidor TCP
	 * 
	 * @param port puerto en el cuál escuchará el servidor
	 */
	public ServidorTCP(int port) {
		this.setPort(port);
	}

	/**
	 * Espera la conexión de clientes
	 * 
	 * @throws IOException
	 */
	public void listen() throws IOException {
		ServerSocket listeningSocket = new ServerSocket(this.getPort());
		
		while(true) {
			Socket clientSocket = listeningSocket.accept();
			
			this.handleClient(clientSocket);
			
			listeningSocket.close();
		}
	}
	
	/**
	 * Maneja un cliente conectado
	 * 
	 * @param clientSocket socket del cliente conectado
	 */
	public abstract void handleClient(Socket clientSocket);
	
	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

}
