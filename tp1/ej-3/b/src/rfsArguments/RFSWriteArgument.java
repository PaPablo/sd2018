package rfsArguments;

import java.io.File;

public class RFSWriteArgument extends RFSArgument {
	private File file;
	private int count;
	private byte[] data;
	
	public RFSWriteArgument(File file) {
		this(file, null);
	}
	
	public RFSWriteArgument(File file, byte[] data) {
		this.setFile(file);
		this.setData(data);
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
	public byte[] getData() {
		return data;
	}
	public void setData(byte[] data) {
		this.data = data;
	}
}
