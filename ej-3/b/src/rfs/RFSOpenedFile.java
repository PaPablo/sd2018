package rfs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class RFSOpenedFile {
	
	private File file;
	private FileInputStream inputStream;
	private FileOutputStream outputStream;

	public RFSOpenedFile(String filename){
		File f = new File(filename);
		this.setFile(f);
	}
	
	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public FileInputStream getInputStream() throws FileNotFoundException {
		if (this.inputStream == null) {
			FileInputStream fileStream = 
					new FileInputStream(
							this.getFile());
			this.setInputStream(fileStream);
			return fileStream;
		}
		return inputStream;
	}

	public void setInputStream(FileInputStream inputStream) {
		this.inputStream = inputStream;
	}

	public FileOutputStream getOutputStream() throws FileNotFoundException {
		if (this.outputStream == null) {
			FileOutputStream fileStream =
					new FileOutputStream(
							this.getFile());
			this.setOutputStream(fileStream);
			return fileStream;
		}
		return outputStream;
	}

	public void setOutputStream(FileOutputStream outputStream) {
		this.outputStream = outputStream;
	}
	
	@Override
	public String toString() {
		return String.format(
				"RFSOpenedFile [%s]",
				this.getFile());
	}
	
	
}
