package at.aau;

import at.aau.model.MethodModel;
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

	@Test
	public void Test1() throws Exception {
		Options.setTimeout(10000);
		Options.setRecoveryEnabled(false);
		Options.setTimeEnabled(true);
		String cp = ":/Users/giovanni/.m2/repository/org/slf4j/slf4j-api/1.7.25/slf4j-api-1.7.25.jar:/Users/giovanni/.m2/repository/org/apache/airavata/airavata-gfac-core/0.17-SNAPSHOT/airavata-gfac-core-0.17-SNAPSHOT.jar:/Users/giovanni/.m2/repository/org/jglobus/gss/2.1.0/gss-2.1.0.jar:/Users/giovanni/.m2/repository/org/jglobus/myproxy/2.1.0/myproxy-2.1.0.jar:/Users/giovanni/.m2/repository/com/jcraft/jsch/0.1.53/jsch-0.1.53.jar:/Users/giovanni/.m2/repository/net/schmizz/sshj/0.6.1/sshj-0.6.1.jar:/Users/giovanni/.m2/repository/ch/ethz/ganymed/ganymed-ssh2/262/ganymed-ssh2-262.jar:/Users/giovanni/.m2/repository/com/fasterxml/jackson/core/jackson-databind/2.4.4/jackson-databind-2.4.4.jar:/Users/giovanni/.m2/repository/eu/unicore/unicore-client-wrapper/1.7.2_1/unicore-client-wrapper-1.7.2_1.jar:/Users/giovanni/.m2/repository/net/sf/saxon/Saxon-HE/9.6.0-1/Saxon-HE-9.6.0-1.jar:/Users/giovanni/.m2/repository/commons-httpclient/commons-httpclient/3.1/commons-httpclient-3.1.jar:/Users/giovanni/.m2/repository/org/apache/airavata/aurora-client/0.17-SNAPSHOT/aurora-client-0.17-SNAPSHOT.jar";
		ProcessFile pf = new ProcessFile(
			Paths.get("/Users/giovanni/repository/projects/repos/airavata/modules/gfac/gfac-impl/src/main/java/org/apache/airavata/gfac/impl/watcher/CancelRequestWatcherImpl.java").toAbsolutePath(),
			"/Users/giovanni/repository/projects/repos/airavata/modules/gfac/gfac-impl/",
			new DefaultLog(new ConsoleLogger()),
			cp
		);
		System.out.println(pf.getOutputFiles().size());
	}

	@Test
	public void Test2() throws Exception {
		Options.setTimeout(10000);
		Options.setRecoveryEnabled(false);
		Options.setTimeEnabled(true);
		Options.setMathEnabled(true);
		String cp = ":/Users/giovanni/.m2/repository/at/aau/asserting-library/1.0-SNAPSHOT/asserting-library-1.0-SNAPSHOT.jar:/Users/giovanni/.m2/repository/org/powermock/powermock-module-junit4/1.7.1/powermock-module-junit4-1.7.1.jar:/Users/giovanni/.m2/repository/org/powermock/powermock-api-mockito/1.7.1/powermock-api-mockito-1.7.1.jar:/Users/giovanni/.m2/repository/org/apache/hadoop/hadoop-annotations/3.0.0-beta1-SNAPSHOT/hadoop-annotations-3.0.0-beta1-SNAPSHOT.jar:/Users/giovanni/.m2/repository/org/apache/hadoop/hadoop-auth/3.0.0-beta1-SNAPSHOT/hadoop-auth-3.0.0-beta1-SNAPSHOT.jar:/Users/giovanni/.m2/repository/org/apache/hadoop/hadoop-common/3.0.0-beta1-SNAPSHOT/hadoop-common-3.0.0-beta1-SNAPSHOT.jar:/Users/giovanni/.m2/repository/org/apache/hadoop/hadoop-common/3.0.0-beta1-SNAPSHOT/hadoop-common-3.0.0-beta1-SNAPSHOT-tests.jar:/Users/giovanni/.m2/repository/org/apache/hadoop/hadoop-hdfs-client/3.0.0-beta1-SNAPSHOT/hadoop-hdfs-client-3.0.0-beta1-SNAPSHOT.jar:/Users/giovanni/.m2/repository/org/apache/zookeeper/zookeeper/3.4.9/zookeeper-3.4.9-tests.jar:/Users/giovanni/.m2/repository/com/google/guava/guava/11.0.2/guava-11.0.2.jar:/Users/giovanni/.m2/repository/org/eclipse/jetty/jetty-server/9.3.11.v20160721/jetty-server-9.3.11.v20160721.jar:/Users/giovanni/.m2/repository/org/eclipse/jetty/jetty-util/9.3.11.v20160721/jetty-util-9.3.11.v20160721.jar:/Users/giovanni/.m2/repository/org/eclipse/jetty/jetty-util-ajax/9.3.11.v20160721/jetty-util-ajax-9.3.11.v20160721.jar:/Users/giovanni/.m2/repository/com/sun/jersey/jersey-core/1.19/jersey-core-1.19.jar:/Users/giovanni/.m2/repository/com/sun/jersey/jersey-server/1.19/jersey-server-1.19.jar:/Users/giovanni/.m2/repository/commons-cli/commons-cli/1.2/commons-cli-1.2.jar:/Users/giovanni/.m2/repository/commons-codec/commons-codec/1.4/commons-codec-1.4.jar:/Users/giovanni/.m2/repository/commons-io/commons-io/2.4/commons-io-2.4.jar:/Users/giovanni/.m2/repository/commons-lang/commons-lang/2.6/commons-lang-2.6.jar:/Users/giovanni/.m2/repository/commons-logging/commons-logging/1.1.3/commons-logging-1.1.3.jar:/Users/giovanni/.m2/repository/commons-daemon/commons-daemon/1.0.13/commons-daemon-1.0.13.jar:/Users/giovanni/.m2/repository/log4j/log4j/1.2.17/log4j-1.2.17.jar:/Users/giovanni/.m2/repository/com/google/protobuf/protobuf-java/2.5.0/protobuf-java-2.5.0.jar:/Users/giovanni/.m2/repository/javax/servlet/javax.servlet-api/3.1.0/javax.servlet-api-3.1.0.jar:/Users/giovanni/.m2/repository/junit/junit/4.11/junit-4.11.jar:/Users/giovanni/.m2/repository/org/apache/hadoop/hadoop-minikdc/3.0.0-beta1-SNAPSHOT/hadoop-minikdc-3.0.0-beta1-SNAPSHOT.jar:/Users/giovanni/.m2/repository/org/mockito/mockito-all/1.8.5/mockito-all-1.8.5.jar:/Users/giovanni/.m2/repository/org/slf4j/slf4j-log4j12/1.7.25/slf4j-log4j12-1.7.25.jar:/Users/giovanni/.m2/repository/io/netty/netty/3.10.5.Final/netty-3.10.5.Final.jar:/Users/giovanni/.m2/repository/io/netty/netty-all/4.0.23.Final/netty-all-4.0.23.Final.jar:/Users/giovanni/.m2/repository/xerces/xercesImpl/2.9.1/xercesImpl-2.9.1.jar:/Users/giovanni/.m2/repository/org/apache/htrace/htrace-core4/4.1.0-incubating/htrace-core4-4.1.0-incubating.jar:/Users/giovanni/.m2/repository/org/apache/hadoop/hadoop-kms/3.0.0-beta1-SNAPSHOT/hadoop-kms-3.0.0-beta1-SNAPSHOT.jar:/Users/giovanni/.m2/repository/org/apache/hadoop/hadoop-kms/3.0.0-beta1-SNAPSHOT/hadoop-kms-3.0.0-beta1-SNAPSHOT-tests.jar:/Users/giovanni/.m2/repository/org/fusesource/leveldbjni/leveldbjni-all/1.8/leveldbjni-all-1.8.jar:/Users/giovanni/.m2/repository/org/bouncycastle/bcprov-jdk16/1.46/bcprov-jdk16-1.46.jar:/Users/giovanni/.m2/repository/com/fasterxml/jackson/core/jackson-databind/2.7.8/jackson-databind-2.7.8.jar";
		ProcessFile pf = new ProcessFile(
			Paths.get("/Users/giovanni/repository/projects/repos/neo4j/community/kernel/src/main/java/org/neo4j/kernel/AvailabilityGuard.java").toAbsolutePath(),
			"/Users/giovanni/repository/projects/repos/neo4j/community/kernel",
			new DefaultLog(new ConsoleLogger()),
			cp
		);
		System.out.println(pf.getOutputFiles().size());
		for(MethodModel m : pf.getOutputFiles()){
			System.out.println(m.getModel());
		}
	}
}
