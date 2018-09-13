package rfsArguments;

import java.io.File;

public class RFSReadArgument extends RFSArgument {
	private File file;
	private int count;
	private byte[] data;
	
	public RFSReadArgument(File file) {
		this(file, null);
	}

	public RFSReadArgument(File file, byte[] buffer) {
		this.setFile(file);
		this.setData(buffer);
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

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
}
