package rfs;

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IFileSystem extends Remote {
	public File open(String filename) throws RemoteException;
	public byte[] read(File file, int count) throws RemoteException;
	public int write(File file, byte[] data) throws RemoteException;
	public boolean close(File file) throws RemoteException;
}
