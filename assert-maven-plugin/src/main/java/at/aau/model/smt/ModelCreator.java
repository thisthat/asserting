package at.aau.model.smt;

import com.microsoft.z3.*;
import at.aau.model.smt.exception.FunctionNotFoundException;
import at.aau.model.smt.exception.ModelNotCorrect;
import at.aau.model.smt.exception.ModelTimeout;
import at.aau.model.smt.exception.VarNotFoundException;

import java.util.HashMap;

/**
 * Created by giovanni on 10/07/2017.
 */
public class ModelCreator {

    private IntExpr max_val;
    private IntExpr over_max_val;
    private IntExpr min_val;
    private IntExpr time;
    private FuncDecl timeFuncDec;

    public static String _MaxVal = "9223372036854775807";
    public static String _NotValidMax = "9223372036854775808";
    public static String _NotValidMin = "-9223372036854775808";

    private String lastMinModel = "";
    private String lastMaxModel = "";

    public static boolean _debug_ = false;
    public static boolean checkModel = false;

    Context ctx;
    Optimize opt;

    HashMap<String,IntExpr> vars = new HashMap<>();
    HashMap<String,IntExpr> functions = new HashMap<>();

    public ModelCreator() {
        HashMap<String, String> cfg = new HashMap<>();
        cfg.put("model", "true");
        ctx = new Context(cfg);
        opt = ctx.mkOptimize();
        insertStandardDefinition();
    }

    private void insertStandardDefinition(){
        IntExpr maxCorrectUpper = ctx.mkInt( _MaxVal );
        IntExpr notCorrectUpper = ctx.mkInt(_NotValidMax);

        max_val= ctx.mkIntConst("max_val");
        over_max_val= ctx.mkIntConst("over_max_val");
        min_val= ctx.mkIntConst("min_val");

        //Bind every var/fun to its value and correct range

        BoolExpr std_ok = ctx.mkEq(max_val, maxCorrectUpper);
        BoolExpr std_not = ctx.mkEq(over_max_val, notCorrectUpper);
        opt.Add(std_ok);
        opt.Add(std_not);

        BoolExpr min = ctx.mkEq(min_val, ctx.mkInt(_NotValidMin));
        opt.Add(min);

        timeFuncDec = ctx.mkFuncDecl("time", new IntSort[0], ctx.getIntSort());
        time = (IntExpr) ctx.mkApp(timeFuncDec, new IntExpr[0]);
        BoolExpr t = ctx.mkAnd(
                ctx.mkGe(time, ctx.mkInt(0)),
                ctx.mkLe(time, max_val)
                );
        opt.Add(t);

    }

    public static Expr max2(Context ctx, ArithExpr x, ArithExpr y)
            throws Z3Exception {
        return ctx.mkITE(ctx.mkLe(x, y), y, x);
    }
    public static Expr min2(Context ctx, ArithExpr x, ArithExpr y)
            throws Z3Exception {
        return ctx.mkITE(ctx.mkLe(x, y), x, y);
    }
    public static Expr abs(Context ctx, ArithExpr x)
            throws Z3Exception {
        return ctx.mkITE(ctx.mkLe(x, ctx.mkInt(0)), ctx.mkUnaryMinus(x), x);
    }

    public IntExpr getTimeCall() {
        return time;
    }

    public IntExpr createFunction(String methodName) {
        IntExpr name;
        try {
            name = getFunction(methodName);
        } catch (FunctionNotFoundException e) {
            FuncDecl f = ctx.mkFuncDecl(methodName, new IntSort[0], ctx.getIntSort());
            name = (IntExpr) ctx.mkApp(f, new IntExpr[0]);
            functions.put(methodName, name);
        }
        return name;
    }

    public IntExpr createVariable(String name){
        IntExpr v;
        try {
            v = getVar(name);
        } catch (VarNotFoundException e) {
            if(name.matches("[0-9]+")){
                v = ctx.mkInt(name);
            } else if(name.matches("[0-9]+\\.[0-9]+")) {
                v = ctx.mkReal2Int(ctx.mkReal(name));
            } else if(name.matches("[0-9]+L")){
                String val = name.substring(0, name.length()-1);
                v = ctx.mkInt(val);
            } else {
                v = ctx.mkIntConst(name);
                BoolExpr t = ctx.mkLe(min_val, v);
                opt.Add(t);
                t = ctx.mkLe(v, over_max_val);
                opt.Add(t);
            }
            vars.put(name, v);
        }
        return v;
    }

    public IntExpr getVar(String name) throws VarNotFoundException {
        if(!vars.containsKey(name)){
            throw new VarNotFoundException();
        }
        return vars.get(name);
    }
    public IntExpr getFunction(String name) throws FunctionNotFoundException {
        if(!functions.containsKey(name)){
            throw new FunctionNotFoundException();
        }
        return functions.get(name);
    }

    public void verifyVariable(String name) throws ModelNotCorrect, VarNotFoundException, ModelTimeout {
        if(this.checkModel) {
            IntExpr v = this.getVar(name);
            this.verifyVariable(v);
        }
    }

    public void verifyVariable(IntExpr v) throws ModelNotCorrect, ModelTimeout {
        boolean min = verify_min(v);
        //boolean max = verify_max(v);
        if(!min)
            throw new ModelNotCorrect(v.getSExpr());
    }

    private boolean verify_max(IntExpr v) throws ModelNotCorrect, ModelTimeout {
        opt.Push();
        Optimize.Handle mx = opt.MkMaximize(v);
        opt.Check();
        this.lastMaxModel = opt.toString();
        if(_debug_){
            System.out.println("MAX: " + v.getSExpr() + " = " + mx.toString());
        }
        boolean f = validValue(mx.toString(), v.toString());
        opt.Pop();
        return f;
    }

    private boolean verify_min(IntExpr v) throws ModelNotCorrect, ModelTimeout {
        opt.Push();
        Optimize.Handle mx = opt.MkMinimize(v);
        Params p = ctx.mkParams();
        p.add("timeout", 9000);
        opt.setParameters(p);
        opt.Check();
        this.lastMinModel = opt.toString();
        if(_debug_){
            System.out.println("MIN: " + v.getSExpr() + " = " + mx.toString());
        }
        boolean f = validValue(mx.toString(), v.toString());
        opt.Pop();
        return f;
    }

    private boolean validValue(String val, String var) throws ModelTimeout {
        if(val.equals("(* (- 1) oo)")){
            throw new ModelTimeout(var);
        }
        if( val.equals( _NotValidMax )){
            return false;
        }
        if( val.startsWith( "-" )){
            return false;
        }
        return true;
    }

    public void addConstraint(BoolExpr t){
        opt.Add(t);
    }

    public Model getModel() {
        opt.Check();
        return opt.getModel();
    }

    public Context getCtx() {
        return ctx;
    }

    public Optimize getOpt() {
        return opt;
    }

    public String getLastMinModel() {
        return lastMinModel;
    }

    public String getLastMaxModel() {
        return lastMaxModel;
    }
}
