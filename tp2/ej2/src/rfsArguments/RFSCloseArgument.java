package rfsArguments;

import java.io.File;

public class RFSCloseArgument extends RFSArgument{
	private File file;
	
	public RFSCloseArgument(File file) {
		this.setFile(file);
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}
}
