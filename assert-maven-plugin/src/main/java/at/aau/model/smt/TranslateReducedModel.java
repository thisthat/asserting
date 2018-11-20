package at.aau.model.smt;

import com.microsoft.z3.*;
import at.aau.intermediateModel.interfaces.IASTRE;
import at.aau.intermediateModel.interfaces.IASTToken;
import at.aau.intermediateModel.structure.expression.*;
import at.aau.model.slicing.model.*;
import at.aau.model.slicing.model.interfaces.Stm;
import at.aau.model.smt.exception.ModelNotCorrect;
import at.aau.model.smt.exception.ModelTimeout;
import at.aau.model.smt.exception.VarNotFoundException;
import at.aau.model.smt.exception.VariableNotCorrect;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by giovanni on 13/07/2017.
 */
public class TranslateReducedModel {

    private StackTraceElement[] cause = Thread.currentThread().getStackTrace();
    private ModelCreator modelCreator;
    private Context ctx;
    private List<String> pushModel;
    private List<VariableNotCorrect> errors;
    private List<String> timeout;
    private boolean saveModel = false;
    private boolean stop = false;

    public void saveModel(boolean f) {
        this.saveModel = f;
    }

    private enum RetType {
        INT, BOOL
    }


    public TranslateReducedModel() {
    }

    public List<String> getPushModel() {
        return pushModel;
    }

    public Optimize convert(Method m){
        stop = false;
        modelCreator = new ModelCreator();
        pushModel = new ArrayList<>();
        errors = new ArrayList<>();
        timeout = new ArrayList<>();
        ctx = modelCreator.getCtx();
        for(Stm s : m.getBody()) {
            convert(s);
        }
        return modelCreator.getOpt();
    }
    public List<VariableNotCorrect> check(Method m){
        timeout = new ArrayList<>();
        convert(m);
        if(ModelCreator._debug_ && timeout.size() > 0) {
            System.err.println("Model timeout");
            for(String txt : timeout){
                System.err.println(txt);
            }
        }
        return errors;
    }

    private void convert(Stm s) {
        if(stop)
            return;
        if(s instanceof Assignment){
            handleAssignment((Assignment) s);
        } else if(s instanceof DoWhile){
            handleDoWhile((DoWhile) s);
        } else if(s instanceof Expression){
            handleExpression((Expression) s);
        } else if(s instanceof If){
            handleIf((If) s);
        } else if(s instanceof MethodCall){
            handleMethodCall((MethodCall) s);
        } else if(s instanceof While){
            handleWhile((While) s);
        } else if(s instanceof Stop){
            stop = true;
        }else {
            notYet(s);
        }
    }

    private Expr convert(IASTRE r, RetType t){
        if(r instanceof ASTArrayInitializer){
            return handleArrayInitializer((ASTArrayInitializer)r);
        } else if(r instanceof ASTAssignment){
            return handleAssignmentExpression((ASTAssignment)r);
        } else if(r instanceof ASTAttributeAccess){
            return handleAttributeAccess((ASTAttributeAccess)r);
        } else if(r instanceof ASTBinary){
            return handleBinary((ASTBinary) r, t);
        } else if(r instanceof ASTCast){
            return handleCast((ASTCast) r, t);
        } else if(r instanceof ASTConditional){
            return handleConditional((ASTConditional) r);
        } else if(r instanceof ASTLiteral){
            return handleLiteral((ASTLiteral) r);
        } else if(r instanceof ASTIdentifier){
            return handleIdentifier((ASTIdentifier) r);
        } else if(r instanceof ASTMethodCall){
            return handleMethodCallExpression((ASTMethodCall) r);
        } else if(r instanceof ASTNewObject){
            return handleNewObject( (ASTNewObject) r);
        } else if(r instanceof ASTPostOp){
            return handlePostOp( (ASTPostOp) r);
        } else if(r instanceof ASTPreOp){
            return handlePreOp( (ASTPreOp) r);
        } else if(r instanceof ASTUnary){
            return handleUnary( (ASTUnary) r);
        } else if(r instanceof ASTVariableDeclaration){
            return handleVariableDec( (ASTVariableDeclaration) r);
        } else if(r instanceof ASTVariableMultipleDeclaration){
            return handleMultipleVarDec( (ASTVariableMultipleDeclaration) r);
        } else {
            notYet(r);
        }
        return ctx.mkInt("-9223372036854775808");
    }

    /***************************
     *       Expression        *
     ***************************/
    private Expr handleMultipleVarDec(ASTVariableMultipleDeclaration r) {
        for(IASTRE rr : r.getVars()) {
            convert(rr, RetType.BOOL);
        }
        return ctx.mkBool(true);
    }

    private Expr handleVariableDec(ASTVariableDeclaration r) {
        String varName = r.getNameString();
        IntExpr var = modelCreator.createVariable(varName);
        if(r.getExpr() != null) {
            BoolExpr b = ctx.mkEq(var, convert(r.getExpr(), RetType.BOOL));
            modelCreator.addConstraint(b);
            return b;
        }
        return var;
    }

    private Expr handleUnary(ASTUnary r) {
        Expr e = null;
        switch (r.getOp()) {
            case not:
                Expr cnv = convert(r.getExpr(), RetType.BOOL);
                if(cnv instanceof IntExpr){
                    cnv = ctx.mkBool(true);
                }
                e = ctx.mkNot((BoolExpr) cnv);
                modelCreator.addConstraint((BoolExpr) e);
                break;
            case minus:
                return ctx.mkSub( ctx.mkInt(0), (ArithExpr) convert(r.getExpr(), RetType.INT));
        }
        return e;
    }

    private BoolExpr handlePreOp(ASTPreOp r) {
        IntExpr var = modelCreator.createVariable(r.getVar().print());
        Expr tmp;
        if(r.getType() == IASTRE.ADDDEC.increment) {
            tmp = ctx.mkAdd(var, ctx.mkInt(1));
        } else {
            tmp = ctx.mkSub(var, ctx.mkInt(1));
        }
        BoolExpr b = ctx.mkEq(var, tmp);
        modelCreator.addConstraint(b);
        return b;
    }

    private BoolExpr handlePostOp(ASTPostOp r) {
        IntExpr var = modelCreator.createVariable(r.getVar().print());
        Expr tmp;
        if(r.getType() == IASTRE.ADDDEC.increment) {
            tmp = ctx.mkAdd(var, ctx.mkInt(1));
        } else {
            tmp = ctx.mkSub(var, ctx.mkInt(1));
        }
        BoolExpr b = ctx.mkEq(var, tmp);
        modelCreator.addConstraint(b);
        return b;
    }

    private BoolExpr handleNewObject(ASTNewObject r) {
        return ctx.mkBool(true);
    }

    private IntExpr handleMethodCallExpression(ASTMethodCall r) {
        if(r.isMaxMin()) {
            return convertMaxMin(r);
        } else if(r.isTimeCritical()){
            //TODO
            return modelCreator.getTimeCall();
        }
        return modelCreator.createFunction(r.getMethodName());
    }

    private IntExpr convertMaxMin(ASTMethodCall r) {
        ArithExpr t0 = (ArithExpr) convert(r.getParameters().get(0), RetType.INT);
        if(r.getMethodName().equals("abs")){
            return (IntExpr) ModelCreator.abs(ctx, t0);
        }
        ArithExpr t1 = (ArithExpr) convert(r.getParameters().get(1), RetType.INT);
        if(r.getMethodName().equals("min")){
            return (IntExpr) ModelCreator.min2(ctx, t0, t1);
        }
        return (IntExpr) ModelCreator.max2(ctx, t0, t1);
    }

    private IntExpr handleLiteral(ASTLiteral r) {
        IntExpr e = modelCreator.createVariable(r.getValue());
        return e;
    }

    private IntExpr handleIdentifier(ASTIdentifier r) {
        IntExpr e = modelCreator.createVariable(r.getValue());
        return e;
    }

    private Expr handleConditional(ASTConditional r) {
        if(r.isTimeCritical()){
            return modelCreator.getTimeCall();
        }
        return ctx.mkInt(-1);
    }

    private Expr handleCast(ASTCast r, RetType t) {
        return convert(r.getExpr(), t);
    }

    private Expr handleBinary(ASTBinary r, RetType t) {
        Expr e = null;
        if(!r.isTimeCritical() && t == RetType.BOOL){
            if(!r.getLeft().isTimeCritical() && !r.getRight().isTimeCritical())
                return ctx.mkBool(true);
        }
        switch (r.getOp()){
            case mod:
                e = ctx.mkMod(
                        (IntExpr) convert(r.getLeft(), RetType.INT),
                        (IntExpr) convert(r.getRight(), RetType.INT)
                );
                break;
            case plus:
                e = ctx.mkAdd(
                        (ArithExpr) convert(r.getLeft(), RetType.INT),
                        (ArithExpr) convert(r.getRight(), RetType.INT)
                );
                break;
            case minus:
                e = ctx.mkSub(
                        (ArithExpr)convert(r.getLeft(), RetType.INT),
                        (ArithExpr) convert(r.getRight(), RetType.INT)
                );
                break;
            case less:
                e = ctx.mkLt(
                        (ArithExpr)convert(r.getLeft(), RetType.INT),
                        (ArithExpr) convert(r.getRight(), RetType.INT)
                );
                break;
            case lessEqual:
                e = ctx.mkLe(
                        (ArithExpr)convert(r.getLeft(), RetType.INT),
                        (ArithExpr) convert(r.getRight(), RetType.INT)
                );
                break;
            case greater:
                e = ctx.mkGt(
                        (ArithExpr)convert(r.getLeft(), RetType.INT),
                        (ArithExpr) convert(r.getRight(), RetType.INT)
                );
                break;
            case greaterEqual:
                e = ctx.mkGe(
                        (ArithExpr)convert(r.getLeft(), RetType.INT),
                        (ArithExpr) convert(r.getRight(), RetType.INT)
                );
                break;
            case equality:
                e = ctx.mkEq(
                        convert(r.getLeft(), t),
                        convert(r.getRight(), t)
                );
                break;
            case notEqual:
                e = ctx.mkNot(ctx.mkEq(
                        convert(r.getLeft(), t),
                        convert(r.getRight(), t)
                ));
                break;
            case mul:
                e = ctx.mkMul(
                        (ArithExpr)convert(r.getLeft(), RetType.INT),
                        (ArithExpr) convert(r.getRight(), RetType.INT)
                );
                break;
            case div:
                e = ctx.mkDiv(
                        (ArithExpr) convert(r.getLeft(), RetType.INT),
                        (ArithExpr) convert(r.getRight(), RetType.INT)
                );
                break;
            case and: {
                BoolExpr left, right;
                if (r.getLeft().isTimeCritical()) {
                    left = (BoolExpr) convert(r.getLeft(), RetType.BOOL);
                } else {
                    left = ctx.mkBool(true);
                }
                if (r.getRight().isTimeCritical()) {
                    right = (BoolExpr) convert(r.getRight(), RetType.BOOL);
                } else {
                    right = ctx.mkBool(true);
                }
                e = ctx.mkAnd(left, right);
                break;
            }
            case or: {
                BoolExpr left, right;
                if (r.getLeft().isTimeCritical()) {
                    left = (BoolExpr) convert(r.getLeft(), RetType.BOOL);
                } else {
                    left = ctx.mkBool(true);
                }
                if (r.getRight().isTimeCritical()) {
                    right = (BoolExpr) convert(r.getRight(), RetType.BOOL);
                } else {
                    right = ctx.mkBool(true);
                }
                e = ctx.mkOr(left, right);
                break;
            }
            /*case xor: {
                Expr ll = convert(r.getLeft(), t);
                Expr rr = convert(r.getRight(), t);
                BitVecExpr left;
                BitVecExpr right;
                if (ll instanceof IntExpr) {
                    left = ctx.mkInt2BV(32, (IntExpr) ll);
                } else {
                    left = (BitVecExpr) ll;
                }
                if (ll instanceof IntExpr) {
                    right = ctx.mkInt2BV(32, (IntExpr) rr);
                } else {
                    right = (BitVecExpr) rr;
                }
                e = ctx.mkBVXOR(left, right);
                break;
            }*/
            default:
                if(t == RetType.INT)
                    e = ctx.mkInt(0);
                else
                    e = ctx.mkBool(true);
        }
        return e;
    }

    private Expr handleAttributeAccess(ASTAttributeAccess r) {
        return modelCreator.createVariable(r.print());
    }

    private Expr handleAssignmentExpression(ASTAssignment r) {
        return modelCreator.createVariable(r.getLeft().print());
    }

    private Expr handleArrayInitializer(ASTArrayInitializer r) {
        return ctx.mkBool(true);
    }


    /***************************
     *          MODEL          *
     ***************************/


    private void storeError(String var, Stm where){
        if (this.saveModel)
            errors.add(new VariableNotCorrect(var, where, modelCreator.getLastMinModel(), modelCreator.getLastMaxModel()));
        else
            errors.add(new VariableNotCorrect(var, where));
    }

    private void handleWhile(While s) {
        for(String v : s.getTimeVars()){
            try {
                modelCreator.verifyVariable(v);
            } catch (ModelNotCorrect e) {
                storeError(v,s);
                //System.err.println("@" + s.getLine() + " " + e.getMessage());
            } catch (VarNotFoundException e) {
                //this is not a problem
            } catch (ModelTimeout e){
                timeout.add(e.getMessage());
            }
        }
        push();
        if(s.getExpr() != null){
            convert(s.getExpr());
        }
        for(Stm ss : s.getWhileBody()){
            convert(ss);
        }
        pop();
        if(s.getExpr() != null) {
            Expr b = convert(s.getExpr().getExpr().negate(), RetType.BOOL);
            if(b instanceof BoolExpr)
                modelCreator.addConstraint((BoolExpr) b);
        }
    }

    private void handleDoWhile(DoWhile s) {
        push();
        if(s.getExpr() != null){
            convert(s.getExpr());
        }
        for(Stm ss : s.getWhileBody()){
            convert(ss);
        }
        pop();
        if(s.getExpr() != null) {
            Expr b = convert(s.getExpr().getExpr().negate(), RetType.BOOL);
            if(b instanceof BoolExpr)
                modelCreator.addConstraint((BoolExpr) b);
        }
        for(String v : s.getTimeVars()){
            try {
                modelCreator.verifyVariable(v);
            } catch (ModelNotCorrect e) {
                if (this.saveModel)
                    errors.add(new VariableNotCorrect(v, s, modelCreator.getLastMinModel(), modelCreator.getLastMaxModel()));
                else
                    errors.add(new VariableNotCorrect(v, s));
                //System.err.println("@" + s.getLine() + " " + e.getMessage());
            } catch (VarNotFoundException e) {
                //this is not a problem
            } catch (ModelTimeout e){
                timeout.add(e.getMessage());
            }
        }
    }

    private void handleIf(If s) {
        push();
        if(s.getIfBody().size() > 0) {
            if(s.getExpr() != null)
                convert(s.getExpr());
            for(Stm ss : s.getIfBody()){
                convert(ss);
            }
        }
        pop();
        if(s.getElseBody().size() > 0){
            push();
            if(s.getExpr() != null){
                IASTRE expr = s.getExpr().getExpr().negate();
                //expr.setTimeCritical(true);
                Expr e = convert(expr, RetType.BOOL);
                BoolExpr b;
                if (e instanceof IntExpr) {
                    b = ctx.mkGe((ArithExpr) e, ctx.mkInt(0));
                } else {
                    b = (BoolExpr) e;
                }
                modelCreator.addConstraint(b);
            }
            for(Stm ss : s.getElseBody()){
                convert(ss);
            }
            pop();
        } else {
            //there is no else
            if(s.getExpr() != null){
                IASTRE expr = s.getExpr().getExpr().negate();
                //expr.setTimeCritical(true);
                Expr e = convert(expr, RetType.BOOL);
                BoolExpr b;
                if (e instanceof IntExpr) {
                    b = ctx.mkGe((ArithExpr) e, ctx.mkInt(0));
                } else {
                    b = (BoolExpr) e;
                }
                modelCreator.addConstraint(b);
            }
        }

    }

    private void handleExpression(Expression s) {
        Expr e = convert(s.getExpr(), RetType.BOOL);
        BoolExpr b;
        if (e instanceof IntExpr) {
            b = ctx.mkGe((ArithExpr) e, ctx.mkInt(0));
        } else {
            b = (BoolExpr) e;
        }
        modelCreator.addConstraint(b);
    }

    private void handleMethodCall(MethodCall s) {
        for(String v : s.getVariables()){
            try {
                modelCreator.verifyVariable(v);
            } catch (ModelNotCorrect e) {
                storeError(v,s);
                //System.err.println("@" + s.getLine() + " " + e.getMessage());
            } catch (VarNotFoundException e) {
                //this is not a problem
            } catch (ModelTimeout e){
                timeout.add(e.getMessage());
            }
        }
        convert(s.getMethodCall(), RetType.INT);
    }

    private void handleAssignment(Assignment s) {
        try {
            IntExpr v = modelCreator.createVariable(s.getLeft());
            if (s.getRight() != null) {
                Expr e = convert(s.getRight(), RetType.INT);
                if (e instanceof IntExpr) {
                    BoolExpr b = ctx.mkEq(v, e);
                    modelCreator.addConstraint(b);
                }
            }
        }catch(Exception x){
            System.out.println("Error in assignment line: " + s.getLine());
            throw x;
        }
    }

    private void push(){
        modelCreator.getOpt().Push();
    }

    private void pop(){
        pushModel.add(modelCreator.getOpt().toString());
        modelCreator.getOpt().Pop();
    }

    private void notYet(IASTToken s){
        if(s != null)
            System.err.println("Not Yet Implemented :: [" + s.getStart() +"]" + s.getCode() + " @ " + s.getLine() + "--" + cause[2]);
    }

}
