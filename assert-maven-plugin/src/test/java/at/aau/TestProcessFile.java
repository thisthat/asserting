package at.aau;

import org.apache.maven.monitor.logging.DefaultLog;
import org.codehaus.plexus.logging.console.ConsoleLogger;
import org.junit.Test;

import java.nio.file.Paths;

public class TestProcessFile {
    @Test
    public void SimpleTest() throws Exception {
        Options.setTimeout(10000);
        Options.setRecoveryEnabled(false);
        Options.setTimeEnabled(true);
        ProcessFile pf = new ProcessFile(
                Paths.get("/Users/giovanni/repository/projects/repos/activemq/activemq-jdbc-store/src/main/java/org/apache/activemq/store/jdbc/LeaseDatabaseLocker.java").toAbsolutePath(),
                "/Users/giovanni/repository/projects/repos/activemq/activemq-jdbc-store",
                new DefaultLog(new ConsoleLogger()),
                ":/Users/giovanni/.m2/repository/org/apache/activemq/activemq-broker/5.16.0-SNAPSHOT/activemq-broker-5.16.0-SNAPSHOT.jar:/Users/giovanni/.m2/repository/org/apache/derby/derby/10.11.1.1/derby-10.11.1.1.jar:/Users/giovanni/.m2/repository/org/apache/activemq/activeio-core/3.1.4/activeio-core-3.1.4.jar:/Users/giovanni/.m2/repository/at/aau/asserting-library/1.0-SNAPSHOT/asserting-library-1.0-SNAPSHOT.jar"
        );
        System.out.println(pf.getOutputFiles().size());
    }
}
