package utils;

import reloj.IRelojListener;

public class Logger implements IRelojListener {

    public void updateClock(String time) {
        System.out.println(String.format(
                    "[LOGGER] La hora es %s",
                    time
                    ));
    }

}
