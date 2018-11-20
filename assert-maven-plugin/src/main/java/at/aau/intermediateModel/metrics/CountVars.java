package at.aau.intermediateModel.metrics;

import intermediateModel.interfaces.IASTMethod;
import intermediateModel.structure.*;
import intermediateModel.structure.expression.ASTIdentifier;
import intermediateModel.visitors.DefaultASTVisitor;

import java.util.ArrayList;
import java.util.List;

public class CountVars {

    private static class CountVar extends DefaultASTVisitor {
        long counter;
        List<String> identifiers = new ArrayList<>();
        public CountVar(IASTMethod m, ASTClass c) {
            this.setExcludeHiddenClass(false);
            this.setExcludePars(true);
            long init = m.getParameters().size() + c.getAttributes().size();
            counter = init;
            identifiers.clear();
        }

        public long getCounter() {
            return counter + identifiers.size();
        }

        @Override
        public void enterASTVariable(ASTVariable elm) {
            counter++;
        }

        @Override
        public void enterASTIdentifier(ASTIdentifier elm) {
            if(!identifiers.contains(elm.getValue())){
                identifiers.add(elm.getValue());
            }
        }
    }

    public static long get(IASTMethod m, ASTClass _c){
        CountVar c = new CountVar(m,_c);
        m.visit(c);
        return c.getCounter();
    }

}
