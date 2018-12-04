package at.aau.asserting.smt;

import at.aau.asserting.AssertingException;
import at.aau.asserting.Formula;
import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Z3Connector {

    String model;

    public Z3Connector(String model) {
        this.model = model;
    }

    public void check(Formula formula){
        String fed = this.model + formula.prepare();
        String counter = null;
        try {
            counter = run(createTmpFile(fed));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(counter != null)
            checkResult(counter, formula);
    }

    private void checkResult(String result, Formula formula) {
        //System.out.println("Result: " + result.substring(0, result.indexOf('\n') > 0 ? result.indexOf('/') : result.length()));
        if(result.startsWith("sat")){
            String counter = result.substring(3);
            String goodModel = validModel(formula);
            System.out.println(goodModel);
            throw new AssertingException(formula.pretty(), counter);
        } else {
            //check overflows
            String f = this.model + formula.prepare(false);
            String r = "";
            try {
                r = run(createTmpFile(f));
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(r != null && r.length() > 0){
                Map<String, String> vars = new HashMap<>();
                List<String> over = new ArrayList<>();
                AssertingException.parsing(r, vars);
                for(String var : formula.getVar()){
                    String val = vars.get(var);
                    if(val != null && val.equals("9223372036854775808")){
                        over.add(var);
                    }
                }
                String[] model = this.model.split("\n");
                StringBuilder newModel = new StringBuilder();
                for(String line : model){
                    if(line.contains("(declare")){
                        newModel.append(line).append("\n");
                        continue;
                    }
                    boolean add = true;
                    for(String v : over){
                        if(line.contains("(assert (= " + v)){
                            add = false;
                        }
                    }
                    if(add)
                        newModel.append(line).append("\n");
                }
                for(String v : over){
                    newModel.append("(assert (= -9223372036854775808 " + v + "))").append("\n");
                }
                newModel.append(formula.noQuantifier().prepare());
                try {
                    result = run(createTmpFile(newModel.toString()));
                    if(result.startsWith("sat")){
                        String counter = result.substring(3);
                        String goodModel = validModel(formula);
                        System.out.println(goodModel);
                        throw new AssertingException(formula.pretty(), counter);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String createTmpFile(String data){
        try {
            File tmp = File.createTempFile("asserting-runtime-lib", ".smt");
            tmp.deleteOnExit();
            FileUtils.write(tmp, data, "UTF-8");
            return tmp.getAbsolutePath();
        } catch (Exception ex){
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        }
        return null;
    }

    private String validModel(Formula f){
        String m = this.model + f.noQuantifier().prepare(false);
        String valid = "";
        try {
            valid = run(createTmpFile(m));
        } catch (Exception ex){
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        }
        return valid;
    }

    public String run(String file) throws IOException {

        // A Runtime object has methods for dealing with the OS
        Runtime r = Runtime.getRuntime();
        Process p;     // Process tracks one external native process
        BufferedReader is;  // reader for output of process
        String line;

        // Our argv[0] contains the program to run; remaining elements
        // of argv contain args for the target program. This is just
        // what is needed for the String[] form of exec.
        String cmd = "z3 -smt2 " + file;
        //System.out.println(cmd);
        p = r.exec(cmd);


        // getInputStream gives an Input stream connected to
        // the process p's standard output. Just use it to make
        // a BufferedReader to readLine() what the program writes out.
        StringBuilder sb = new StringBuilder();
        is = new BufferedReader(new InputStreamReader(p.getInputStream()));
        while ((line = is.readLine()) != null)
            sb.append(line).append("\n");
        try {
            p.waitFor();  // wait for process to complete
        } catch (InterruptedException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        //System.out.println(sb.toString());
        return sb.toString();
    }
}
