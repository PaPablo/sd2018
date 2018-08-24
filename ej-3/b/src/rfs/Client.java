package rfs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import exceptions.CouldNotReadFileException;
import exceptions.CouldNotWriteFileException;

public class Client {
	private IFileSystem fileSystem;
	
	/**
	 * Cliente para leer/escribir archivos
	 */
	public Client() {
		String host = "localhost";
		int port = 8080;
		this.fileSystem = new RFSClientStub(host, port);
	}
	
	/**
	 * Escribe un archivo completo
	 * @param file archivo a escribir
	 * @throws CouldNotWriteFileException
	 */
	public void writeFile(File file) throws CouldNotWriteFileException {
		try {
			
			File openedFile = this.fileSystem.open(file.getName());
			
			FileInputStream fileStream = 
					new FileInputStream(file);
			byte[] fileBuffer = new byte[1024];
			
			while(fileStream.read(fileBuffer) > 0) {
				this.fileSystem.write(openedFile, fileBuffer);
			}
			
			this.fileSystem.close(openedFile);
			fileStream.close();
		} catch (FileNotFoundException e) {
			throw new CouldNotWriteFileException();
		} catch (IOException e) {
			throw new CouldNotWriteFileException();
		}
	}
	
	/**
	 * Lee un archivo completo y lo guarda en el HOME del usuario
	 * @param filename nombre del archivo a leer
	 * @throws CouldNotReadFileException
	 */
	public void readFile(String filename) throws CouldNotReadFileException {
		try {
			File openedFile = this.fileSystem.open(filename);
			
			String filepath = String.format(
					"%s/%s",
					System.getProperty("user.home"),
					filename);
			FileOutputStream fileStream = 
					new FileOutputStream(
							new File(filepath));
			
			int count = 0;
			byte[] fileBuffer = new byte[1024];
			StringBuilder fileContent = new StringBuilder();
			
			while((count = this.fileSystem.read(
					openedFile, 1024, fileBuffer)) > 0) {
				fileContent.append(new String(fileBuffer));
//				fileStream.write(fileBuffer, 0, count);
			}
			
			System.out.println(String.format(
					"Client: recibi [%s]",
					fileContent.toString().trim()));
		
			this.fileSystem.close(openedFile);
			fileStream.close();
		} catch (FileNotFoundException e) {
			throw new CouldNotReadFileException();
		} catch (IOException e) {
			throw new CouldNotReadFileException();
		}
	}
}
