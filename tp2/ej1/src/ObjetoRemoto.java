/*
 * ObjetoRemoto.java
 *
 * Created on 27 de abril de 2004, 21:18
 */

//package chuidiang.ejemplos.rmi.suma;

//import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * @author  Javier Abell�n
 */
public class ObjetoRemoto extends UnicastRemoteObject 
    implements InterfaceRemota
{
    /**
     * Construye una instancia de ObjetoRemoto
     * @throws RemoteException
     */
    protected ObjetoRemoto () throws RemoteException
    {
        super();
    }

    /**
     * Obtiene la suma de los sumandos que le pasan y la devuelve.
     */
    public int suma(int a, int b) 
    {
	    System.out.println ("Sumando " + a + " + " + b +"...");
        return a+b;
    }
    
}
