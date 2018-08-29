package rfs;

import java.net.Socket;
import java.net.SocketException;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.DataInputStream;

public class ServidorEcho extends ServidorTCP {

	/**
	 * Clase que modela un servidor TCP que hace Echo
	 * 
	 * @param port puerto en el cuál escuchará el servidor
	 */
	public ServidorEcho(int port) {
		super(port);

	}


	@Override
	public void handleClient(Socket clientSocket) {
		System.out.println("CLIENTE CONECTADO");
		try {
			DataInputStream inputStream = new DataInputStream(
					clientSocket.getInputStream());
			DataOutputStream outputStream = new DataOutputStream(
					clientSocket.getOutputStream());
			
			String buffer;
			while(true) {
				try {
					buffer = inputStream.readUTF();
					System.out.println(String.format(
							"RECIBIDO: %s", buffer));
					outputStream.writeUTF(buffer.toUpperCase());
				} catch (Exception e) {
					return;
				}
				
			}
			
			
		} catch (SocketException e) {
			System.out.println(e);
		} catch (IOException e) {
			System.out.println(e);
		} 
		

	}

}
