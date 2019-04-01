package at.aau;

public class Options {

    private static Options instance;

    private boolean isMathEnable = false;
    private boolean isTimeEnable = false;
    private boolean isRecoveryEnable = false;

    private Options(){}

    private static Options getInstance(){
        if(instance == null){
            synchronized (Options.class) {
                if(instance == null){
                    instance = new Options();
                }
            }
        }
        return instance;
    }

    public static boolean isMathEnabled(){
        return getInstance().isMathEnable;
    }
    public static void setMathEnabled(boolean f){
        getInstance().isMathEnable = f;
    }
    public static boolean isRecoveryEnabled(){
        return getInstance().isRecoveryEnable;
    }
    public static void setRecoveryEnabled(boolean f){
        getInstance().isRecoveryEnable = f;
    }
    public static boolean isTimeEnabled(){
        return getInstance().isTimeEnable;
    }
    public static void setTimeEnabled(boolean f){
        getInstance().isTimeEnable = f;
    }
}