import static org.junit.jupiter.api.Assertions.assertEquals;

import at.aau.ProcessFile;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;
import java.util.Map;

public class MyTest {

    @Test
    void multiplicationOfZeroIntegersShouldReturnZero() {
        MyClass tester = new MyClass(); // MyClass is tested

        // assert statements
        assertEquals(0, tester.multiply(10, 0), "10 x 0 must be 0");
        assertEquals(0, tester.multiply(0, 10), "0 x 10 must be 0");
        assertEquals(0, tester.multiply(0, 0), "0 x 0 must be 0");
    }

    @Test
    void TestLib() {
        String javaLibPath = System.getProperty("java.library.path");
        Map<String, String> envVars = System.getenv();
        System.out.println(envVars.get("Path"));
        System.out.println(javaLibPath);
        for (String var : envVars.keySet()) {
            System.err.println("examining " + var + ":" + envVars.get(var));
            if (envVars.get(var).equals(javaLibPath)) {
                System.out.println(var);
            }
        }
    }

    @Test
    void TestProcessFile() {
        String input = "/Users/giovanni/repository/asserting/test-plugin/src/main/java/MyClass.java";
        String base = "/Users/giovanni/repository/asserting/test-plugin/";
        ProcessFile pf = new ProcessFile(Paths.get(input), base, new org.apache.maven.plugin.logging.Log() {
            @Override
            public boolean isDebugEnabled() {
                return false;
            }

            @Override
            public void debug(CharSequence charSequence) {

            }

            @Override
            public void debug(CharSequence charSequence, Throwable throwable) {

            }

            @Override
            public void debug(Throwable throwable) {

            }

            @Override
            public boolean isInfoEnabled() {
                return false;
            }

            @Override
            public void info(CharSequence charSequence) {

            }

            @Override
            public void info(CharSequence charSequence, Throwable throwable) {

            }

            @Override
            public void info(Throwable throwable) {

            }

            @Override
            public boolean isWarnEnabled() {
                return false;
            }

            @Override
            public void warn(CharSequence charSequence) {

            }

            @Override
            public void warn(CharSequence charSequence, Throwable throwable) {

            }

            @Override
            public void warn(Throwable throwable) {

            }

            @Override
            public boolean isErrorEnabled() {
                return false;
            }

            @Override
            public void error(CharSequence charSequence) {

            }

            @Override
            public void error(CharSequence charSequence, Throwable throwable) {

            }

            @Override
            public void error(Throwable throwable) {

            }
        });
    }
}