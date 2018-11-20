package at.aau.model.slicing.model;

import at.aau.intermediateModel.structure.ASTIf;
import at.aau.model.slicing.model.interfaces.Stm;
import at.aau.model.slicing.model.visitor.ReducedVisitor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by giovanni on 11/07/2017.
 */
public class If extends Stm {

    Expression expr = null;
    List<Stm> ifBody = new ArrayList<>();
    List<Stm> elseBody = new ArrayList<>();

    public If(int start, int end, int line, int lineEnd, String code) {
        super(start, end, line, lineEnd, code);
    }

    public If(ASTIf stm) {
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

    public List<Stm> getIfBody() {
        return ifBody;
    }

    public void setIfBody(List<Stm> ifBody) {
        this.ifBody = ifBody;
    }

    public List<Stm> getElseBody() {
        return elseBody;
    }

    public void setElseBody(List<Stm> elseBody) {
        this.elseBody = elseBody;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        If anIf = (If) o;

        if(!super.equals(o)) return false;

        if (expr != null ? !expr.equals(anIf.expr) : anIf.expr != null) return false;
        if (ifBody != null ? !ifBody.equals(anIf.ifBody) : anIf.ifBody != null) return false;
        return elseBody != null ? elseBody.equals(anIf.elseBody) : anIf.elseBody == null;
    }

    @Override
    public int hashCode() {
        int result = expr != null ? expr.hashCode() : 0;
        result = 31 * result + (ifBody != null ? ifBody.hashCode() : 0);
        result = 31 * result + (elseBody != null ? elseBody.hashCode() : 0);
        return result;
    }

    @Override
    public void visit(ReducedVisitor visitor) {
        visitor.enterIf(this);
        if(expr != null){
            expr.visit(visitor);
        }
        for(Stm s : ifBody){
            s.visit(visitor);
        }
        for(Stm s : elseBody){
            s.visit(visitor);
        }
        visitor.exitIf(this);
    }
}
