package rfs;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;


public class ServidorRFS extends ServidorTCP {

	public ServidorRFS(int port) {
		super(port);
	}

	@Override
	public void handleClient(Socket clientSocket) {
		System.out.println("Servidor RFS: cliente conectado");
		try {
			DataInputStream inputStream = new DataInputStream(
					clientSocket.getInputStream());
			DataOutputStream outputStream = new DataOutputStream(
					clientSocket.getOutputStream());
			
			byte[] buffer = new byte[2048];
			
			inputStream.read(buffer);
			String[] input = new String(buffer)
					.trim()
					.toLowerCase()
					.split(" ");

			try {
				String cmd = input[0];
				String file = input[1];
				
				if (cmd.equalsIgnoreCase("escribir")) {
					outputStream.write("OK".getBytes());
					System.out.println(
							String.format(
									"Hay que escribir [%s]",
									file));
					int count = 0;
					byte[] fileBuffer = new byte[1024];
					FileOutputStream fileOutput = 
							new FileOutputStream(file);
					
//					Mientras sigan llegando datos para escribir, vamos escribiendo al archivo
					while((count = inputStream.read(fileBuffer)) > 0) {
						System.out.println(
								String.format(
										"ServidorRFS: recibido [%s]",
										new String(fileBuffer).trim()));
						
						fileOutput.write(fileBuffer, 0, count);
					}
					fileOutput.close();
					
				} else if (cmd.equalsIgnoreCase("leer")) {
					System.out.println(
							String.format(
									"Hay que leer [%s]",
									file));
					
//					Armamos la ruta absoluta del archivo
//					OJO QUE CAPAZ QUE ESTO NO ANDA EN WINDOR
					String filepath = String.format(
							"%s/%s",
							System.getProperty("user.dir"),
							file);
					
					File fileToSend = new File(filepath);
					
//					Si el archivo no existe, le avisamos al cliente
					if(!fileToSend.exists()) {
						System.out.println(String.format(
								"ServidorRFS: No existe [%s]",
								filepath));
						outputStream.write("NO OK".getBytes());
						return;
					}
					
					outputStream.write("OK".getBytes());
					
					byte[] fileBuffer = new byte[1024];
					
					FileInputStream fileStream = 
							new FileInputStream(fileToSend);
					
					int count = 0;
//					Mientras se sigan leyendo datos del archivo, seguimos mandando
					while((count = fileStream.read(fileBuffer)) > 0) { 
						System.out.println(String.format(
								"ServidorRFS: mandando [%s]",
								new String(fileBuffer).trim()));
						outputStream.write(fileBuffer, 0, count);
					}
					
					fileStream.close();
					
				} else {
					System.out.println("CUALQUIERA EL COMANDO");
					outputStream.write("NO OK".getBytes());
				}
			} catch (IndexOutOfBoundsException e) {
				System.out.println("Servidor RFS: ERROR AL PROCESAR");
				System.out.println(e);
				outputStream.write("NO OK".getBytes());
			}
			
		} catch (IOException e) {
			System.out.println("Servidor RFS: handleClient");
			System.out.println(e);
			// TODO: handle exception
		}
	}

}
