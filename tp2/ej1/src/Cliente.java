/*
 * Cliente.java
 *
 * Ejemplo de muy simple de rmi
 */

import java.rmi.Naming;                    /* lookup         */
import java.rmi.registry.Registry;         /* REGISTRY_PORT  */

import java.net.MalformedURLException;     /* Exceptions...  */
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * Ejemplo de cliente rmi nocivo, para aprovecharse de un servidor sin
 * SecurityManager.
 * @author  Javier Abell�n
 */
public class Cliente {
    
    private String host;

    /**
     * Crea nueva instancia de Cliente 
     * */
    public Cliente(String host) {

        this.setHost(host);
        try {
            String rname = 
                "//" + host + ":" + Registry.REGISTRY_PORT + "/ObjetoRemoto";
            InterfaceRemota objetoRemoto = 
                (InterfaceRemota) Naming.lookup (rname);
            System.out.print ("2 + 3 = ");
            System.out.println (objetoRemoto.suma(2,3));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
    }

    public String getRemoteByOp(String op) {
        return op == "+" || op == "-" ? "SumaResta"
            : "MultiplicacionDivision";
    }
    public int sumar(int n1, int n2) throws NoSePudoCompletarLaOperacionException {
        return -1;
    }
    public int restar(int n1, int n2) throws NoSePudoCompletarLaOperacionException {
        return -1;
    }
    public int multiplicar(int n1, int n2) throws NoSePudoCompletarLaOperacionException {
        return -1;
    }
    public int dividir(int n1, int n2) throws NoSePudoCompletarLaOperacionException {
        return -1;
    }

    

    public String getHost() {
        return host;
    }
    public void setHost(String host) {
        this.host = host;
    }
}
