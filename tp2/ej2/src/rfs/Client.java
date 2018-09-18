package rfs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import java.rmi.Naming;

import exceptions.CouldNotReadFileException;
import exceptions.CouldNotWriteFileException;

public class Client {
	private IFileSystem fileSystem;
    private String host;
    private int port;

	public static final int BUFFER_SIZE = 1024 * 8;
    
		/**
	 * Cliente para leer/escribir archivos
	 */
	public Client(String host, int port) {
        try {
            this.setHost(host);
            this.setPort(port);
            String rname = this.getRname();
            System.out.println(String.format("Client rname = %s", rname));
            this.fileSystem =
                (IFileSystem) Naming.lookup(rname);
        } catch(Exception e){
            e.printStackTrace();
        }
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
			byte[] fileBuffer = new byte[BUFFER_SIZE];
			
			while(fileStream.read(fileBuffer) > 0) {
				this.fileSystem.write(openedFile, fileBuffer);
			}
			
			this.fileSystem.close(openedFile);
			fileStream.close();
		} catch (FileNotFoundException e) {
			throw new CouldNotWriteFileException("FileNotFound");
		} catch (IOException e) {
			throw new CouldNotWriteFileException("IOException");
        } catch (NullPointerException e) {
			throw new CouldNotWriteFileException("NullPointerException");
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
			
			if(!openedFile.exists()) {
				throw new CouldNotReadFileException("Archivo no existe");
			}
			
			String filepath = String.format(
					"%s/%s",
					System.getProperty("user.home"),
					filename);
			
			FileOutputStream fileStream = 
					new FileOutputStream(
							new File(filepath));
			
			//int count = ;
			//byte[] fileBuffer = new byte[BUFFER_SIZE];
			//StringBuilder fileContent = new StringBuilder();
            int count = BUFFER_SIZE;
            byte[] fileBuffer;
            String _recv; 
			
			while(true) {
                fileBuffer = this.fileSystem.read(openedFile, count);

                if (fileBuffer == null || fileBuffer.length == 0) break;

                _recv = new String(fileBuffer).trim();
				//fileContent.append(_recv);
				fileStream.write(fileBuffer);
                System.out.println(String.format(
                            "Client: _recv = %s", _recv
                            ));
			}

			//System.out.println(String.format(
					//"Client: recibi [%s]",
					//fileContent.toString().trim()));
		
			this.fileSystem.close(openedFile);
			fileStream.close();
		} catch (FileNotFoundException e) {
			throw new CouldNotReadFileException("FileNotFound");
		} catch (IOException e) {
			throw new CouldNotReadFileException("IOException");
		} catch (NullPointerException e) {
			throw new CouldNotReadFileException("NullPointerException");
		}
    }

    public String getRname() {
        return String.format("//%s:%d/%s",
                this.getHost(),
                this.getPort(),
                this.getRemoteFSName());
    }
    public String getRemoteFSName() {
        return "RFS";
    }
    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
