package rfs;

import java.io.IOException;
import java.net.Socket;

/**
 * Clase para manejar la conexión de un cliente de forma concurrente
 * @author luciano
 *
 */
public class RFSClientConnection extends Thread {
	private Socket socket;
	private IClientHandler clientHandler;

	public RFSClientConnection(Socket socket, IClientHandler clientHandler) {
		this.setClientHandler(clientHandler);
		this.setSocket(socket);
		
		System.out.println(
				String.format(
						"Hilo [%d] atendiendo al cliente [%s]",
						this.getId(),
						socket));
	}

	@Override
	public void run() {
		try {
			this.getClientHandler()
			.handleClient(this.getSocket());
			this.getSocket().close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public IClientHandler getClientHandler() {
		return clientHandler;
	}

	public void setClientHandler(IClientHandler clientHandler) {
		this.clientHandler = clientHandler;
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}
}
