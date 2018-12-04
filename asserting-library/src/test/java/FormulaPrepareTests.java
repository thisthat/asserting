import at.aau.asserting.Formula;
import at.aau.asserting.logics.FOLConstructors.Const;
import at.aau.asserting.logics.FOLConstructors.Variable;
import org.junit.jupiter.api.Test;

import static at.aau.asserting.logics.FOL.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class FormulaPrepareTests {

    String def = "\r\n(maximize time)\n(check-sat)\n" +
            "(get-model)";

    @Test
    void TestPrintForall() {
        Formula f = ForAll("timeout", LTE("now", "deadline"));
        assertEquals( "(assert (exists ((timeout Int)) (> now deadline)))" + def, f.prepare());
    }

    @Test
    void TestPrintExists() {
        Formula f = Exists("timeout", LTE("now", "deadline"));
        assertEquals( "(assert (forall ((timeout Int)) (> now deadline)))" + def, f.prepare());
    }

    @Test
    void TestPrintAnd() {
        Formula f = AND(LT("now", "deadline"), LTE("now", "deadline"));
        assertEquals( "(assert (or (>= now deadline) (> now deadline)))" + def, f.prepare());
    }

    @Test
    void TestPrintOr() {
        Formula f = OR(GT("now", "deadline"), GTE("now", "deadline"));
        assertEquals( "(assert (and (<= now deadline) (< now deadline)))" + def, f.prepare());
    }

    @Test
    void TestPrintImplies() {
        Formula f = Implies(GT("now", "deadline"), GTE("now", "deadline"));
        assertEquals( "(assert (and (> now deadline) (< now deadline)))" + def, f.prepare());
    }

    @Test
    void TestTerminals() {
        Formula f1 = new Const("v1");
        Formula f2 = new Variable("v2");
        assertEquals( "(assert v1)" + def , f1.prepare());
        assertEquals( "(assert v2)" + def , f2.prepare());
    }
}