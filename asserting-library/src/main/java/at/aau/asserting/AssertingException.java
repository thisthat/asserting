package at.aau.asserting;

public class AssertingException extends RuntimeException {
    public AssertingException(String formula, String message) {
        super("Cannot validate " + formula + " \r\n -- counter example -- \r\n" + message);
    }
}
