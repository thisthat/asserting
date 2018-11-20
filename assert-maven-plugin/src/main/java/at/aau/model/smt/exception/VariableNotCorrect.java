package at.aau.model.smt.exception;

import at.aau.model.slicing.model.interfaces.Stm;

/**
 * Created by giovanni on 17/07/2017.
 */
public class VariableNotCorrect {
    String varName;
    Stm where;
    String minModel = "";
    String maxModel = "";

    public VariableNotCorrect(String varName, Stm where) {
        this.varName = varName;
        this.where = where;
    }

    public VariableNotCorrect(String varName, Stm where, String min, String max) {
        this.varName = varName;
        this.where = where;
        this.minModel = min;
        this.maxModel = max;
    }

    public String getMinModel() {
        return minModel;
    }

    public String getMaxModel() {
        return maxModel;
    }

    public String getVarName() {
        return varName;
    }

    public Stm getWhere() {
        return where;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VariableNotCorrect that = (VariableNotCorrect) o;

        if (varName != null ? !varName.equals(that.varName) : that.varName != null) return false;
        return where != null ? where.equals(that.where) : that.where == null;
    }

    @Override
    public int hashCode() {
        int result = varName != null ? varName.hashCode() : 0;
        result = 31 * result + (where != null ? where.hashCode() : 0);
        return result;
    }
}
