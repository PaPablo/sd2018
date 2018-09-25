/*
 * ObjetoRemoto.java
 *
 * Created on 27 de abril de 2004, 21:18
 */

//package chuidiang.ejemplos.rmi.suma;

//import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import javax.swing.JOptionPane;

/**
 * @author  Javier Abell�n
 */
public class SumaResta extends UnicastRemoteObject 
    implements ISumaResta
{

    private int _total;
    /**
     * Construye una instancia de ObjetoRemoto
     * @throws RemoteException
     */
    protected SumaResta () throws RemoteException
    {
        super();
        this.setTotal(0);
    }

    /**
     * Obtiene la suma de los sumandos que le pasan y la devuelve.
     */
    public int suma(int a, int b) 
    {
        System.out.println (String.format("Sumando %d + %d...", a,b));
        this.displayMsg(String.format("TOTAL ANTES DE SUMAR: %d", 
                    this.getTotal()));
        this.increment();
        this.displayMsg(String.format("TOTAL DESPUES DE SUMAR: %d", 
                    this.getTotal()));
        return a+b;
    }
    public int resta(int a, int b) 
    {
        System.out.println (String.format("Restando %d - %d...", a,b));
        this.displayMsg(String.format("TOTAL ANTES DE RESTAR: %d", 
                    this.getTotal()));
        this.increment();
        this.displayMsg(String.format("TOTAL DESPUES DE RESTAR: %d", 
                    this.getTotal()));
        return a-b;
    }

    public void displayMsg(String msg) {
        JOptionPane.showMessageDialog(
                null, 
                msg);
    }

    public void increment() {
        this.setTotal(this.getTotal() + 1);
    }

    public void setTotal(int _total) {
        this._total = _total;
    }

    public int getTotal() {
        return _total;
    }

}
