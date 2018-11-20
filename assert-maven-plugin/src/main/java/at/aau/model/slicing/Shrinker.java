package at.aau.model.slicing;

import at.aau.model.slicing.model.If;
import at.aau.model.slicing.model.Method;
import at.aau.model.slicing.model.While;
import at.aau.model.slicing.model.interfaces.Stm;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by giovanni on 06/07/2017.
 */
public class Shrinker {

    public static void shrink(Stm s){
        if(s instanceof Method){
            shrink((Method)s);
        } else if(s instanceof While){
            shrink((While)s);
        } else if(s instanceof If){
            shrink((If)s);
        }
    }

    public static void shrink(Method m){
        for(Stm s : m.getBody()){
            shrink(s);
        }
        List<Stm> keep = new ArrayList<>();
        for(Stm s : m.getBody()){
            if(shouldKeep(s)){
                keep.add(s);
            }
        }
        m.setBody(keep);
    }

    public static void shrink(While w){
        for(Stm s : w.getWhileBody()){
            shrink(s);
        }
        List<Stm> keep = new ArrayList<>();
        for(Stm s : w.getWhileBody()){
            if(shouldKeep(s)){
                keep.add(s);
            }
        }
        w.setWhileBody(keep);
    }

    public static void shrink(If _if){
        for(Stm s : _if.getIfBody()){
            shrink(s);
        }
        for(Stm s : _if.getElseBody()){
            shrink(s);
        }
        List<Stm> keep = new ArrayList<>();
        for(Stm s : _if.getIfBody()){
            if(shouldKeep(s)){
                keep.add(s);
            }
        }
        _if.setIfBody(keep);

        keep = new ArrayList<>();
        for(Stm s : _if.getElseBody()){
            if(shouldKeep(s)){
                keep.add(s);
            }
        }
        _if.setElseBody(keep);
    }


    private static boolean shouldKeep(Stm s) {
        if(s instanceof If) {
            return shouldKeep((If) s);
        } else if(s instanceof While){
            return shouldKeep((While) s);
        }
        return true;
    }

    private static boolean shouldKeep(If s) {
        boolean flag = false;
        if(s.getExpr() != null)
            flag = true;
        if(s.getIfBody().size() != 0)
            flag = true;
        if(s.getElseBody().size() != 0)
            flag = true;
        return flag;
    }

    private static boolean shouldKeep(While s) {
        boolean flag = false;
        if(s.getExpr() != null)
            flag = true;
        if(s.getWhileBody().size() != 0)
            flag = true;
        return flag;
    }

}
