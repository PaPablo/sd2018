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
public class MultiplicacionDivision extends UnicastRemoteObject 
    implements IMultiplicacionDivision
{
    /**
     * Construye una instancia de ObjetoRemoto
     * @throws RemoteException
     */
    protected MultiplicacionDivision () throws RemoteException
    {
        super();
    }

    /**
     * Obtiene la suma de los sumandos que le pasan y la devuelve.
     */
    public int multiplicacion(int a, int b) 
    {
        System.out.println(String.format("Multiplicando %d * %d...", a, b));
        return a*b;
    }

    public int division(int a, int b) 
    {
        System.out.println(String.format("Dividiendo %d / %d...", a, b));
        return (int)(a/b);
    }
    
}
