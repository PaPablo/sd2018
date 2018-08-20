package rfs;

import java.io.*;
import java.net.*;

public class ClienteTCP {
	
	private String host;
	private int port; 
	private Socket socket; 
	
	/**
	 * Clase que modela un cliente TCP
	 * 
	 * @param host nombre del host al cual conectarse
	 * @param port puerto en el cuál estará escuchando el servidor
	 */
	public ClienteTCP(String host, int port) {
		this.setHost(host);
		this.setPort(port);
	}
	
	/**
	 * Conecta el cliente con el servidor
	 * 
	 * @return el socket de la conexión
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public Socket connect() throws UnknownHostException, IOException {
		this.setSocket(new Socket(
				this.getHost(),
				this.getPort()
				));
		return this.getSocket();
	}
	
	/**
	 * Envía una cadena por el socket
	 * 
	 * @param toSend cadena UTF a enviar a través del socket
	 * @throws IOException
	 */
	public void send(String toSend) throws IOException {
		this.send(toSend.getBytes());
	}
	
	/**
	 * Envía bytes por el socket
	 * 
	 * @param toSend arreglo de bytes a enviar a traes del socket
	 * @throws IOException
	 */
	public void send(byte[] toSend) throws IOException {
		DataOutputStream output = new DataOutputStream(
				this.getSocket().getOutputStream());
		output.write(toSend);
	}
	/**
	 * 
	 * @return cadena de datos UTF leída del socket
	 * @throws IOException
	 */
	public String recv() throws IOException {
		DataInputStream input = new DataInputStream(
				this.getSocket().getInputStream());
		return input.readUTF();
	}
	
	/**
	 * Cierra la conexión establecida con el servidor
	 * @throws IOException
	 */
	public void disconnect() throws IOException {
		this.getSocket().close();
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

}
