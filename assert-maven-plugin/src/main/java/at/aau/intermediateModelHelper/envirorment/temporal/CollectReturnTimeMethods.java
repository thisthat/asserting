package at.aau.intermediateModelHelper.envirorment.temporal;

import intermediateModel.interfaces.IASTMethod;
import intermediateModel.interfaces.IASTVar;
import intermediateModel.structure.*;
import intermediateModel.types.definition.Duration;
import intermediateModel.types.definition.TimeType;
import intermediateModel.types.definition.Timestamp;
import intermediateModel.types.definition.Unknown;
import intermediateModel.types.rules.TypeResolver;
import intermediateModel.visitors.ExtractTimeAttribute;
import intermediateModel.visitors.interfaces.ParseIM;
import intermediateModelHelper.CheckExpression;
import intermediateModelHelper.envirorment.Env;
import intermediateModelHelper.envirorment.temporal.structure.TimeTypes;
import intermediateModelHelper.envirorment.temporalTypes.TemporalTypes;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CollectReturnTimeMethods extends ParseIM {
    static TemporalTypes ti = TemporalTypes.getInstance();

    static {
        new File("config").mkdir();
    }

    boolean store = false;
    String storeName = "";
    List<TimeTypes> output;
    IASTMethod lastMethod = null;

    public CollectReturnTimeMethods(ASTClass _class, boolean store, String storeName) {
        super(_class);
        this.store = store;
        this.storeName = storeName;
    }

    public CollectReturnTimeMethods(boolean store, String storeName) {
        this.store = store;
        this.storeName = storeName;
    }

    @Override
    protected void analyzeMethod(IASTMethod method, Env e) {
        IASTMethod bck = lastMethod;
        lastMethod = method;
        super.analyzeMethod(method, e);
        lastMethod = bck;
    }

    public List<TimeTypes> index(ASTClass c) {
        output = new ArrayList<>();
        super.set_class(c);
        Env base = super.createBaseEnv(c);
        ExtractTimeAttribute timeAttribute = new ExtractTimeAttribute(c);
        for (IASTVar p : timeAttribute.getTimeAttributes()) {
            if (base.existVarName(p.getName())) {
                IASTVar v = base.getVar(p.getName());
                v.setTimeCritical(true);
            }
        }
        //first constructor
        for (IASTMethod m : c.getMethods()) {
            if (m instanceof ASTMethod) continue;
            Env eMethod = new Env(base);
            eMethod = CheckExpression.checkPars(m.getParameters(), eMethod);
            analyzeMethod(m, eMethod);
            //analyze(m.getStms(), eMethod );
        }

        //then methods
        for (IASTMethod m : c.getMethods()) {
            if (m instanceof ASTConstructor) continue;
            String ret = m.getReturnType();
            if (ret.equals("long") || ret.equals("int") || ret.equals("Long") || ret.equals("Integer")) {
                Env eMethod = new Env(base);
                eMethod = CheckExpression.checkPars(m.getParameters(), eMethod);
                analyzeMethod(m, eMethod);
            }
            //analyze(m.getStms(), eMethod );
        }
        if (store) {
            String full = "config/" + this.storeName + "_types.csv";
            String timestamp = "config/" + this.storeName + "_rt_t.csv";
            String duration = "config/" + this.storeName + "_rt_d.csv";
            try {
                writeFile(full, this.output);
            } catch (IOException e) {
                System.err.println("Cannot write " + full + " file");
                System.err.println(e.getMessage());
            }
            List<TimeTypes> outputT = new ArrayList<>();
            List<TimeTypes> outputD = new ArrayList<>();
            for (TimeTypes t : output) {
                if (t.getTimeType() instanceof Timestamp) {
                    outputT.add(t);
                } else if (t.getTimeType() instanceof Duration) {
                    outputD.add(t);
                }
            }
            try {
                writeFile(timestamp, outputT);
            } catch (IOException e) {
                System.err.println("Cannot write " + timestamp + " file");
                System.err.println(e.getMessage());
            }
            try {
                writeFile(duration, outputD);
            } catch (IOException e) {
                System.err.println("Cannot write " + duration + " file");
                System.err.println(e.getMessage());
            }
        }
        return output;
    }

    private void writeFile(String filename, List<TimeTypes> output) throws IOException {
        File f = new File(filename);
        boolean exists = f.exists();
        BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true));
        if (!exists)
            writer.write("Class;Name;Signature;Ret Type\n");
        for (TimeTypes t : output) {
            writer.write(t.toString());
            writer.write("\n");
            writer.flush();
        }
        writer.close();
    }

    @Override
    protected void analyzeASTRE(ASTRE r, Env env) {
        super.analyzeASTRE(r, env);
        CheckExpression.checkRE(r, env);
    }

    @Override
    protected void analyzeASTReturn(ASTReturn elm, Env env) {

        ASTRE re = elm.getExpr();
        if (re != null && re.getExpression() != null && //sanity checks
                CheckExpression.checkIt(re.getExpression(), env)) {
            TimeType tt = new Unknown();
            try {
                tt = TypeResolver.resolveTimerType(re.getExpression(), env);
            } catch (Exception ex) {
                // ignore errors now
                System.err.println(this._class.getPath());
                ex.printStackTrace();
            }
            //there is time!
            TimeTypes t = new TimeTypes(this._class.fullName(), lastMethod.getName(), lastMethod.getSignature(), tt);
            output.add(t);
            //check interfaces if method and not constructor
            if (lastMethod instanceof ASTMethod) {
                ASTMethod method = (ASTMethod) lastMethod;
                for (ASTInterfaceMethod im : this._class.getInterfaceMethods(method)) {
                    TimeTypes tinf = new TimeTypes(im.getInterfaceName(), im.getMethodName(), im.getSignature(), tt);
                    if (!output.contains(tinf))
                        output.add(tinf);
                }
            }
        }
    }

}
