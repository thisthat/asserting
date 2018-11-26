import at.aau.asserting.Formula;
import at.aau.asserting.logics.FOLConstructors.Const;
import at.aau.asserting.logics.FOLConstructors.Variable;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static at.aau.asserting.logics.FOL.*;

public class FormulaBuildTests {

    @Test
    void TestPrintForall() {
        Formula f = ForAll("timeout", LTE("now", "deadline"));
        assertEquals( "(forall ((timeout Int)) (<= now deadline))" , f.print());
    }

    @Test
    void TestPrintExists() {
        Formula f = Exists("timeout", LTE("now", "deadline"));
        assertEquals( "(exists ((timeout Int)) (<= now deadline))" , f.print());
    }

    @Test
    void TestPrintAnd() {
        Formula f = AND(LT("now", "deadline"), LTE("now", "deadline"));
        assertEquals( "(and (< now deadline) (<= now deadline))" , f.print());
    }

    @Test
    void TestPrintOr() {
        Formula f = OR(GT("now", "deadline"), GTE("now", "deadline"));
        assertEquals( "(or (> now deadline) (>= now deadline))" , f.print());
    }

    @Test
    void TestPrintImplies() {
        Formula f = Implies(GT("now", "deadline"), GTE("now", "deadline"));
        assertEquals( "(or (not (> now deadline)) (>= now deadline))" , f.print());
    }

    @Test
    void TestTerminals() {
        Formula f1 = new Const("v1");
        Formula f2 = new Variable("v2");
        assertEquals( "v1" , f1.print());
        assertEquals( "v2" , f2.print());
    }
}