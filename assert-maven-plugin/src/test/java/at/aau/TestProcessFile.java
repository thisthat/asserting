package at.aau;

import org.apache.maven.monitor.logging.DefaultLog;
import org.apache.maven.plugin.logging.Log;
import org.codehaus.plexus.logging.console.ConsoleLogger;
import org.junit.Test;

import java.nio.file.Paths;
import java.util.logging.Logger;

public class TestProcessFile {
    @Test
    public void SimpleTest() throws Exception {
        ProcessFile pf = new ProcessFile(
                Paths.get("/Users/giovanni/repository/benchmark/manual/12/approach/src/main/java/MyClass.java"),
                "/Users/giovanni/repository/benchmark/manual/12/approach",
                new DefaultLog(new ConsoleLogger()),
                ""
        );
    }
}
