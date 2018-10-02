package cliente;

import utils.Timer;
import exceptions.ClienteException;

public class ActualizadorDeriva extends Thread {

    
    public static final int FIRST_RANGE = 5000;
    public static final int SECOND_RANGE = FIRST_RANGE * 2;
    public static final int THIRD_RANGE = FIRST_RANGE * 3;

    public static final int FIRST_RANGE_ADJUSTMENT = 500;
    public static final int SECOND_RANGE_ADJUSTMENT = FIRST_RANGE_ADJUSTMENT * 2;
    public static final int THIRD_RANGE_ADJUSTMENT = FIRST_RANGE_ADJUSTMENT * 3;

    public static final int SYNC_PERIOD = 1000;

    private Cliente cliente;
    private int requests;

    public ActualizadorDeriva(Cliente cliente, int requests) {
        this.cliente = cliente;
        this.requests = requests;
    }

    public void run() {
        Timer timer = new Timer();
        long[] patternTimes = new long[this.requests];
        long[] localTimes = new long[this.requests];
        long[] rtts = new long[this.requests];


        while (true) {
            try {
                for (int i = 0; i < this.requests; i++) {
                    try {
                        timer.start();
                        patternTimes[i] = this.cliente.getPatternTime();
                        rtts[i] = timer.getPartial();
                        localTimes[i] = this.cliente.getTime();
                    } catch (ClienteException e) {
                        e.printStackTrace();
                    }
                }
                //Conseguimos el índice del RTT más corto
                int indexOfMinRTT = this.getIndexOfMinValue(rtts);
                //Ahora el tiempo del servidor que llegó más rápido
                //(el que tuvo el RTT más corto)
                long fastestPatternTime = patternTimes[indexOfMinRTT]; 
                //Y el RTT más corto
                long shortestRTT = rtts[indexOfMinRTT];

                //Estos serían el C'(T4) y el C(T4) 
                //según el algoritmo de Cristian, respectivamente
                long adjustedPatternTime = fastestPatternTime + shortestRTT/2;
                long localTime = localTimes[indexOfMinRTT];

                this.adjustClock(localTime, adjustedPatternTime);

                //Esperar hasta la próxima sincronización
                Thread.sleep(SYNC_PERIOD);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public int getIndexOfMinValue(long[] arr) {
        long minVal = arr[0];
        int minIndex = 0;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] < minVal) {
                minVal = arr[i];
                minIndex = i;
            }
        }

        return minIndex;
    }

    public void adjustClock(long localTime, long patternTime) {
        //Si offset > 0, el reloj local va atrasado => Aumentar la deriva
        //Si offset < 0, el reloj local va adelantado => Reducir la deriva
        long offset = patternTime - localTime;

        int driftAdjustment = this.getClockDriftAdjustment(offset);

        this.cliente.adjustDrift(driftAdjustment);
    }

    public int getClockDriftAdjustment(long offset) {
        if (offset > THIRD_RANGE) {
            return -THIRD_RANGE_ADJUSTMENT;
        }
        if (offset > SECOND_RANGE) {
            return -SECOND_RANGE_ADJUSTMENT;
        }
        if (offset > FIRST_RANGE) {
            return -FIRST_RANGE_ADJUSTMENT;
        }

        if (offset > -FIRST_RANGE) {
            return FIRST_RANGE_ADJUSTMENT;
        }
        if (offset > -SECOND_RANGE) {
            return SECOND_RANGE_ADJUSTMENT;
        }

        return THIRD_RANGE_ADJUSTMENT;
    }
}
