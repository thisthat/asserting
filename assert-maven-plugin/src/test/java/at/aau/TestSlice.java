package at.aau;

import at.aau.intermediateModel.interfaces.IASTMethod;
import at.aau.intermediateModel.structure.ASTClass;
import at.aau.intermediateModel.visitors.creation.JDTVisitor;
import at.aau.model.slicing.Slice;
import at.aau.model.slicing.TimeElement;
import at.aau.model.slicing.TimeStatements;
import at.aau.model.slicing.model.Method;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class TestSlice {

    @Before
    public void setUp() throws Exception {
        Options.setMathEnabled(true);
    }

    @Test
    public void SimpleTest() throws Exception {
        Path current = Paths.get(System.getenv("PWD"));
        String base = current.getParent().toString();
        String file = base +  "/test-plugin/src/main/java/at/aau/example/MyClass.java";
        List<ASTClass> cs = JDTVisitor.parse(file, base, "");
        HashMap<IASTMethod, Method> s = Slice.slice(cs.get(0));
        List<TimeElement> te = TimeStatements.getInstance().getStms();
        boolean flag = false;
        for(TimeElement t : te){
            if(t.getStm().getCode().equals("long t0 = 100;")){
                flag = true;
            }
        }
        assertTrue(flag);
    }
}
