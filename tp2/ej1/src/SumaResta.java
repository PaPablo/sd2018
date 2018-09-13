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
public class SumaResta extends UnicastRemoteObject 
    implements ISumaResta
{
    /**
     * Construye una instancia de ObjetoRemoto
     * @throws RemoteException
     */
    protected SumaResta () throws RemoteException
    {
        super();
    }

    /**
     * Obtiene la suma de los sumandos que le pasan y la devuelve.
     */
    public int suma(int a, int b) 
    {
	    System.out.println (String.format("Sumando %d + %d...", a,b));
        return a+b;
    }
    public int resta(int a, int b) 
    {
	    System.out.println (String.format("Restando %d - %d...", a,b));
        return a-b;
    }
    
}
