package at.aau.intermediateModelHelper.heuristic.v2;

import at.aau.intermediateModel.interfaces.IASTStm;

/**
 * Created by giovanni on 13/07/2017.
 */
public class TimeElement {

    public enum Type {
        MinMax,
        Usage,
        Assignment,
        Boolean,
        Return,
    }

    IASTStm stm;
    Type type;

    public TimeElement(IASTStm stm, Type type) {
        this.stm = stm;
        this.type = type;
    }

    public IASTStm getStm() {
        return stm;
    }

    public Type getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TimeElement that = (TimeElement) o;

        if (stm != null ? !stm.equals(that.stm) : that.stm != null) return false;
        return type == that.type;
    }

    @Override
    public int hashCode() {
        int result = stm != null ? stm.hashCode() : 0;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }
}
