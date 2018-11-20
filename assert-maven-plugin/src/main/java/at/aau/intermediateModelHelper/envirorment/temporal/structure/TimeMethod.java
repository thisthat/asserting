package at.aau.intermediateModelHelper.envirorment.temporal.structure;

import intermediateModel.structure.expression.ASTMethodCall;
import intermediateModel.types.definition.TimeType;

import java.util.Arrays;
import java.util.List;

/**
 * Created by giovanni on 07/03/2017.
 */
public class TimeMethod extends TimeInfo {

    private int[] timeouts;
    TimeType[] timeType;

    public TimeMethod(String className, String methodName, List<String> signature, int[] timeouts) {
        super(className, methodName, signature);
        this.timeouts = timeouts;
        timeType = new TimeType[0];
    }

    public TimeMethod(String className, String methodName, List<String> signature, int[] timeouts, TimeType[] timeType) {
        super(className, methodName, signature);
        this.timeouts = timeouts;
        this.timeType = timeType;
    }

    public TimeType[] getTimeType() {
        return timeType;
    }

    public int[] getTimeouts() {
        return timeouts;
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
        StringBuilder ti = new StringBuilder();
        for(int i = 0; i < timeouts.length; i++){
            ti.append(timeouts[i]).append(",").append(timeType[i]);
            if(i != timeouts.length -1)
                ti.append(",");
        }
        return String.format("%s;%s;%s;%s", className, methodName, sign.toString(), ti.toString());
    }

    public boolean isMethodCall(ASTMethodCall m) {
        if(m.getClassPointed() == null) return false;
        if(m.getMethodName() == null) return false;
        if(!m.getClassPointed().equals(className)) return false;
        if(!m.getMethodName().equals(methodName)) return false;
        if(m.getParameters().size() != signature.size()) return false;
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TimeMethod)) return false;
        TimeMethod that = (TimeMethod) o;
        if(!that.getClassName().equals(this.getClassName())) return false;
        if(!that.getMethodName().equals(this.getMethodName())) return false;
        if(!that.getSignature().equals(this.getSignature())) return false;
        return Arrays.equals(getTimeouts(), that.getTimeouts()) &&
                Arrays.equals(getTimeType(), that.getTimeType());
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(getTimeouts());
        result = 31 * result + Arrays.hashCode(getTimeType());
        return result;
    }
}
