package rfs;

import java.io.DataInputStream;
import java.io.DataOutputStream;
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
			
			/*
			 * Recibir datos and shit
			 * */
			
			/*
			byte[] buffer = new byte[1024];
			inputStream.read(buffer);
			System.out.println(String.format("Servidor RFS: [%s]", new String(buffer, "UTF-8")));
			*/
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

}
