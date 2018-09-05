package rfs;

import java.io.File;

public interface IStatefulFileSystem {
	public OpenedFile openFile(String filename);
	public OpenedFile getOpenedFile(File file);
	public boolean closeOpenedFile(OpenedFile openedFile);
	public int readOpenedFile(OpenedFile openedFile, byte[] buffer);
	public int writeOpenedFile(OpenedFile openedFile, byte[] data);
}
