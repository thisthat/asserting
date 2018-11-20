package at.aau.intermediateModelHelper;

public class Config {
    public static boolean debug = false;

    public static boolean isDebug() {
        return debug;
    }

    public static void setDebug(boolean debug) {
        Config.debug = debug;
    }
}
