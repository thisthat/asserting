package at.aau.model.slicing.model;

import at.aau.intermediateModel.interfaces.IASTRE;
import at.aau.intermediateModel.interfaces.IASTStm;
import at.aau.model.slicing.model.interfaces.Stm;
import at.aau.model.slicing.model.visitor.ReducedVisitor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by giovanni on 11/07/2017.
 */
public class MethodCall extends Stm {

    String pointedClass;
    IASTRE methodCall;
    List<String> variables = new ArrayList<>();

    public MethodCall(int start, int end, int line, int lineEnd, String code) {
        super(start, end, line, lineEnd, code);
    }

    public MethodCall(IASTStm stm) {
        super(  stm.getStart(),
                stm.getEnd(),
                stm.getLine(),
                stm.getLineEnd(),
                stm.getCode()
        );
    }

    public String getPointedClass() {
        return pointedClass;
    }

    public void setPointedClass(String pointedClass) {
        this.pointedClass = pointedClass;
    }

    public IASTRE getMethodCall() {
        return methodCall;
    }

    public void setMethodCall(IASTRE methodCall) {
        this.methodCall = methodCall;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MethodCall that = (MethodCall) o;
        if(!super.equals(o)) return false;
        if (pointedClass != null ? !pointedClass.equals(that.pointedClass) : that.pointedClass != null) return false;
        return methodCall != null ? methodCall.equals(that.methodCall) : that.methodCall == null;
    }

    @Override
    public int hashCode() {
        int result = pointedClass != null ? pointedClass.hashCode() : 0;
        result = 31 * result + (methodCall != null ? methodCall.hashCode() : 0);
        return result;
    }

    @Override
    public void visit(ReducedVisitor visitor) {
        visitor.enterMethodCall(this);
        if(methodCall != null)
            methodCall.visit(visitor);
        visitor.exitMethodCall(this);
    }

    public void addVariable(String name) {
        if(!variables.contains(name))
            variables.add(name);
    }

    public void setVariables(List<String> variables) {
        this.variables = variables;
    }

    public List<String> getVariables() {
        return variables;
    }
}
