package rfsArguments;

import java.io.File;

public class RFSReadArgument extends RFSArgument {
	private File file;
	private byte[] data;
	private int count;
	
	public RFSReadArgument(File file, int count) {
		this.setCount(count);
		this.setFile(file);
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}
}
