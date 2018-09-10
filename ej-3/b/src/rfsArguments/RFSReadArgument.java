package rfsArguments;

import java.io.File;

public class RFSReadArgument extends RFSArgument {
	private File file;
	private int qty;
	
	public RFSReadArgument(File file, int qty) {
		this.setFile(file);
		this.setQty(qty);
	}
	
	public int getQty() {
		return this.qty;
	}
	
	public void setQty(int qty) {
		this.qty = qty;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

}
