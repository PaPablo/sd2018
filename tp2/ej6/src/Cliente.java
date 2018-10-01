import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;

public class Cliente {

    public Cliente() {

    }

    public String getHoraFromMillis(long milis) {
        /*
         * Take from here:
         * https://stackoverflow.com/questions/4142313/java-convert-milliseconds-to-time-format
         * */
        long _millis = milis % 1000;
        long _second = (milis / 1000) % 60;
        long _minute = (milis / (1000 * 60)) % 60;
        long _hour = (milis / (1000 * 60 * 60)) % 24;

        String time = String.format(
                "%02d:%02d:%02d.%d", 
                _hour, 
                _minute, 
                _second, 
                _millis);
        return time;
    }

    public long getHora() {
        try {
            IReloj reloj = 
                (IReloj) Naming.lookup(this.getRelojRname());
            long horaMilis = reloj.now();

            System.out.println(String.format(
                        "[CLIENTE] La hora es [%s]",
                        this.getHoraFromMillis(horaMilis)));

            return horaMilis;
        } catch (RemoteException 
                | NotBoundException
                | MalformedURLException e) {
            System.out.println(String.format(
                        "No se pudo completar la operación [%s]",
                        e));
            return -1;
        }
    }

    public String getRelojRname() {
        return String.format(
                "//localhost:%d/Reloj",
                Registry.REGISTRY_PORT);
    }
}
