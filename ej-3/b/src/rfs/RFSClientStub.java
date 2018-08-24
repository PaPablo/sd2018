package rfs;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import rfsArguments.RFSArgument;
import rfsArguments.RFSOpenArgument;
import rfsArguments.RFSReadArgument;

public class RFSClientStub implements IFileSystem {
	
	private String host;
	private int port;
	
	/**
	 * Stub para leer/escribir archivos remotos
	 * @param host dirección del host
	 * @param port puerto en el que está escuchando el host
	 */
	public RFSClientStub(String host, int port) {
		this.setHost(host);
		this.setPort(port);
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

	@Override
	public File open(String filename) {
		Socket socket = null;
		ObjectOutputStream out = null;
		ObjectInputStream in = null;
		try {
			socket = this.connect();
			
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
			
			out.writeObject(new RFSOpenArgument(filename));
			
			File file = (File) in.readObject();
			
			System.out.println(String.format(
					"RFSClientStub: recibido [%s]",
					file.getAbsolutePath().trim()));
			
			return file;
		} catch (UnknownHostException e) {
			return null;
		} catch (ClassNotFoundException e) {
			return null;
		} catch (IOException e) {
			return null;
		} finally {
			try {
				in.close();
				out.close();
				socket.close();
			} catch (IOException e) {
			}
		}
	}


	@Override
	public int write(File file, byte[] data) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int close(File file) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int read(File file, int count, byte[] buffer) {
		Socket socket = null;
		ObjectOutputStream out = null;
		ObjectInputStream in = null;
		
		try {
			socket = this.connect();
			
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
			
			out.writeObject(new RFSReadArgument(file, count));
			
			RFSReadArgument readArg = (RFSReadArgument) in.readObject();
			buffer = readArg.getData();
			
			System.out.println(String.format(
					"RFSClientStub: recibido [%s]",
					new String(readArg.getData())));
			
			return readArg.getData().length;
		} catch (UnknownHostException e) {
			return -1;
		} catch (ClassNotFoundException e) {
			return -1;
		} catch (IOException e) {
			return -1;
		} finally {
			try {
				in.close();
				out.close();
				socket.close();
			} catch (IOException e) {
			}
		}

	}
	
	public Socket connect() throws UnknownHostException, IOException {
		String host = this.getHost();
		int port = this.getPort();
		return new Socket(host, port);
	}
}
