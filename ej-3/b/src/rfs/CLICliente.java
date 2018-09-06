package rfs;

import java.io.File;
import exceptions.CouldNotReadFileException;
import exceptions.CouldNotWriteFileException;
public class MainCliente {

	private static final String WRITE_CMD = "escribir";
	private static final String READ_CMD = "leer";
	
	public static void printHelp() {
		System.out.println(
				String.format(
						"ARGS: <%s> <%s> <%s> <%s>",
						"SERVER_HOST",
						"SERVER_PORT",
						"COMMAND",
						"FILE"));
	}
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		if(args.length < 4) {
			printHelp();
			System.exit(1);
		}
		
		String host = args[0];
		int port = Integer.parseInt(args[1]);
		String cmd = args[2];
		String file = args[3];
		
		Client client = new Client(host, port);
		
		try {
			if (cmd.equals(WRITE_CMD)) {
				client.writeFile(new File(file));
			} else if (cmd.equals(READ_CMD)) {
				client.readFile(file);
			}
		} catch (CouldNotWriteFileException |
				CouldNotReadFileException e) {
			System.out.println(String.format(
					"No se pudo completar la operación con éxito [%s]",
					e.getMessage()));
		} 
		
	}

}
