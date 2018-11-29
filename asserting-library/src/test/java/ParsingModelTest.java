import at.aau.asserting.AssertingException;
import at.aau.asserting.Formula;
import at.aau.asserting.logics.FOLConstructors.Const;
import at.aau.asserting.logics.FOLConstructors.Variable;
import org.junit.jupiter.api.Test;

import static at.aau.asserting.logics.FOL.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ParsingModelTest {

    @Test
    void TestParseModel() {
        String model = "sat\n" +
                "(model \n" +
                "  (define-fun time () Int\n" +
                "    1543498199267)\n" +
                "  (define-fun timeout () Int\n" +
                "    0)\n" +
                "  (define-fun deadline () Int\n" +
                "    1543498199267)\n" +
                "  (define-fun now () Int\n" +
                "    1543498199267)\n" +
                "  (define-fun min_val () Int\n" +
                "    (- 9223372036854775808))\n" +
                "  (define-fun over_max_val () Int\n" +
                "    9223372036854775808)\n" +
                "  (define-fun max_val () Int\n" +
                "    9223372036854775807)\n" +
                ")\n";
        AssertingException ex = new AssertingException("","", model);
        assertEquals(7, ex.getCorrectValue().size());
        assertEquals("1543498199267", ex.getCorrectValue("time"));
        assertEquals("0", ex.getCorrectValue("timeout"));
        assertEquals("1543498199267", ex.getCorrectValue("deadline"));
        assertEquals("1543498199267", ex.getCorrectValue("now"));
        assertEquals("-9223372036854775808", ex.getCorrectValue("min_val"));
        assertEquals("9223372036854775808", ex.getCorrectValue("over_max_val"));
        assertEquals("9223372036854775807", ex.getCorrectValue("max_val"));
    }


}