package at.aau;

import at.aau.intermediateModel.structure.ASTClass;
import at.aau.intermediateModel.visitors.creation.JDTVisitor;
import at.aau.recovery.CorrectionPoint;
import at.aau.recovery.Recovery;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;

public class TestRecovery {

    @Test
    public void CollectRightPoint() {
        String input = "/Users/giovanni/repository/asserting/test-plugin/src/main/java/at/aau/example/MyClass.java";
        String basedir = "/Users/giovanni/repository/asserting/test-plugin/src/main/java/";
        String classpath = ":/Users/giovanni/.m2/repository/org/junit/jupiter/junit-jupiter-api/5.2.0/junit-jupiter-api-5.2.0.jar:/Users/giovanni/.m2/repository/org/junit/jupiter/junit-jupiter-params/5.2.0/junit-jupiter-params-5.2.0.jar:/Users/giovanni/.m2/repository/org/junit/jupiter/junit-jupiter-engine/5.2.0/junit-jupiter-engine-5.2.0.jar:/Users/giovanni/.m2/repository/org/junit/vintage/junit-vintage-engine/5.2.0/junit-vintage-engine-5.2.0.jar:/Users/giovanni/.m2/repository/org/junit/platform/junit-platform-launcher/1.2.0/junit-platform-launcher-1.2.0.jar:/Users/giovanni/.m2/repository/org/junit/platform/junit-platform-runner/1.2.0/junit-platform-runner-1.2.0.jar:/Users/giovanni/.m2/repository/org/apache/maven/maven-plugin-api/3.6.0/maven-plugin-api-3.6.0.jar:/Users/giovanni/.m2/repository/org/apache/maven/plugin-tools/maven-plugin-annotations/3.6.0/maven-plugin-annotations-3.6.0.jar:/Users/giovanni/.m2/repository/org/apache/maven/shared/file-management/3.0.0/file-management-3.0.0.jar:/Users/giovanni/.m2/repository/javax/inject/javax.inject/1/javax.inject-1.jar:/Users/giovanni/.m2/repository/commons-io/commons-io/2.4/commons-io-2.4.jar:/Users/giovanni/.m2/repository/org/eclipse/jdt/org.eclipse.jdt.core/3.12.2/org.eclipse.jdt.core-3.12.2.jar:/Users/giovanni/.m2/repository/uk/com/robust-it/cloning/1.9.3/cloning-1.9.3.jar:/Users/giovanni/.m2/repository/com/microsoft/z3/4.8.3/z3-4.8.3.jar:/Users/giovanni/.m2/repository/at/aau/assert-maven-plugin/1.0.1/assert-maven-plugin-1.0.1.jar:/Users/giovanni/.m2/repository/at/aau/asserting-library/1.0-SNAPSHOT/asserting-library-1.0-SNAPSHOT.jar";
        List<ASTClass> cs = JDTVisitor.parse(input, basedir, classpath);
        for (ASTClass c : cs) {
            List<CorrectionPoint> rec = Recovery.findPoint(c);
            assertEquals(rec.size(), 2);
        }
    }

    @Test
    public void CorrectRightPoint() {
        String input = "/Users/giovanni/repository/asserting/test-plugin/src/main/java/at/aau/example/MyClass.java";
        String basedir = "/Users/giovanni/repository/asserting/test-plugin/src/main/java/";
        String classpath = ":/Users/giovanni/.m2/repository/org/junit/jupiter/junit-jupiter-api/5.2.0/junit-jupiter-api-5.2.0.jar:/Users/giovanni/.m2/repository/org/junit/jupiter/junit-jupiter-params/5.2.0/junit-jupiter-params-5.2.0.jar:/Users/giovanni/.m2/repository/org/junit/jupiter/junit-jupiter-engine/5.2.0/junit-jupiter-engine-5.2.0.jar:/Users/giovanni/.m2/repository/org/junit/vintage/junit-vintage-engine/5.2.0/junit-vintage-engine-5.2.0.jar:/Users/giovanni/.m2/repository/org/junit/platform/junit-platform-launcher/1.2.0/junit-platform-launcher-1.2.0.jar:/Users/giovanni/.m2/repository/org/junit/platform/junit-platform-runner/1.2.0/junit-platform-runner-1.2.0.jar:/Users/giovanni/.m2/repository/org/apache/maven/maven-plugin-api/3.6.0/maven-plugin-api-3.6.0.jar:/Users/giovanni/.m2/repository/org/apache/maven/plugin-tools/maven-plugin-annotations/3.6.0/maven-plugin-annotations-3.6.0.jar:/Users/giovanni/.m2/repository/org/apache/maven/shared/file-management/3.0.0/file-management-3.0.0.jar:/Users/giovanni/.m2/repository/javax/inject/javax.inject/1/javax.inject-1.jar:/Users/giovanni/.m2/repository/commons-io/commons-io/2.4/commons-io-2.4.jar:/Users/giovanni/.m2/repository/org/eclipse/jdt/org.eclipse.jdt.core/3.12.2/org.eclipse.jdt.core-3.12.2.jar:/Users/giovanni/.m2/repository/uk/com/robust-it/cloning/1.9.3/cloning-1.9.3.jar:/Users/giovanni/.m2/repository/com/microsoft/z3/4.8.3/z3-4.8.3.jar:/Users/giovanni/.m2/repository/at/aau/assert-maven-plugin/1.0.1/assert-maven-plugin-1.0.1.jar:/Users/giovanni/.m2/repository/at/aau/asserting-library/1.0-SNAPSHOT/asserting-library-1.0-SNAPSHOT.jar";
        List<ASTClass> cs = JDTVisitor.parse(input, basedir, classpath);
        List<CorrectionPoint> rec = new ArrayList<>();
        for (ASTClass c : cs) {
            rec.addAll(Recovery.findPoint(c));
        }
        Recovery.recoverFile(input, rec);
    }
}
