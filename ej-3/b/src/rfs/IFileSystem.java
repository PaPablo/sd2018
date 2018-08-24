package rfs;

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;

public interface IFileSystem {
	public File open(String filename);
	public int read(File file, int count, byte[] buffer);
	public int write(File file, byte[] data);
	public int close(File file);
}