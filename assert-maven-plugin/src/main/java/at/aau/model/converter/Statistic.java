package at.aau.model.converter;

public class Statistic {
    static long nMethod = 0;
    static long nMethodPath = 0;

    public static void incrementNMethod(long n){
        nMethod += n;
    }
    public static void incrementNMethodPath(long n){
        nMethodPath += n;
    }

    public static long getnMethod() {
        return nMethod;
    }

    public static long getnMethodPath() {
        return nMethodPath;
    }
}
