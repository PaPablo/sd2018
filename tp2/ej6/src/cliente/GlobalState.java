package cliente;

import java.util.HashSet;
import java.util.Set;

public class GlobalState {
    public static Set<String> env = new HashSet<String>();

    public static void addEnv(String var) {
        env.add(var);
    }

    public static boolean hasEnv(String var) {
        return env.contains(var);
    }

    public static boolean isDebug() {
        return hasEnv("debug");
    }
}
