package at.aau.model.smt.exception;

/**
 * Created by giovanni on 10/07/2017.
 */
public class ModelNotCorrect extends Exception {
    String varName;

    public ModelNotCorrect(String varName) {
        this.varName = varName;
    }

    @Override
    public String getMessage() {
        return String.format("[WARNING] Variable `%s` can be negative", varName);
    }

}
