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
