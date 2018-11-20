package at.aau.intermediateModel.metrics;

import at.aau.intermediateModel.interfaces.IASTMethod;
import at.aau.intermediateModel.interfaces.IASTStm;
import at.aau.intermediateModel.structure.*;
import at.aau.intermediateModel.visitors.DefaultASTVisitor;

import java.util.List;

public class CyclomaticComplexity {

    private static class Count extends DefaultASTVisitor {
        long counter;
        int s, e;

        public Count(IASTMethod m) {
            counter = 1;
            this.setExcludeHiddenClass(false);
            this.setExcludePars(true);
            List<IASTStm> list = m.getStms();
            if(list.size() > 0){
                IASTStm last = list.get(list.size() - 1);
                s = last.getStart();
                e = last.getEnd();
            }
        }

        public long getCounter() {
            return counter;
        }

        @Override
        public void enterASTReturn(ASTReturn elm) {
            //avoid last return
            if(elm.getStart() == s && elm.getEnd() == e){
                return;
            }
            counter++;
        }

        @Override
        public void enterASTIf(ASTIf elm) {
            counter++;
            if(elm.getElseBranch() != null){
                counter++;
            }
        }

        @Override
        public void enterASTSwitch(ASTSwitch elm) {
            counter += elm.getCases().size();
        }

        @Override
        public void enterASTBreak(ASTBreak elm) {
            counter++;
        }

        @Override
        public void enterASTContinue(ASTContinue elm) {
            counter++;
        }

        @Override
        public void enterASTDoWhile(ASTDoWhile elm) {
            counter++;
        }

        @Override
        public void enterASTFor(ASTFor elm) {
            counter++;
        }

        @Override
        public void enterASTForEach(ASTForEach elm) {
            counter++;
        }

        @Override
        public void enterASTWhile(ASTWhile elm) {
            counter++;
        }

        @Override
        public void enterASTThrow(ASTThrow elm) {
            counter++;
        }

        @Override
        public void enterASTTry(ASTTry elm) {
            counter += elm.getCatchBranch().size();
            if(elm.getFinallyBranch() != null){
                counter++;
            }
        }

        @Override
        public void enterASTTryResources(ASTTryResources elm) {
            counter += elm.getCatchBranch().size();
            if(elm.getFinallyBranch() != null){
                counter++;
            }
        }
    }

    public static long get(IASTMethod m){
        Count c = new Count(m);
        m.visit(c);
        return c.getCounter();
    }

}
