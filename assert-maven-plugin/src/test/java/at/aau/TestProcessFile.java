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
                Paths.get("/Users/giovanni/repository/projects/repos/activemq/activemq-broker/src/main/java/org/apache/activemq/transport/vm/VMTransportFactory.java").toAbsolutePath(),
                "/Users/giovanni/repository/projects/repos/activemq/activemq-broker/",
                new DefaultLog(new ConsoleLogger()),
                ":/Users/giovanni/.m2/repository/org/apache/activemq/activemq-client/5.16.0-SNAPSHOT/activemq-client-5.16.0-SNAPSHOT.jar:/Users/giovanni/.m2/repository/org/apache/activemq/activemq-openwire-legacy/5.16.0-SNAPSHOT/activemq-openwire-legacy-5.16.0-SNAPSHOT.jar:/Users/giovanni/.m2/repository/org/osgi/org.osgi.core/4.3.1/org.osgi.core-4.3.1.jar:/Users/giovanni/.m2/repository/org/apache/activemq/activemq-jaas/5.16.0-SNAPSHOT/activemq-jaas-5.16.0-SNAPSHOT.jar:/Users/giovanni/.m2/repository/xalan/xalan/2.7.2/xalan-2.7.2.jar:/Users/giovanni/.m2/repository/junit/junit/4.12/junit-4.12.jar:/Users/giovanni/.m2/repository/org/slf4j/slf4j-log4j12/1.7.25/slf4j-log4j12-1.7.25.jar:/Users/giovanni/.m2/repository/com/google/guava/guava/18.0/guava-18.0.jar:/Users/giovanni/.m2/repository/com/fasterxml/jackson/core/jackson-databind/2.6.7/jackson-databind-2.6.7.jar:/Users/giovanni/.m2/repository/org/powermock/powermock-module-junit4/1.6.5/powermock-module-junit4-1.6.5.jar:/Users/giovanni/.m2/repository/org/mockito/mockito-core/1.10.19/mockito-core-1.10.19.jar:/Users/giovanni/.m2/repository/org/powermock/powermock-api-mockito/1.6.5/powermock-api-mockito-1.6.5.jar:/Users/giovanni/.m2/repository/at/aau/asserting-library/1.0-SNAPSHOT/asserting-library-1.0-SNAPSHOT.jar"
        );
        System.out.println(pf.getOutputFiles().size());
    }
}
