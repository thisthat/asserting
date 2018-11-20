package at.aau.intermediateModel.types;

import at.aau.intermediateModel.interfaces.IASTVar;
import at.aau.intermediateModel.structure.ASTClass;
import at.aau.intermediateModel.structure.ASTRE;
import at.aau.intermediateModel.types.definition.Duration;
import at.aau.intermediateModel.types.definition.TimeType;
import at.aau.intermediateModel.types.definition.Unknown;
import at.aau.intermediateModel.types.rules.TypeResolver;
import at.aau.intermediateModel.types.rules.exception.TimeTypeError;
import at.aau.intermediateModel.types.rules.exception.TimeTypeRecommendation;
import at.aau.intermediateModel.types.rules.exception.TimeTypeWarning;
import at.aau.intermediateModel.visitors.ApplyHeuristics;
import at.aau.intermediateModel.visitors.interfaces.ParseIM;
import at.aau.intermediateModelHelper.envirorment.Env;
import at.aau.intermediateModelHelper.heuristic.v2.*;

import java.util.ArrayList;
import java.util.List;

public class CountUnknown extends ParseIM  {

    static ApplyHeuristics ah = new ApplyHeuristics();
    static {
        //ah.set__DEBUG__(true);
        //ah.subscribe(MarkTime.class); it is integrated in ParseIM#checkRE
        ah.subscribe(TimeInSignature.class);
        ah.subscribe(AssignmentTimeVar.class);
        ah.subscribe(BooleanExpression.class);
        ah.subscribe(MinMaxSearch.class);
        ah.subscribe(ReturnExpression.class);
        ah.subscribe(AddTimeVarToTimeExpression.class);
    }

    List<TimeTypeError> errors = new ArrayList<>();
    List<TimeTypeRecommendation> recommendation = new ArrayList<>();
    List<TimeTypeWarning> warnings = new ArrayList<>();

    public List<TimeTypeRecommendation> getRecommendation() {
        return recommendation;
    }
    public List<TimeTypeError> getErrors() {
        return errors;
    }
    public List<TimeTypeWarning> getWarnings() {
        return warnings;
    }

    List<String> typed = new ArrayList<>();
    List<String> typedDuration = new ArrayList<>();
    List<String> typedTimestamp = new ArrayList<>();
    List<String> untyped = new ArrayList<>();

    public CountUnknown(ASTClass _class) {
        super(_class);
    }

    @Override
    public void start(ASTClass c){
        errors.clear();
        errors.clear();
        ah.analyze(c);
        super.start(c);
    }

    @Override
    protected void analyzeASTRE(ASTRE r, Env env) {
        if(r == null || r.getExpression() == null){
            return;
        }
        try {
            TimeType t = TypeResolver.resolveTimerType(r.getExpression(), env);
        } catch (Exception error){
            // don't care
        } finally {
            for(IASTVar v : env.getAllVarList()){
                String name = v.getName();
                if(v.getVarTimeType() instanceof Unknown){
                    if(typed.contains(name)) continue;
                    if(!untyped.contains(name))
                        untyped.add(name);
                } else {
                    untyped.remove(name);
                    if(!typed.contains(name)){
                        typed.add(name);
                    }
                    if(v.getVarTimeType() instanceof Duration){
                        typedTimestamp.remove(name);
                        if(!typedDuration.contains(name)){
                            typedDuration.add(name);
                        }
                    } else {
                        typedDuration.remove(name);
                        if(!typedTimestamp.contains(name)){
                            typedTimestamp.add(name);
                        }
                    }
                }
            }
        }
    }

    public List<String> getTyped() {
        return typed;
    }

    public List<String> getUntyped() {
        return untyped;
    }

    public List<String> getTypedDuration() {
        return typedDuration;
    }
    public List<String> getTypedTimestamp() {
        return typedTimestamp;
    }
}
