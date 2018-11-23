package at.aau.example;


import at.aau.ProcessFile;
import at.aau.model.MethodModel;
import org.apache.maven.plugin.logging.SystemStreamLog;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MyTest {

    @Test
    void TestLib() {
        System.out.println(System.getProperty("tomare"));
        System.out.println("==========");
//        String javaLibPath = System.getProperty("java.library.path");
//        System.out.println(System.getProperty("java.class.path"));
//        Map<String, String> envVars = System.getenv();
//        System.out.println(envVars.get("Path"));
//        System.out.println(javaLibPath);
//        for (String var : envVars.keySet()) {
//            System.err.println("examining " + var + ":" + envVars.get(var));
//            if (envVars.get(var).equals(javaLibPath)) {
//                System.out.println(var);
//            }
//        }
    }

    @Test
    void TestProcessFile() {
        String input = "/Users/giovanni/repository/asserting/test-plugin/src/main/java/at/aau/example/MyClass.java";
        String base = "/Users/giovanni/repository/asserting/test-plugin/";
        ProcessFile pf = new ProcessFile(Paths.get(input), base, new SystemStreamLog(), "");
        List<MethodModel> out = pf.getOutputFiles();
        assertEquals(2, out.size());
        System.out.println(out);
        System.out.println(out.get(0).getModel());
        System.out.println(out.get(1).getModel());
    }

    @Test
    void TestPoll() {
        MyClass c = new MyClass();
        c.poll(10);
    }
}