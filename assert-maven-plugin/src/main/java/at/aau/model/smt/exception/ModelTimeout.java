package at.aau.model.smt.exception;

/**
 * Created by giovanni on 10/07/2017.
 */
public class ModelTimeout extends Exception {
    String varName;

    public ModelTimeout(String varName) {
        this.varName = varName;
    }

    @Override
    public String getMessage() {
        return String.format("[WARNING] Model timed out with variable `%s`", varName);
    }

}
