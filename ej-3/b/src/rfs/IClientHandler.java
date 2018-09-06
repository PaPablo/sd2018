package rfs;

import java.net.Socket;

public interface IClientHandler {
	public void handleClient(Socket clientSocket);
}
