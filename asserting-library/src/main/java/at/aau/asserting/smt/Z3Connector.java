package at.aau.asserting.smt;

import at.aau.asserting.AssertingException;
import at.aau.asserting.Formula;
import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class Z3Connector {

    String model;

    public Z3Connector(String model) {
        this.model = model;
    }

    public void check(Formula formula){
        //System.out.println("-=-=-=-=-=-=-=--=-");
        //System.out.println("formula: " + formula);
        //System.out.println("-=-=-=-=-=-=-=--=-");
        //System.out.println("Fed into Z3");
        String fed = this.model + formula;
        String model = "";
        try {
            File tmp = File.createTempFile("asserting-runtime-lib", "smt");
            tmp.deleteOnExit();
            FileUtils.write(tmp, fed, "UTF-8");
            model = run(tmp.getAbsolutePath());
        } catch (Exception ex){
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        }
        checkResult(model, formula.pretty());
    }

    private void checkResult(String result, String formula) {
        if(result.startsWith("sat")){
            String counter = result.substring(3);
            throw new AssertingException(formula, counter);
        }
    }

    public static String run(String file) throws IOException {

        // A Runtime object has methods for dealing with the OS
        Runtime r = Runtime.getRuntime();
        Process p;     // Process tracks one external native process
        BufferedReader is;  // reader for output of process
        String line;

        // Our argv[0] contains the program to run; remaining elements
        // of argv contain args for the target program. This is just
        // what is needed for the String[] form of exec.
        p = r.exec("z3 -smt2 " + file);


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
            System.err.println(e);
            e.printStackTrace();
        }
        return sb.toString();
    }
}
