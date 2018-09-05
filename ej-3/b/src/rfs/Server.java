package rfs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import exceptions.FileNotOpenedException;

public class Server implements IStatefulFileSystem {

	private ArrayList<OpenedFile> openedFiles;
	public Server() {
		this.openedFiles = new ArrayList<>();
	}
	
	/**
	 * Devuelve un archivo abierto
	 * @param file archivo abierto a buscar
	 * @return el archivo abierto
	 * @throws FileNotOpenedException
	 */
	public OpenedFile getOpenedFile(File file){
		for (OpenedFile openedFile : this.openedFiles) {
			if (openedFile.getFile().equals(file)) {
				System.out.println(String.format(
						"Server: archivo encontrado [%s]",
						openedFile));
				return openedFile;
			}
		}
		return null;
	}
	
	public boolean closeOpenedFile(OpenedFile openedFile) {
		openedFile.close();
		return this.openedFiles.remove(openedFile);
	}

	/**
	 * Crea un archivo y lo almacena en una lista de archivos abiertos
	 * DEVUELVE NULL SI NO LO PUEDE ABRIR
	 * @param filename nombre del archivo
	 * @return el archivo creado
	 */
//	public File open(String filename) {
//		//TODO: Qué pasa si el archivo ya estaba abierto?
//		OpenedFile fileToAdd = new OpenedFile(filename);
//		this.openedFiles.add(fileToAdd);
//		return fileToAdd.getFile();
//	}
//
//	@Override
//	public int read(File file, byte[] buffer) {
//		// TODO Auto-generated method stub
//		return 0;
//	}
//
//	@Override
//	public int write(File file, byte[] data) {
//		// TODO Auto-generated method stub
//		return 0;
//	}
//
//	@Override
//	public boolean close(File file) {
//		// TODO Auto-generated method stub
//		return false;
//	}

	@Override
	public OpenedFile openFile(String filename) {
		//TODO: Qué pasa si el archivo ya estaba abierto?
		OpenedFile fileToAdd = new OpenedFile(filename);
		this.openedFiles.add(fileToAdd);
		return fileToAdd;
	}

	@Override
	public int readOpenedFile(OpenedFile openedFile, byte[] buffer) {
		try {
			return openedFile
					.getInputStream()
					.read(buffer);
		} catch (FileNotFoundException e) {
			return -1;
		} catch (IOException e) {
			return -1;
		}
	}

	@Override
	public int writeOpenedFile(OpenedFile openedFile, byte[] data) {
		try {
			openedFile
			.getOutputStream()
			.write(data);
			return data.length;
		} catch (FileNotFoundException e) {
			return -1;
		} catch (IOException e) {
			return -1;
		}
		
	}

}
