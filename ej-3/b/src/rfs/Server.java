package rfs;

import java.io.File;
import java.util.ArrayList;

public class Server implements IFileSystem {

	private ArrayList<File> openedFiles;
	public Server() {
		this.openedFiles = new ArrayList<>();
	}
	/**
	 * Crea un archivo y lo almacena en una lista de archivos abiertos
	 * @param filename nombre del archivo
	 * @return el archivo creado
	 */
	@Override
	public File open(String filename) {
		//TODO: Qué pasa si el archivo ya estaba abierto?
		File fileToAdd = new File(filename);
		this.openedFiles.add(fileToAdd);
		return fileToAdd;
	}

	@Override
	public int read(File file, int count, byte[] buffer) {
		// TODO Auto-generated method stub
		return 0;
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

}
