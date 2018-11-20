package at.aau.model.slicing.model.interfaces;

import at.aau.intermediateModel.interfaces.IASTToken;
import at.aau.model.slicing.model.visitor.IReducedVisitor;

/**
 * Created by giovanni on 11/07/2017.
 */
public abstract class Stm implements IReducedVisitor, IASTToken {


    protected int start;
    protected int end;
    protected int line;
    protected int lineEnd;

    protected String code = "";

    public Stm(int start, int end, int line, int lineEnd, String code) {
        this.start = start;
        this.end = end;
        this.line = line;
        this.lineEnd = lineEnd;
        this.code = code;
    }


    public String getCode(){
        return code;
    }

    /**
     * Get the line in the file of the node
     * @return the line number
     */
    public int getLine(){
        return line;
    }

    /**
     * Get the end line in the file of the node
     * @return the end line number
     */
    public int getLineEnd(){
        return lineEnd;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Stm stm = (Stm) o;

        if (start != stm.start) return false;
        if (end != stm.end) return false;
        if (line != stm.line) return false;
        if (lineEnd != stm.lineEnd) return false;
        return code != null ? code.equals(stm.code) : stm.code == null;
    }

    @Override
    public int hashCode() {
        int result = start;
        result = 31 * result + end;
        result = 31 * result + line;
        result = 31 * result + lineEnd;
        result = 31 * result + (code != null ? code.hashCode() : 0);
        return result;
    }
}
