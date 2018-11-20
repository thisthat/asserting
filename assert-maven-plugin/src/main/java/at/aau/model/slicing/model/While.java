package at.aau.model.slicing.model;

import at.aau.intermediateModel.interfaces.IASTStm;
import at.aau.model.slicing.model.interfaces.Stm;
import at.aau.model.slicing.model.visitor.ReducedVisitor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by giovanni on 11/07/2017.
 */
public class While extends Stm {

    Expression expr;
    List<Stm> whileBody = new ArrayList<>();
    List<String> timeVarsInExpr = new ArrayList<>();

    public While(int start, int end, int line, int lineEnd, String code) {
        super(start, end, line, lineEnd, code);
    }

    public While(IASTStm stm) {
        super(  stm.getStart(),
                stm.getEnd(),
                stm.getLine(),
                stm.getLineEnd(),
                stm.getCode()
        );
    }

    public Expression getExpr() {
        return expr;
    }

    public void setExpr(Expression expr) {
        this.expr = expr;
    }

    public List<Stm> getWhileBody() {
        return whileBody;
    }

    public void setWhileBody(List<Stm> whileBody) {
        this.whileBody = whileBody;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        While aWhile = (While) o;
        if(!super.equals(o)) return false;
        if (expr != null ? !expr.equals(aWhile.expr) : aWhile.expr != null) return false;
        return whileBody != null ? whileBody.equals(aWhile.whileBody) : aWhile.whileBody == null;
    }

    @Override
    public int hashCode() {
        int result = expr != null ? expr.hashCode() : 0;
        result = 31 * result + (whileBody != null ? whileBody.hashCode() : 0);
        return result;
    }

    @Override
    public void visit(ReducedVisitor visitor) {
        visitor.enterWhile(this);
        if(expr != null)
            expr.visit(visitor);
        for(Stm s : whileBody){
            s.visit(visitor);
        }
        visitor.exitWhile(this);
    }

    public List<String> getTimeVars() {
        return timeVarsInExpr;
    }
    public void addTimeVar(String name){
        this.timeVarsInExpr.add(name);
    }
}
