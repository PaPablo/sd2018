package rfs;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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
		
		this.send(this.makeRFSWriteHeader(fileToSend));
		System.out.println(String.format(
				"ClientRFS: Antes de recv()"));
		
		DataInputStream in = new DataInputStream(
				this.getSocket().getInputStream());
		
		byte[] buffer = new byte[1024];
		
		in.read(buffer);
		
		String response = new String(buffer).trim();
		System.out.println(String.format(
				"ClientRFS: response = %s", response));
		
		if (!response.equals("OK")) {
			System.out.println(String.format(
					"ClientRFS: No se recibió un 'OK', se recibió un %s",
					response));
			return;
		}
		
		System.out.println("ClientRFS: Se recibió un OK. Mandamos archivo...");
		
		FileInputStream fileStream = 
				new FileInputStream(fileToSend);
		
		int count = 0;
//		Mientras se sigan leyendo datos del archivo, seguimos mandando
		while((count = fileStream.read(buffer)) > 0) { 
			System.out.println(String.format(
					"ClientRFS: mandando [%s]",
					new String(buffer)));
			this.send(buffer, 0, count);
		}
		
		fileStream.close();
	}
	
	public void readRemoteFile(String file) throws IOException{
		this.send(this.makeRFSReadHeader(file));
		
		DataInputStream in = new DataInputStream(
				this.getSocket().getInputStream());
		
		byte[] buffer = new byte[1024];
		
		in.read(buffer);
		
		String response = new String(buffer).trim();
		System.out.println(String.format(
				"ClientRFS: response = %s", response));
		
		if (!response.equals("OK")) {
			System.out.println(String.format(
					"ClientRFS: No se recibió un 'OK', se recibió un %s",
					response));
			return;
		}
		
		System.out.println("ClientRFS: Se recibió un OK. Recibimos archivo...");
		
		byte[] fileBuffer = new byte[1024];
		int count = 0;
		
		String filepath = String.format(
				"%s/%s",
				System.getProperty("user.home"),
				file);
		FileOutputStream fileOutput = 
				new FileOutputStream(
						new File(filepath));
		
		while((count = in.read(fileBuffer)) > 0) {
			System.out.println(String.format(
					"ClientRFS: recibido [%s]",
					new String(fileBuffer).trim()));
			fileOutput.write(fileBuffer, 0, count);
		}
		
		fileOutput.close();
	}
	
	
	public String makeRFSWriteHeader(File file) {
		return String.format("ESCRIBIR %s",
				file.getName());
	}
	
	public String makeRFSReadHeader(String file) {
		return String.format("LEER %s", 
				file);
	}

}
