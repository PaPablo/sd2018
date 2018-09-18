package rfs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import exceptions.FileNotOpenedException;

public class Server extends UnicastRemoteObject 
    implements IFileSystem {

	private ArrayList<OpenedFile> openedFiles;
	public Server() throws RemoteException {
        super(); 

		this.openedFiles = new ArrayList<>();
	}
	

	public OpenedFile getOpenedFile(File file){
		for (OpenedFile openedFile : this.openedFiles) {
			if (openedFile.getFile().equals(file)) {
				//System.out.println(String.format(
						//"Server: archivo encontrado [%s]",
						//openedFile));
				return openedFile;
			}
		}
		return null;
	}
	
	public boolean closeOpenedFile(OpenedFile openedFile) {
		openedFile.close();
		return this.openedFiles.remove(openedFile);
	}


	public OpenedFile openFile(String filename) {
		//TODO: Qué pasa si el archivo ya estaba abierto?
		OpenedFile fileToAdd = new OpenedFile(filename);
		this.openedFiles.add(fileToAdd);
		return fileToAdd;
	}

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


	@Override
	public File open(String filename) {
        System.out.println(String.format(
                    "Server: open [%s]", filename));

		OpenedFile f = new OpenedFile(filename);
		this.openedFiles.add(f);
		return f.getFile();
	}


	@Override
	public int read(File file, byte[] buffer) {
        System.out.println(String.format(
                    "Server: read [%s]", file));

		OpenedFile f = this.getOpenedFile(file);
		try {
			int count = f.getInputStream().read(buffer);

            System.out.println(String.format("Server: leí [%s]",
                        new String(buffer).trim()));

            return count;
		} catch (FileNotFoundException e) {
			return -1;
		} catch (IOException e) {
			return -1;
		}
	}


	@Override
	public int write(File file, byte[] data) {
        System.out.println(String.format(
                    "Server: write [%s]", file));

		OpenedFile f = this.getOpenedFile(file);
		try {
			f.getOutputStream().write(data);
		} catch (IOException e) {
			return -1;
		}
		
		return data.length;
	}


	@Override
	public boolean close(File file) {
        System.out.println(String.format(
                    "Server: close [%s]", file));

		return this.closeOpenedFile(this.getOpenedFile(file));
	}

}
