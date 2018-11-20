package at.aau.intermediateModel.visitors.creation.filter;

import at.aau.intermediateModel.interfaces.IASTMethod;
import at.aau.intermediateModel.structure.ASTClass;
import at.aau.intermediateModel.structure.ASTConstructor;
import at.aau.intermediateModel.structure.ASTIf;
import at.aau.intermediateModel.structure.ASTMethod;
import at.aau.intermediateModel.visitors.DefaultASTVisitor;
import at.aau.intermediateModelHelper.Config;

import java.util.ArrayList;
import java.util.List;

public class ElseIf implements Filter {

    public static final ElseIf filter = new ElseIf();
    private ElseIf(){}

    static class Visitor extends DefaultASTVisitor {
        int depth = 0;
        private int max = 0;

        @Override
        public void enterASTIf(ASTIf elm) {
            depth++;
            max = Math.max(depth, max);
        }

        @Override
        public void exitASTIf(ASTIf elm) {
            depth--;
        }

        public void reset(){
            max = 0;
        }
        public int getMaxDepth(){
            return max;
        }
    }
    private static final Visitor v = new Visitor();

    int threshold = 8;
    @Override
    public void filter(ASTClass c) {
        List<ASTMethod> methods = new ArrayList<>();
        for(IASTMethod mm : c.getMethods()){
            if(mm instanceof ASTConstructor) continue;
            ASTMethod m = (ASTMethod) mm;
            v.reset();
            m.visit(v);
            int d = v.getMaxDepth();
            if(d > threshold)
                methods.add(m);
        }
        for(ASTMethod m : methods){
            if(Config.isDebug()) {
                System.out.print("REMOVED: ");
                System.out.println(m.getName());
            }
            c.getMethods().remove(m);
        }
    }
}
