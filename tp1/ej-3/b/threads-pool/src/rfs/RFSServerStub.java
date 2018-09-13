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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.sun.jdi.connect.Connector.Argument;

import exceptions.FileNotOpenedException;
import rfsArguments.RFSArgument;
import rfsArguments.RFSCloseArgument;
import rfsArguments.RFSError;
import rfsArguments.RFSOpenArgument;
import rfsArguments.RFSReadArgument;
import rfsArguments.RFSWriteArgument;

public class RFSServerStub implements IClientHandler{

	private int port;
	private IStatefulFileSystem fileSystem;

	/**
	 * Capa de middleware del servidor de RFS
	 * 
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
	 * 
	 * @throws IOException
	 */
	public void listen() throws IOException {
		System.out.println(
				String.format(
						"RFSServerStub: escuchando en puerto %d",
						this.getPort()));
		ServerSocket socket = new ServerSocket(this.getPort());
		
		// Se crea el pool de hilos, tamaño 3
		ExecutorService pool = Executors.newFixedThreadPool(3);
		
		while (true) {
			try {
				Socket clientSocket = socket.accept();
				
				// Se le pasa el hilo al pool para que lo ejecute
				pool.execute(new RFSClientConnection(clientSocket, this));
				
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
	public void handleClient(Socket clientSocket) {
		ObjectOutputStream out = null;
		ObjectInputStream in = null;
		try {
			out = new ObjectOutputStream(clientSocket.getOutputStream());
			in = new ObjectInputStream(clientSocket.getInputStream());

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
				RFSReadArgument readArg = (RFSReadArgument) arg;

				System.out.println(
						String.format(
								"RFSServerStub: hay que leer [%s]", 
								readArg.getFile()));

				OpenedFile openedFile = 
						this.fileSystem.getOpenedFile(readArg.getFile());

				int count = this.fileSystem
						.readOpenedFile(
								openedFile,
								readArg.getData());

//				 System.out.println(
//						 String.format(
//								 "RFSServerStub: leido [%s]",
//								 new String(readArg.getData()).trim()));
				readArg.setCount(count);
				out.writeObject(readArg);
			} else if (arg instanceof RFSWriteArgument) {
				// ESCRIBIR ARCHIVO ABIERTO
				RFSWriteArgument writeArg = (RFSWriteArgument) arg;

				System.out.println(
						String.format(
								"RFSServerStub: hay que escribir [%s]", 
								new String(writeArg.getData()).trim()));

				OpenedFile openedFile = 
						this.fileSystem.getOpenedFile(writeArg.getFile());

				int count = 
						this.fileSystem.writeOpenedFile(
								openedFile,
								writeArg.getData());

				writeArg.setCount(count);
				out.writeObject(writeArg);
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
				| IOException e) {
			try {
				out.writeObject(new RFSError());
			} catch (IOException e1) {}
		} finally {
			try {
				in.close();
				out.close();
			} catch (IOException e) {}
		}

	}

}
