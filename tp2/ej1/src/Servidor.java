/*
 * Javier Abell�n. Servidor.java
 *
 * Created on 2 de abril de 2004, 19:15
 */

import java.rmi.Naming;                    /* lookup         */
import java.rmi.registry.Registry;         /* REGISTRY_PORT  */

/**
 * Servidor para el ejemplo de RMI.
 * Exporte un metodo para sumar dos enteros y devuelve el resultado.
 */
public class Servidor 
{
    
    /** Crea nueva instancia de Servidor rmi */
    public Servidor() {
        try {
		// Se indica a rmiregistry d�nde est�n las clases.
		// Cambiar el paht al sitio en el que est�. Es importante
		// mantener la barra al final.
	/*	System.setProperty(
			"java.rmi.server.codebase",
			"file:/D:/users/javier/java/rmi_simple/src_servidor/");
	*/	
            // Se publica el objeto remoto
            ISumaResta sumaresta = new SumaResta();
            IMultiplicacionDivision multidiv = new MultiplicacionDivision();
            String sumaname = "//localhost:" + Registry.REGISTRY_PORT  + "/SumaResta";
            String mname = "//localhost:" + Registry.REGISTRY_PORT  + "/MultiplicacionDivision";
            Naming.rebind (sumaname, sumaresta);
            Naming.rebind (mname, multidiv);
            
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new Servidor();
    }
}
