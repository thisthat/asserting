package at.aau.intermediateModelHelper.envirorment.temporal.structure;

import at.aau.intermediateModel.structure.expression.ASTMethodCall;
import at.aau.intermediateModel.types.definition.TimeType;
import at.aau.intermediateModel.types.definition.Unknown;

import java.util.List;

/**
 * Created by giovanni on 07/03/2017.
 */
public class TimeTypes extends TimeInfo {

    TimeType timeType;

    public TimeTypes(String className, String methodName, List<String> signature) {
        super(className, methodName, signature);
        timeType = new Unknown();
    }

    public TimeTypes(String className, String methodName, List<String> signature, TimeType tt) {
        super(className, methodName, signature);
        timeType = tt;
    }

    public TimeType getTimeType() {
        return timeType;
    }

    @Override
    public String toString() {
        StringBuilder sign = new StringBuilder();
        int last = signature.size() - 1;
        for(int i = 0; i <= last; i++){
            if(i == last){
                sign.append(signature.get(i));
            } else {
                sign.append(signature.get(i)).append(",");
            }
        }
        return String.format("%s;%s;%s;long", className, methodName, sign.toString());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TimeTypes)) return false;
        TimeTypes oo = (TimeTypes) o;
        if(!className.equals(oo.className)) return false;
        if(!methodName.equals(oo.methodName)) return false;
        if(!signature.equals(oo.signature)) return false;
        if(timeType == null) return oo.timeType==null;
        return timeType.equals(oo.timeType);
    }

    public boolean isMethodCall(ASTMethodCall m) {
        if(m.getClassPointed() == null) return false;
        if(m.getMethodName() == null) return false;
        if(!m.getClassPointed().equals(className)) return false;
        if(!m.getMethodName().equals(methodName)) return false;
        //if(m.getParameters().size() != signature.size()) return false;
        return true;
    }
}
