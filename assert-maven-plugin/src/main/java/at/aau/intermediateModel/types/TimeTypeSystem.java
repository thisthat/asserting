package at.aau.intermediateModel.types;

import debugger.Debugger;
import intermediateModel.structure.ASTClass;
import intermediateModel.structure.ASTRE;
import intermediateModel.types.definition.TimeType;
import intermediateModel.types.rules.TypeResolver;
import intermediateModel.types.rules.exception.TimeTypeError;
import intermediateModel.types.rules.exception.TimeTypeRecommendation;
import intermediateModel.types.rules.exception.TimeTypeWarning;
import intermediateModel.visitors.ApplyHeuristics;
import intermediateModel.visitors.interfaces.ParseIM;
import intermediateModelHelper.envirorment.Env;
import intermediateModelHelper.heuristic.v2.*;

import java.util.ArrayList;
import java.util.List;

public class TimeTypeSystem extends ParseIM  {

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

    public TimeTypeSystem() {
    }

    public TimeTypeSystem(ASTClass _class) {
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
            Debugger.getInstance(false).log("Line " + r.getLine() + " : " + t);
        } catch (TimeTypeRecommendation error){
            // catch the error and enhance it. Then store.
            recommendation.add(new TimeTypeRecommendation(super.getLastClass(), super._class.getPath(), error));
        } catch (TimeTypeError error){
            // catch the error and enhance it. Then store.
            errors.add(new TimeTypeError(super.getLastClass(), super._class.getPath(), error));
        } catch (TimeTypeWarning warn) {
            warnings.add(new TimeTypeWarning(super.getLastClass(), super._class.getPath(), warn));
        } catch (Exception e){
            System.out.println("@ File:" + this._class.getPath());
            System.err.println("@ File:" + this._class.getPath());
            e.printStackTrace();
        }
    }


}
