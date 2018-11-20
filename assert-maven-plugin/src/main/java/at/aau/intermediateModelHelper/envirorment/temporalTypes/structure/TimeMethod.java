package at.aau.intermediateModelHelper.envirorment.temporalTypes.structure;

import intermediateModel.structure.expression.ASTMethodCall;

import java.util.List;

/**
 * Created by giovanni on 07/03/2017.
 */
public class TimeMethod {
    String className;
    String methodName;
    List<String> signature;

    public TimeMethod(String className, String methodName, List<String> signature) {
        this.className = className;
        this.methodName = methodName;
        this.signature = signature;
    }

    public TimeMethod() {
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public List<String> getSignature() {
        return signature;
    }

    public void setSignature(List<String> signature) {
        this.signature = signature;
    }

    @Override
    public String toString() {
        StringBuilder sign = new StringBuilder();
        List<String> signature = getSignature();
        int last = signature.size() - 1;
        for(int i = 0; i <= last; i++){
            if(i == last){
                sign.append(signature.get(i));
            } else {
                sign.append(signature.get(i)).append(",");
            }
        }
        return String.format("%s;%s;%s;long", getClassName(), getMethodName(), sign.toString());
    }

    public boolean isMethodCall(ASTMethodCall m) {
        if(m.getClassPointed() == null) return false;
        if(m.getMethodName() == null) return false;
        if(!m.getClassPointed().equals(getClassName())) return false;
        if(!m.getMethodName().equals(getMethodName())) return false;
        //if(m.getParameters().size() != signature.size()) return false;
        return true;
    }
}
