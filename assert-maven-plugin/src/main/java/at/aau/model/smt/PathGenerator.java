package at.aau.model.smt;

import com.rits.cloning.Cloner;
import at.aau.model.slicing.Shrinker;
import at.aau.model.slicing.model.Expression;
import at.aau.model.slicing.model.If;
import at.aau.model.slicing.model.Method;
import at.aau.model.slicing.model.While;
import at.aau.model.slicing.model.interfaces.Stm;
import at.aau.model.slicing.model.visitor.DefaultReducedVisitor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by giovanni on 10/07/2017.
 */
public class PathGenerator {

    List<Method> models;
    Method original;
    Cloner cloner = new Cloner();

    public PathGenerator() {
    }

    public List<Method> generate(Method m){
        original = m;
        models = new ArrayList<>();
        models.add(m);
        for(Stm s : m.getBody()){
            convert(s);
        }
        for(Method mm : models){
            Shrinker.shrink(mm);
        }
        return models;
    }

    private void convert(Stm s) {
        if(s instanceof If){
            handleIf((If) s);
        } else if(s instanceof While){
            handleWhile((While) s);
        }
    }


    private void handleWhile(While s) {
        Method m = cloner.deepClone(original);
        remove(m, s.getWhileBody());
        remove(m, s.getExpr());
        models.add(m);
        for(Stm ss : s.getWhileBody()){
            convert(ss);
        }
    }

    private void handleIf(If s) {
        if(s.getExpr() != null)
            return; //only if we do not have time in the if
        Method m = cloner.deepClone(original);
        remove(m, s.getIfBody());
        if(s.getElseBody() != null && s.getElseBody().size() == 0) {
            //there is no else
            remove(m, s.getExpr());
        }
        models.add(m);
        if(s.getElseBody() != null && s.getElseBody().size() != 0){
            Method m1 = cloner.deepClone(original);
            remove(m1, s.getIfBody());
        }

        for(Stm ss : s.getIfBody()){
            convert(ss);
        }
        for(Stm ss : s.getElseBody()){
            convert(ss);
        }
    }


    private void remove(Method m, Expression e){
        m.visit(new DefaultReducedVisitor(){
            @Override
            public void enterIf(If elm) {
                if(elm.getExpr() != null && elm.getExpr().equals(e)){
                    elm.setExpr(null);
                }
            }

            @Override
            public void enterWhile(While elm) {
                if(elm.getExpr() != null && elm.getExpr().equals(e)){
                    elm.setExpr(null);
                }
            }
        });
    }

    private void remove(Method m, List<Stm> body) {
        m.visit(new DefaultReducedVisitor(){
            @Override
            public void enterIf(If elm) {
                if(elm.getIfBody().equals(body)){
                    elm.setIfBody(new ArrayList<>());
                } else if(elm.getElseBody() != null && elm.getElseBody().equals(body)){
                    elm.setElseBody(new ArrayList<>());
                }
            }

            @Override
            public void enterWhile(While elm) {
                if(elm.getWhileBody().equals(body)){
                    elm.setWhileBody(new ArrayList<>());
                }
            }
        });
    }
}
