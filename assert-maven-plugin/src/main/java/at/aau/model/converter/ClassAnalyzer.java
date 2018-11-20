package at.aau.model.converter;

import at.aau.intermediateModel.interfaces.IASTMethod;
import at.aau.intermediateModel.structure.ASTClass;
import at.aau.model.slicing.SLOCCounter;
import at.aau.model.slicing.Slice;
import at.aau.model.slicing.model.Method;
import at.aau.model.smt.PathGenerator;
import at.aau.model.smt.exception.VariableNotCorrect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//import at.aau.model.smt.TranslateReducedModel;

/**
 * Created by giovanni on 17/07/2017.
 */
public class ClassAnalyzer {

    //private final TranslateReducedModel translateReducedModel;
    HashMap<IASTMethod, Method> reducedModel = new HashMap<>();
    private boolean getModel = false;

    public ClassAnalyzer(ASTClass c) {
        reducedModel = Slice.slice(c);
        //translateReducedModel = new TranslateReducedModel();
    }

    public long getSLOC(){
        long sum = 0;
        PathGenerator pg = new PathGenerator();
        for(IASTMethod m : reducedModel.keySet()){
            Method mm = reducedModel.get(m);
            List<Method> analyze = pg.generate(mm);
            boolean cont = false;
            for(Method a : analyze) {
                if(cont) continue;
                sum += SLOCCounter.count(a);
                try {
                    cont = getErrors(a).size() > 0;
                } catch (Exception e) {
                    cont = true;
                }
            }
        }
        return sum;
    }

    public List<VariableNotCorrect> getErrors(IASTMethod m){
        Method mm = reducedModel.get(m);
        if(mm == null)
            return new ArrayList<>();
        return getErrors(mm);
    }

    public List<VariableNotCorrect> getErrors(Method m){
        List<VariableNotCorrect> out = new ArrayList<>();
        PathGenerator pg = new PathGenerator();
        List<Method> analyze = pg.generate(m);
        if(m.getBody().size() > 0){
            Statistic.incrementNMethod(1);
            Statistic.incrementNMethodPath(analyze.size());
        }
        //translateReducedModel.saveModel(getModel);
        List<VariableNotCorrect> tmp;
        for(Method mm : analyze) {
            //tmp = translateReducedModel.check(mm);
            //out.addAll(tmp);
        }
        return out;
    }

    public HashMap<IASTMethod, List<VariableNotCorrect>> getErrors(){
        HashMap<IASTMethod, List<VariableNotCorrect>> out = new HashMap<>();
        for(IASTMethod m : reducedModel.keySet()){
            try {
                out.put(m, getErrors(m));
            } catch (Exception e){
                System.out.println("Problem in" + m.getName() + "@" + m.getLine());
                throw e;
            }
        }
        return out;
    }

    public void setGetModel(boolean getModel) {
        this.getModel = getModel;
    }
}
