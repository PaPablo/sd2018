package rfs;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import exceptions.FileNotOpenedException;

public class Server implements IFileSystem {

	private ArrayList<RFSOpenedFile> openedFiles;
	public Server() {
		this.openedFiles = new ArrayList<>();
	}
	
	/**
	 * Devuelve un archivo abierto
	 * @param file archivo abierto a buscar
	 * @return el archivo abierto
	 * @throws FileNotOpenedException
	 */
	public RFSOpenedFile getOpenedFile(File file) throws FileNotOpenedException {
		for (RFSOpenedFile openedFile : this.openedFiles) {
			if (openedFile.getFile().equals(file)) {
				System.out.println(String.format(
						"Server: archivo encontrado [%s]",
						openedFile));
				return openedFile;
			}
		} 
		throw new FileNotOpenedException();
	}
	
	public boolean closeOpenedFile(RFSOpenedFile openedFile) {
		return this.openedFiles.remove(openedFile);
	}

	/**
	 * Crea un archivo y lo almacena en una lista de archivos abiertos
	 * DEVUELVE NULL SI NO LO PUEDE ABRIR
	 * @param filename nombre del archivo
	 * @return el archivo creado
	 */
	@Override
	public File open(String filename) {
		//TODO: Qué pasa si el archivo ya estaba abierto?
		RFSOpenedFile fileToAdd = new RFSOpenedFile(filename);
		this.openedFiles.add(fileToAdd);
		return fileToAdd.getFile();
	}

	@Override
	public int read(File file, byte[] buffer) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int write(File file, byte[] data) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean close(File file) {
		// TODO Auto-generated method stub
		return false;
	}

}
