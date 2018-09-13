package rfs;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

import exceptions.CouldNotReadFileException;
import rfsArguments.RFSArgument;
import rfsArguments.RFSCloseArgument;
import rfsArguments.RFSOpenArgument;
import rfsArguments.RFSReadArgument;
import rfsArguments.RFSWriteArgument;

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
			
			JOptionPane.showMessageDialog(null, "CONECTADO");
			
			out.writeObject(new RFSOpenArgument(filename));
			
			File file = (File) in.readObject();
			
			System.out.println(String.format(
					"RFSClientStub: recibido OPEN [%s]",
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

		Socket socket = null;
		ObjectOutputStream out = null;
		ObjectInputStream in = null;
		
		try {
			socket = this.connect();
			
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
			
			RFSWriteArgument arg = new RFSWriteArgument(file, data);
			arg.setCount(data.length);
			
			out.writeObject(arg);
			
			RFSWriteArgument writeArg = (RFSWriteArgument) in.readObject();

			return writeArg.getCount();
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

	@Override
	public boolean close(File file) {

		Socket socket = null;
		ObjectOutputStream out = null;
		ObjectInputStream in = null;
		try {
			socket = this.connect();
			
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
			
			out.writeObject(new RFSCloseArgument(file));
			
			boolean result = (boolean) in.readObject();
			
			System.out.println(String.format(
					"RFSClientStub: recibido CLOSE [%s]",
					result));
			
			return result;
		} catch (UnknownHostException e) {
			return false;
		} catch (ClassNotFoundException e) {
			return false;
		} catch (IOException e) {
			return false;
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
	public int read(File file, byte[] buffer) {
		Socket socket = null;
		ObjectOutputStream out = null;
		ObjectInputStream in = null;
		
		try {
			socket = this.connect();
			
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
			
			RFSReadArgument arg = new RFSReadArgument(file, buffer);
			arg.setCount(buffer.length);
			
			out.writeObject(arg);
			
			RFSReadArgument readArg = (RFSReadArgument) in.readObject();
			
			//Copiamos los datos a buffer
			for (int i = 0; i < readArg.getData().length; i++) {
				buffer[i] = readArg.getData()[i];
			}
			
//			System.out.println(String.format(
//					"RFSClientStub: recibido READ [%s]",
//					new String(buffer).trim()));
			
			return readArg.getCount();
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
