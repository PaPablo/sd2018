package rfs;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import com.sun.jdi.connect.Connector.Argument;

import rfsArguments.RFSArgument;
import rfsArguments.RFSOpenArgument;
import rfsArguments.RFSReadArgument;

public class RFSServerStub {

	private int port;
	private IFileSystem fileSystem;
	
	/**
	 * Capa de middleware del servidor de RFS
	 * @param port puerto en el cuál escuchará conexiones
	 */
	public RFSServerStub(int port) {
		this.setPort(port);
		this.fileSystem = new Server();
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
	

	/**
	 * Escucha peticiones entrantes de clientes
	 * @throws IOException
	 */
	public void listen() throws IOException {
		System.out.println(String.format(
				"RFSServerStub: escuchando en puerto %d",
				this.getPort()));
		ServerSocket socket = new ServerSocket(
				this.getPort());
		
		while (true) {
			try {
				Socket clientSocket = socket.accept();
				this.handleClient(clientSocket);
				clientSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Atiende a un cliente conectado
	 * @param clientSocket socket de la conexión
	 * @throws IOException
	 */
	public void handleClient(Socket clientSocket) throws IOException {
		ObjectOutputStream out = 
				new ObjectOutputStream(
						clientSocket.getOutputStream());
		ObjectInputStream in = 
				new ObjectInputStream(
						clientSocket.getInputStream());
		
		try {
			RFSArgument arg = (RFSArgument) in.readObject();
			
			if (arg instanceof RFSOpenArgument) {
				RFSOpenArgument openArg = (RFSOpenArgument) arg;
				
				System.out.println(
						String.format(
								"RFSServerStub: hay que abrir [%s]",
								openArg.getFilename().trim()));
				
				File openedFile = this.fileSystem.open(
						openArg.getFilename());
				out.writeObject(openedFile);
			} else if (arg instanceof RFSReadArgument) {
				RFSReadArgument readArg = (RFSReadArgument) arg;
				
				System.out.println(
						String.format(
								"RFSServerStub: hay que leer [%s]",
								readArg.getFile()));
				
				FileInputStream fileStream = new FileInputStream(
						readArg.getFile());
				
//				NO CORTA NUNCA DE LEER
				byte[] fileBuffer = new byte[readArg.getCount()];
				fileStream.read(fileBuffer, 0, readArg.getCount());
				
				readArg.setData(fileBuffer);
				
				fileStream.close();
				out.writeObject(readArg);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			in.close();
			out.close();
		}
		
	}

}
