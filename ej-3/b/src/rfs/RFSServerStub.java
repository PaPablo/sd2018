package rfs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import com.sun.jdi.connect.Connector.Argument;

import exceptions.FileNotOpenedException;
import rfsArguments.RFSArgument;
import rfsArguments.RFSCloseArgument;
import rfsArguments.RFSOpenArgument;
import rfsArguments.RFSReadArgument;
import rfsArguments.RFSWriteArgument;

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
				// ABRIR ARCHIVO
				RFSOpenArgument openArg = (RFSOpenArgument) arg;
				
				System.out.println(
						String.format(
								"RFSServerStub: hay que abrir [%s]",
								openArg.getFilename().trim()));
				
//				ME ESTAS HACIENDO CAGAR LOS ARCHIVOSSS
//				El tema eran los FileStream
//				Si abrís el OutputStream, te hace cagar el archivo
				File openedFile = this.fileSystem.open(
						openArg.getFilename().trim());
				
				System.out.println(
						String.format(
								"RFSServerStub: abierto [%s]",
								openedFile.getAbsolutePath()));
				
				out.writeObject(openedFile);
			} else if (arg instanceof RFSReadArgument) {
				// LEER ARCHIVO ABIERTO
				RFSReadArgument readArg = (RFSReadArgument) arg;
				
				System.out.println(
						String.format(
								"RFSServerStub: hay que leer [%s]",
								readArg.getFile()));
				
				RFSOpenedFile openedFile = 
						this.getOpenedFile(readArg.getFile());
				
				FileInputStream fileStream = openedFile.getInputStream();
				int count = fileStream.read(readArg.getData());
				 	
//				System.out.println(
//						String.format(
//								"RFSServerStub: leido [%s]",
//								new String(readArg.getData()).trim()));
				readArg.setCount(count);
				out.writeObject(readArg);
			} else if (arg instanceof RFSWriteArgument) {
//				ESCRIBIR ARCHIVO ABIERTO
				RFSWriteArgument writeArg = (RFSWriteArgument) arg;
				
				System.out.println(
						String.format(
								"RFSServerStub: hay que escribir [%s]",
								new String(writeArg.getData()).trim()));
				
				RFSOpenedFile openedFile =
						this.getOpenedFile(writeArg.getFile());
				FileOutputStream fileStream = openedFile.getOutputStream();
				
				fileStream.write(writeArg.getData());
				
				out.writeObject(writeArg);
			} else if (arg instanceof RFSCloseArgument) {
				// CERRAR ARCHIVO
				RFSCloseArgument closeArg = (RFSCloseArgument) arg;
				
				System.out.println(String.format(
						"RFSServerStub: hay que cerrar [%s]",
						closeArg.getFile().getName().trim()));
				
				RFSOpenedFile openedFile =
						this.getOpenedFile(closeArg.getFile());
				
				boolean success = this.closeOpenedFile(openedFile);
				
				System.out.println(String.format(
						"RFSServerStub: cerrado [%s]",
						closeArg.getFile().getName().trim()));
				out.writeObject(success);
			}
		} catch (ClassNotFoundException 
				| FileNotOpenedException 
				| FileNotFoundException e) {
			out.writeObject(new RFSReadArgument(null));
		}  finally {
			in.close();
			out.close();
		}
		
	}
	
	public RFSOpenedFile getOpenedFile(File file) throws FileNotOpenedException {
		Server server = (Server) this.fileSystem;
		return server.getOpenedFile(file);
	}
	
	public boolean closeOpenedFile(RFSOpenedFile openedFile) {
		Server server = (Server) this.fileSystem;
		return server.closeOpenedFile(openedFile);
	}

}
