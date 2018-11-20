package at.aau.model.slicing;

import at.aau.model.slicing.model.*;
import at.aau.model.slicing.model.interfaces.Stm;

import java.util.List;

public class SLOCCounter {

    public static long count(Method m){
        return count(m.getBody());
    }

    private static long count(List<Stm> body) {
        if(body == null) return 0;
        long sum = 0;
        for(Stm s : body){
            sum += count(s);
        }
        return sum;
    }

    private static long count(Stm s){
        if(s instanceof Assignment){
            return countAssignment((Assignment)s);
        } else if(s instanceof DoWhile){
            return countDoWhile((DoWhile)s);
        } else if(s instanceof While){
            return countWhile((While)s);
        } else if(s instanceof Expression){
            return countExpression((Expression)s);
        } else if(s instanceof If){
            return countIf((If)s);
        } else if(s instanceof Method){
            return 0;
        } else if(s instanceof MethodCall){
            return countMethodCall((MethodCall)s);
        } else if(s instanceof Stop){
            return countStop((Stop)s);
        }
        return 0;
    }

    private static long countStop(Stop s) {
        return 0;
    }

    private static long countMethodCall(MethodCall s) {
        return 1;
    }

    private static long countIf(If s) {
        return 2 + count(s.getIfBody()) + count(s.getElseBody());
    }

    private static long countExpression(Expression s) {
        return 1;
    }

    private static long countWhile(While s) {
        return 2 + count(s.getWhileBody());
    }

    private static long countDoWhile(DoWhile s) {
        return 2 + count(s.getWhileBody());
    }

    private static long countAssignment(Assignment s) {
        return 1;
    }


}
