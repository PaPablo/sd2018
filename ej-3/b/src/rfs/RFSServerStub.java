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
import rfsArguments.RFSError;
import rfsArguments.RFSOpenArgument;
import rfsArguments.RFSReadArgument;
import rfsArguments.RFSReadArgumentServer;
import rfsArguments.RFSWriteArgument;

public class RFSServerStub {

	private int port;
	private IStatefulFileSystem fileSystem;

	/**
	 * Capa de middleware del servidor de RFS
	 * 
	 * @param port
	 *            puerto en el cuál escuchará conexiones
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
	 * 
	 * @throws IOException
	 */
	public void listen() throws IOException {
		System.out.println(
				String.format(
						"RFSServerStub: escuchando en puerto %d",
						this.getPort()));
		ServerSocket socket = new ServerSocket(this.getPort());

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
	 * 
	 * @param clientSocket
	 *            socket de la conexión
	 * @throws IOException
	 */
	public void handleClient(Socket clientSocket) throws IOException {
		ObjectOutputStream out = 
				new ObjectOutputStream(clientSocket.getOutputStream());
		ObjectInputStream in = 
				new ObjectInputStream(clientSocket.getInputStream());

		try {
			RFSArgument arg = (RFSArgument) in.readObject();

			if (arg instanceof RFSOpenArgument) {
				// ABRIR ARCHIVO
				RFSOpenArgument openArg = (RFSOpenArgument) arg;

				System.out.println(
						String.format(
								"RFSServerStub: hay que abrir [%s]", 
								openArg.getFilename().trim()));

				// El tema eran los FileStream
				// Si abrís el OutputStream, te hace cagar el archivo
				File openedFile = 
						this
						.fileSystem
						.openFile(
								openArg
								.getFilename()
								.trim())
						.getFile();

				System.out.println(
						String.format(
								"RFSServerStub: abierto [%s]", 
								openedFile.getAbsolutePath()));

				out.writeObject(openedFile);
			} else if (arg instanceof RFSReadArgument) {
				// LEER ARCHIVO ABIERTO
				RFSReadArgument readArgIn = (RFSReadArgument) arg;
				

				System.out.println(
						String.format(
								"RFSServerStub: hay que leer [%s]", 
								readArgIn.getFile()));

				OpenedFile openedFile = 
						this.fileSystem.getOpenedFile(readArgIn.getFile());
				
				RFSReadArgumentServer readArgOut = new RFSReadArgumentServer(new byte[Client.BUFFER_SIZE]);
				
				int count = this.fileSystem
						.readOpenedFile(
								openedFile,
								readArgOut.getData()
								);

//				 System.out.println(
//						 String.format(
//								 "RFSServerStub: leido [%s]",
//								 new String(readArg.getData()).trim()));
				readArgOut.setCount(count);
				out.writeObject(readArgOut);
			} else if (arg instanceof RFSWriteArgument) {
				// ESCRIBIR ARCHIVO ABIERTO
				RFSWriteArgument writeArg = (RFSWriteArgument) arg;

				System.out.println("RFSServerStub: hay que escribir\n");
//				System.out.println(
//						String.format(
//								"RFSServerStub: hay que escribir [%s]", 
//								new String(writeArg.getData()).trim()));

				OpenedFile openedFile = 
						this.fileSystem.getOpenedFile(writeArg.getFile());

				int count = 
						this.fileSystem.writeOpenedFile(
								openedFile,
								writeArg.getData());

				out.writeObject(count);
			} else if (arg instanceof RFSCloseArgument) {
				// CERRAR ARCHIVO
				RFSCloseArgument closeArg = (RFSCloseArgument) arg;

				System.out.println(
						String.format(
								"RFSServerStub: hay que cerrar [%s]",
								closeArg.getFile().getName().trim()));

				OpenedFile openedFile = 
						this.fileSystem.getOpenedFile(closeArg.getFile());

				boolean success = 
						this.fileSystem.closeOpenedFile(openedFile);

//				System.out.println(
//						String.format("RFSServerStub: cerrado [%s]",
//								closeArg.getFile().getName().trim()));
				out.writeObject(success);
			}
		} catch (ClassNotFoundException 
				| NullPointerException
				| FileNotFoundException e) {
			out.writeObject(new RFSError());
		} finally {
			in.close();
			out.close();
		}

	}

}
