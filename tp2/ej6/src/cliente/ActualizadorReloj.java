package cliente;

import java.util.ArrayList;

import reloj.IRelojListener;
import exceptions.ClienteException;
import utils.DateFormatter;

public class ActualizadorReloj extends Thread {
    
    private Cliente cliente;
    private ArrayList<IRelojListener> relojListeners;

    public ActualizadorReloj(Cliente cliente) {
        this.cliente = cliente;
        this.relojListeners = new ArrayList<IRelojListener>();
    }
    
    public void addListener(IRelojListener listener) {
        this.relojListeners.add(listener);
    }

    public void run() {
        try {
            long milis;
            String time;
            while(true) {
                milis = this.cliente.getTime();
                time = DateFormatter.getTimeFromMillis(milis);
                for (IRelojListener listener : this.relojListeners) {
                    listener.updateClock(time);
                }

                long sleep = this.cliente.getUpdateTime();
                //Esperamos el tiempo que indique la deriva del reloj
                if (GlobalState.isDebug()) {
                    System.out.println(String.format(
                                "[DEBUG] ActualizadorReloj - me voy a dormir [%d]",
                                sleep
                                ));
                }
                Thread.sleep(sleep);
                //Actualizamos reloj
                this.cliente.tick();
            }
        } catch (InterruptedException
                | ClienteException e) {
            e.printStackTrace();
        }
    }
}
