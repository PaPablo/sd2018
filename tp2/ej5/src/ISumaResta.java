/*
 * ISumaResta.java
 *
 * Created on 27 de abril de 2004, 21:17
 */

//package chuidiang.ejemplos.rmi.suma;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface remota con los m�todos que se pueden llamar en remoto
 * @author  Javier Abell�n
 */
public interface ISumaResta extends Remote {
    public int suma (int a, int b) throws RemoteException; 
    public int resta (int a, int b) throws RemoteException; 
}
