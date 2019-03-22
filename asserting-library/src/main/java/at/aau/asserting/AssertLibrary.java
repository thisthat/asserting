package at.aau.asserting;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Map;

import at.aau.asserting.smt.Z3Connector;
import org.apache.commons.io.IOUtils;

public class AssertLibrary {
    public static void assertFormula(Formula f){
        String caller = getCaller();
        if(!passFilters(caller)){
            return;
        }
        String modelName = getModelName();
        String model = loadModel(modelName);
        if(model.length() == 0){
            return;
        }
        Z3Connector z3 = new Z3Connector(model);
        z3.check(f);
    }

    private static String getModelName() {
        StackTraceElement ste = Thread.currentThread().getStackTrace()[3];
        return ste.getClassName() + "_" + ste.getMethodName() + "_" + ste.getLineNumber() + ".smt";
    }

    private static String getCaller(){
        StackTraceElement[] ste = Thread.currentThread().getStackTrace();
        if(ste.length > 4){
            return ste[3].getClassName();
        }
        return "";
    }

    private static boolean passFilters(String caller) {
        String filters = getFiltersProperty();
        if(filters == null)
            return false;
        for(String f : filters.split(":")){
            if(caller.matches(f))
                return true;
        }
        return false;
    }

    private static String getFiltersProperty() {
        String f = System.getProperty("asserting.filters");
        if(f != null){
            return f;
        }
        f = System.getProperty("af");
        if(f != null){
            return f;
        }
        Map<String,String> env = System.getenv();
        if(env.containsKey("asserting.filters")){
            return env.get("asserting.filters");
        }
        if(env.containsKey("af")){
            return env.get("af");
        }
        return null;
    }

    private static String loadModel(String modelName){
        try {
            InputStream s = AssertLibrary.class.getClassLoader().getResourceAsStream(modelName);
            return IOUtils.toString(s, Charset.defaultCharset());
        } catch (Exception ex){
            System.err.println("File " + modelName + " not found!");
        }
        return "";
    }
}
