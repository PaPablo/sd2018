import java.rmi.Naming;
import java.rmi.registry.Registry;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Cliente {
    
    private String host;

    /**
     * Crea nueva instancia de Cliente 
     * */
    public Cliente(String host) {
        this.setHost(host);
    }

    /**
     * Devuelve el nombre del objeto remoto necesario para completar la operación
     */
    public String getRemoteByOp(String op) {
        return op == "+" || op == "-" ? "SumaResta"
            : "MultiplicacionDivision";
    }

    /**
     * Devuelve el URI del objeto remoto necesario
     */
    public String getRname(String op) {
        return String.format("//%s:%d/%s",
                this.getHost(),
                Registry.REGISTRY_PORT,
                this.getRemoteByOp(op));
    }

    /**
     * Wrapper para demostrar que el pasaje de objetos remotos
     * es por referencia y no por copia
     */
    wrapperSuma(ISumaResta calc, int a, int b) {
        return calc.suma(a, b);
    }
    /**
     * Realiza una suma
     *
     * throws NoSePudoCompletarLaOperacionException
     */
    public int sumar(int n1, int n2) throws NoSePudoCompletarLaOperacionException {
        try {
            ISumaResta calc = 
                (ISumaResta) Naming.lookup(this.getRname("+"));
            return this.wrapper_suma(calc, n1, n2);
        } catch (Exception e) {
            throw new NoSePudoCompletarLaOperacionException();
        }
    }

    /**
     * Realiza una resta
     *
     * throws NoSePudoCompletarLaOperacionException
     */
    public int restar(int n1, int n2) throws NoSePudoCompletarLaOperacionException {
        try {
            ISumaResta calc = 
                (ISumaResta) Naming.lookup(this.getRname("-"));
            return calc.resta(n1, n2);
        } catch (Exception e) {
            throw new NoSePudoCompletarLaOperacionException();
        }
    }

    /**
     * Realiza una multiplicación
     *
     * throws NoSePudoCompletarLaOperacionException
     */
    public int multiplicar(int n1, int n2) throws NoSePudoCompletarLaOperacionException {
        try {
            IMultiplicacionDivision calc = 
                (IMultiplicacionDivision) Naming.lookup(this.getRname("*"));
            return calc.multiplicacion(n1, n2);
        } catch (Exception e) {
            throw new NoSePudoCompletarLaOperacionException();
        }
    }

    /**
     * Realiza una división
     *
     * throws NoSePudoCompletarLaOperacionException
     */
    public int dividir(int n1, int n2) throws NoSePudoCompletarLaOperacionException {
        try {
            IMultiplicacionDivision calc = 
                (IMultiplicacionDivision) Naming.lookup(this.getRname("/"));
            return calc.division(n1, n2);
        } catch (Exception e) {
            throw new NoSePudoCompletarLaOperacionException();
        }
    }

    

    public String getHost() {
        return host;
    }
    public void setHost(String host) {
        this.host = host;
    }
}
