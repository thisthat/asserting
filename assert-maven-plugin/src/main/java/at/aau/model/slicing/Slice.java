package at.aau.model.slicing;

import at.aau.intermediateModel.interfaces.IASTMethod;
import at.aau.intermediateModel.interfaces.IASTRE;
import at.aau.intermediateModel.interfaces.IASTStm;
import at.aau.intermediateModel.structure.*;
import at.aau.intermediateModel.structure.expression.ASTAssignment;
import at.aau.intermediateModel.structure.expression.ASTMethodCall;
import at.aau.intermediateModel.structure.expression.ASTNewObject;
import at.aau.intermediateModel.structure.expression.ASTVariableDeclaration;
import at.aau.intermediateModel.visitors.ApplyHeuristics;
import at.aau.intermediateModel.visitors.DefaultASTVisitor;
import at.aau.model.slicing.heuristics.*;
import at.aau.model.slicing.model.*;
import at.aau.model.slicing.model.interfaces.Stm;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by giovanni on 06/07/2017.
 */
public class Slice {

    static ApplyHeuristics ah = new ApplyHeuristics();
    static {
        //ah.set__DEBUG__(true);
        ah.subscribe(MarkTime.class);
        ah.subscribe(TimeInSignature.class);
        ah.subscribe(AssignmentTimeVar.class);
        ah.subscribe(BooleanExpression.class);
        ah.subscribe(MinMaxSearch.class);
        ah.subscribe(ReturnExpression.class);
        ah.subscribe(AddTimeVarToTimeExpression.class);
    }

    private ASTClass _class;
    private HashMap<IASTMethod,Method> slices = new LinkedHashMap<>();
    private List<Stm> current;
    private List<TimeElement> timeStms;
    private HashMap<ASTClass, List<ASTRE>> timeAttributes;

    public static HashMap<IASTMethod, Method> slice(ASTClass c) {
        return slice(c,true);
    }

    public static HashMap<IASTMethod, Method> slice(ASTClass c, boolean shrink) {
        TimeStatements timeStms = TimeStatements.getInstance();
        timeStms.clear();
        ah.analyze(c);
        //debug
        Slice slicer = new Slice(c);
        slicer.setTimeAttributes(ah.getTimeAttrs());
        slicer.start();
        HashMap<IASTMethod,Method> slices = slicer.getSlices();
        if(shrink) {
            for (IASTMethod k : slices.keySet()) {
                Shrinker.shrink(slices.get(k));
            }
        }
        return slices;
    }

    private Slice(ASTClass _class) {
        this._class = _class;
        timeStms = TimeStatements.getInstance().getStms();
    }

    public void setTimeAttributes(HashMap<ASTClass, List<ASTRE>> timeAttributes) {
        this.timeAttributes = timeAttributes;
    }

    public HashMap<IASTMethod, Method> getSlices() {
        return slices;
    }

    private void start() {
        slices.clear();
        for(IASTMethod m : _class.getMethods()){
            String fullname = _class.fullName() + "_" + m.getName();
            Method mm = new Method(m.getStart(),m.getEnd(),m.getLine(),m.getLineEnd(),m.getCode(), fullname);
            mm.setName(m.getName());
            mm.setSignature(m.getSignature());
            current = mm.getBody();
            slices.put(m, mm);
            if(timeAttributes.containsKey(_class)){
                for(ASTRE v : timeAttributes.get(_class)){
                    analyzeAttribute(v);
                }
            }
            analyze(m);
        }
    }

    private void analyzeAttribute(ASTRE expr) {
        if(expr != null && expr.getExpression() != null) {
            Stm a = handleAssignment(expr.getExpression());
            if(!current.contains(a))
                current.add(a);
        }
    }

    private void analyze(IASTMethod m) {
        analyze(m.getStms());
    }

    private void analyze(List<IASTStm> stms) {
        for(IASTStm stm : stms) {
            if (stm instanceof ASTRE){
                analyze((ASTRE) stm);
            }
            else if (stm instanceof ASTAssert){
                analyze((ASTAssert) stm);
            }
            else if(stm	instanceof ASTDoWhile){
                analyze((ASTDoWhile)stm);
            }
            else if(stm	instanceof ASTFor){
                analyze((ASTFor)stm);
            }
            else if(stm	instanceof ASTForEach){
                analyze((ASTForEach)stm);
            }
            else if(stm	instanceof ASTIf){
                analyze((ASTIf)stm);
            }
            else if(stm	instanceof ASTReturn){
                analyze((ASTReturn)stm);
            }
            else if(stm	instanceof ASTSwitch){
                analyze((ASTSwitch)stm);
            }
            else if(stm	instanceof ASTSynchronized){
                analyze((ASTSynchronized)stm);
            }
            else if(stm	instanceof ASTThrow){
                analyze((ASTThrow)stm);
            }
            //the order is important because ASTTry is extended by ASTTryResources
            else if(stm	instanceof ASTTryResources){
                analyze((ASTTryResources)stm);
            }
            else if(stm	instanceof ASTTry){
                analyze((ASTTry)stm);
            }
            else if(stm	instanceof ASTWhile){
                analyze((ASTWhile)stm);
            }
            else if(stm instanceof ASTNewObject){
                analyze((ASTNewObject) stm);
            }
            else if(stm instanceof ASTHiddenClass){
                analyze((ASTHiddenClass)stm);
            }
            else {
                analyze(stm);
            }
        }
    }

    private void analyze(ASTHiddenClass hc) {
        List<Stm> bck = current;

        for(IASTMethod m : hc.getMethods()) {
            List<Stm> bckMethod = bck;
            String fullname = hc.fullName() + "_" + m.getName();
            Method mm = new Method(m.getStart(), m.getEnd(), m.getLine(), m.getLineEnd(), m.getCode(), fullname);

            mm.setName("anonymous" + hc.getIdHidden() + "_" + m.getName());
            mm.setSignature(m.getSignature());
            current = mm.getBody();
            slices.put(m, mm);

            ASTClass parent = hc;
            do{
                if(timeAttributes.containsKey(parent)){
                    for(ASTRE v : timeAttributes.get(parent)){
                        analyzeAttribute(v);
                    }
                }
                parent = parent.getParent();
            } while (parent != null);

            analyze(m);

            bck = bckMethod;
        }

        current = bck;
    }

    private void analyze(ASTNewObject stm) {
        ASTHiddenClass hc = stm.getHiddenClass();
        if(hc != null){
            analyze(hc);
        }
        for(IASTRE p : stm.getParameters()){
            if(p instanceof ASTNewObject){
                analyze((ASTNewObject) p);
            }
        }
    }

    private void analyze(ASTWhile stm) {
        List<Stm> bck = current;
        While w = new While(stm);
        for(String v : stm.getTimeVars()){
            w.addTimeVar(v);
        }
        Stm g = getStm(stm.getExpr());
        if(g instanceof Expression)
            w.setExpr((Expression) g);
        current.add(w);
        current = w.getWhileBody();

        analyze(stm.getStms());

        current = bck;
    }

    private void analyze(ASTDoWhile stm) {

        //one round as it's executed normally
        //analyze(stm.getStms());

        List<Stm> bck = current;
        DoWhile w = new DoWhile(stm);
        for(String v : stm.getTimeVars()){
            w.addTimeVar(v);
        }
        Stm g = getStm(stm.getExpr());
        if(g instanceof Expression)
            w.setExpr((Expression) g);
        current.add(w);
        current = w.getWhileBody();

        analyze(stm.getStms());

        current = bck;

    }

    private void analyze(ASTTry stm) {
        analyze(stm.getTryBranch().getStms());
        for(ASTTry.ASTCatchBranch catchBranch : stm.getCatchBranch()){
            analyze( catchBranch.getStms() );
        }
        if(stm.getFinallyBranch() != null)
            analyze(stm.getFinallyBranch().getStms());
    }

    private void analyze(ASTTryResources stm) {
        for(ASTRE r : stm.getResources()){
            analyze(r);
        }
        analyze((ASTTry) stm);
    }

    private void analyze(ASTThrow stm) {
        Stop s = new Stop(stm.getStart(), stm.getEnd(), stm.getLine(), stm.getLineEnd(), stm.getCode());
        current.add(s);
    }

    private void analyze(ASTSynchronized stm) {
        analyze(stm.getExpr());
        analyze(stm.getStms());
    }

    private void analyze(ASTSwitch stm) {
        analyze(stm.getExpr());
        for (ASTSwitch.ASTCase c : stm.getCases()) {
            analyze( c.getStms() );
        }
    }

    private void analyze(ASTReturn stm) {
        if(stm.getExpr() != null) {
            analyze(stm.getExpr());
            Stop s = new Stop(stm.getStart(), stm.getEnd(), stm.getLine(), stm.getLineEnd(), stm.getCode());
            current.add(s);
        }
    }

    private void analyze(ASTIf stm) {
        List<Stm> bck = current;
        If _if = new If(stm);
        Stm g = getStm(stm.getGuard());
        if(g instanceof Expression)
            _if.setExpr((Expression) g);
        //_if.setExpr(g);
        current.add(_if);

        current = _if.getIfBody();
        analyze(stm.getIfBranch().getStms());

        if(stm.getElseBranch() != null) {
            current = _if.getElseBody();
            analyze(stm.getElseBranch().getStms());
        }

        current = bck;
    }

    private void analyze(ASTForEach stm) {
        analyze(stm.getExpr());
        analyze(stm.getStms());
    }

    private void analyze(ASTFor stm) {
        for(ASTRE exp : stm.getInit()) {
            analyze(exp);
        }
        analyze(stm.getExpr());
        analyze(stm.getStms());
        for(ASTRE exp : stm.getPost()){
            analyze(exp);
        }
    }

    private void analyze(ASTRE r) {
        if(isAssertLibrary(r)){
            Assert ass = new Assert(r.getStart(),
                r.getEnd(),
                r.getLine(),
                r.getLineEnd(),
                r.getCode());
            current.add(ass);
        } else {
            Stm stm = getStm(r);
            if (stm != null)
                current.add(stm);
            analyzeNewObj(r);
        }
    }

    private boolean isAssertLibrary(ASTRE r) {
        if(r != null && r.getExpression() instanceof ASTMethodCall){
            ASTMethodCall mc = (ASTMethodCall) r.getExpression();
            return mc.getClassPointed() != null && mc.getClassPointed().equals("at.aau.asserting.AssertLibrary");
        }
        return false;
    }


    private void analyze(ASTAssert r) {
//        Assert ass = new Assert(r.getStart(),
//                r.getEnd(),
//                r.getLine(),
//                r.getLineEnd(),
//                r.getCode());
//        current.add(ass);
    }

    private Stm getStm(ASTRE r){
        TimeElement te = valid(r);
        if(te != null){
            IASTRE stm = r.getExpression();
            if(stm instanceof ASTVariableDeclaration || stm instanceof ASTAssignment) {
                return handleAssignment(stm);
            } else if(stm instanceof ASTMethodCall){
                return handleMethodCall((ASTMethodCall)stm);
            }

            Expression e = new Expression(stm);
            e.setExpr(stm);
            return e;
        }
        return null;
    }

    private void analyzeNewObj(ASTRE r) {
        if(r != null && r.getExpression() != null) {
            final ASTNewObject[] objs = {null};
            r.getExpression().visit(new DefaultASTVisitor() {
                @Override
                public void enterASTNewObject(ASTNewObject elm) {
                    if(objs[0] == null){
                        objs[0] = elm;
                    }
                }
            }.setExcludeHiddenClassContinuos(true));
            if(objs[0] != null)
                analyze(objs[0]);
        }
    }

    private void analyze(IASTStm stm) {
        if(stm instanceof ASTContinue || stm instanceof  ASTBreak)
            return;
        System.err.println("Not supported, YET!");
        System.err.println(stm.getCode());
    }

    private TimeElement valid(ASTRE r){
        if(r == null)
            return null;
        int s = r.getStart();
        int e = r.getEnd();
        int l = r.getLine();
        for(TimeElement te : this.timeStms){
            IASTStm stm = te.getStm();
            if(stm.getLine() == l && stm.getStart() == s && stm.getEnd() == e)
                return te;
        }
        return null;
    }

    private Assignment handleAssignment(IASTRE stm) {
        Assignment as = new Assignment(stm);
        if(stm instanceof ASTVariableDeclaration){
            ASTVariableDeclaration vd = (ASTVariableDeclaration) stm;
            as.setLeft(vd.getNameString());
            as.setRight(vd.getExpr());
        } else {
            ASTAssignment a = (ASTAssignment) stm;
            as.setLeft(a.getLeft().print());
            as.setRight(a.getRight());
        }
        return as;
    }

    private MethodCall handleMethodCall(ASTMethodCall stm) {
        MethodCall mc = new MethodCall(stm);
        mc.setPointedClass(stm.getClassPointed());
        mc.setMethodCall(stm);
        mc.setVariables(stm.getTimePars());
        return mc;
    }
}
