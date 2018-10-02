package utils;

public class DateFormatter {
    
    public static String getTimeFromMillis(long milis) {
        /*
         * Taken from here:
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
}
