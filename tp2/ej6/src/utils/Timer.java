package utils;

public class Timer {

    private long _start;
    private long _stop;

    public Timer() { }

    public void start() {
        this._start = System.currentTimeMillis();
    }

    public long getPartial() {
        return System.currentTimeMillis() - this._start;
    }

    public long stop() {
        this._stop = System.currentTimeMillis();
        return this._stop;
    } 
}
