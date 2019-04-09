package at.aau.asserting;

import java.util.HashMap;
import java.util.Map;

public class AssertingException extends RuntimeException {

    private String validModel;
    Map<String,String> correctValue = new HashMap<>();

    public AssertingException(String formula, String message, String validModel) {
        super("Cannot validate " + formula + " \r\n -- counter example -- \r\n" + message);
        this.validModel = validModel;
        cleaning();
        parsing(this.validModel, this.correctValue);
    }

    private void cleaning() {
        this.validModel = validModel.replace("\r", "\n");
        this.validModel = validModel.replace("\n\n", "\n");
    }

    public static void parsing(String model, Map<String,String> save){
        String initModel = "(model";
        String initVar = "(define-fun";
        String endLine = ")\n";
        int init = model.indexOf(initModel) + initModel.length();
        String vars = model.substring(init);
        boolean more;
        do {
            int s = vars.indexOf(initVar) + initVar.length();
            int e = vars.indexOf(endLine, s);
            String v = vars.substring(s, e);
            String name = v.substring(0, v.indexOf("(")).trim();
            String val = v.substring(v.indexOf("\n")).trim();
            val = sanitize(val);
            save.put(name, val);
            vars = vars.substring(e + endLine.length());
            more = vars.contains(initVar);
        } while(more);
    }

    private static String sanitize(String val) {
        if(val.startsWith("(-")){
            val = "-" + val.substring(2, val.length()-1).trim();
        }
        return val;
    }

    public Map<String, String> getCorrectValue() {
        return correctValue;
    }

    public String getCorrectValue(String var) {
        return correctValue.get(var);
    }
    public int aCorrectValueFor(String var, int v) {
        int o;
        try {
            o = Integer.parseInt(getCorrectValue(var));
        } catch (Exception e){ o =  Integer.MAX_VALUE; }
        return o;
    }
    public long aCorrectValueFor(String var, long v) {
        long o;
        try {
            o = Long.parseLong(getCorrectValue(var));
        } catch (Exception e){ o = Long.MAX_VALUE; }
        return o;
    }
}
