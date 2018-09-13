package rfsArguments;

import java.io.File;

public class RFSReadArgumentServer extends RFSArgument {
	private int count;
	private byte[] data;

	public RFSReadArgumentServer(byte[] buffer) {
		this.setData(buffer);
	}
	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
}