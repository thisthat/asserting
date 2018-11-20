package at.aau.intermediateModel.types.rules.exception;

public class TimeTypeWarning extends TimeException {
    private String className;
    private String filename;
    private int line;
    private String cause;

    public TimeTypeWarning(String className, String filename, int line, String cause) {
        super(cause);
        this.className = className;
        this.filename = filename;
        this.line = line;
    }

    public TimeTypeWarning(int line, String cause) {
        super(cause);
        this.line = line;
    }

    public TimeTypeWarning(String className, String filename, TimeTypeWarning prev) {
        super(prev.getMessage());
        this.cause = prev.getMessage();
        this.line = prev.getLine();
        this.className = className;
        this.filename = filename;
    }


    public String getClassName() {
        return className;
    }

    public String getFilename() {
        return filename;
    }

    public int getLine() {
        return line;
    }


    public String getCauseMsg() {
        return cause;
    }

    public String getFullMessage() {
        return String.format(
                "%s - %s -- [%d] %s",
                filename, className, line, cause
        );
    }
}
