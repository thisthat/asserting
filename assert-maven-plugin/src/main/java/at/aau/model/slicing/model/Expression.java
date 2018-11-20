package at.aau.model.slicing.model;

import at.aau.intermediateModel.interfaces.IASTRE;
import at.aau.model.slicing.model.interfaces.Stm;
import at.aau.model.slicing.model.visitor.ReducedVisitor;

/**
 * Created by giovanni on 11/07/2017.
 */
public class Expression extends Stm {

    IASTRE expr = null;

    public Expression(int start, int end, int line, int lineEnd, String code) {
        super(start, end, line, lineEnd, code);
    }

    public Expression(IASTRE stm) {
        super(  stm.getStart(),
                stm.getEnd(),
                stm.getLine(),
                stm.getLineEnd(),
                stm.getCode()
                );
    }

    public IASTRE getExpr() {
        return expr;
    }

    public void setExpr(IASTRE expr) {
        this.expr = expr;
    }

    @Override
    public void visit(ReducedVisitor visitor) {
        visitor.enterExpression(this);
        if(expr != null)
            expr.visit(visitor);
        visitor.exitExpression(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Expression that = (Expression) o;
        if(!super.equals(o)) return false;

        return expr != null ? expr.equals(that.expr) : that.expr == null;
    }

    @Override
    public int hashCode() {
        return expr != null ? expr.hashCode() : 0;
    }

    @Override
    public String toString() {
        return expr.print();
    }
}
