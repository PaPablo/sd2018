package rfs;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ClienteRFS extends ClienteTCP {

	/**
	 * Clase que modela un cliente para el FS remoto
	 * 
	 * @param host nombre del host al cual conectarse
	 * @param port puerto en el cuál estará escuchando el servidor
	 */
	public ClienteRFS(String host, int port) {
		super(host, port);
	}
	
	/**
	 * Enviar un archivo al servidor
	 * 
	 * @param fileToSend archivo a enviar
	 * @throws IOException 
	 */
	public void sendFile(File fileToSend) throws IOException {
		
		this.sendRFSWriteHeader(fileToSend.getName());
		
		if (this.recv() != "OK") {
			System.out.println("No se pudo enviar el archivo");
			return;
		}
		
		FileInputStream inputStream = 
				new FileInputStream(fileToSend);
		byte [] buffer = new byte[1024];
		
		while(inputStream.read(buffer) != -1) {
			this.send(buffer);
		}
		
		inputStream.close();
	}
	
	public void sendRFSWriteHeader(String fileName) throws IOException {
		this.send(String.format("ESCRIBIR %s", fileName));
	}

}
