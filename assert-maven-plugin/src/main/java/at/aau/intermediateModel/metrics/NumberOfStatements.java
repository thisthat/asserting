package at.aau.intermediateModel.metrics;

import at.aau.intermediateModel.interfaces.IASTMethod;
import at.aau.intermediateModel.structure.*;
import at.aau.intermediateModel.visitors.DefaultASTVisitor;

public class NumberOfStatements {

    private static class CountStatement extends DefaultASTVisitor {
        long counter;
        public CountStatement() {
            counter = 0;
            this.setExcludeHiddenClass(false);
            this.setExcludePars(true);
        }

        public long getCounter() {
            return counter;
        }

        @Override
        public void enterASTHiddenClass(ASTHiddenClass astHiddenClass) {
            counter++;
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
            counter -= elm.getInit().size();
            counter -= elm.getPost().size();
        }

        @Override
        public void enterASTForEach(ASTForEach elm) {
            if(elm.getExpr() == null) {
                counter++;
            }
        }

        @Override
        public void enterASTIf(ASTIf elm) {
            if(elm.getElseBranch() != null){
                counter++;
            }
        }

        @Override
        public void enterASTRE(ASTRE elm) {
            counter++;
        }

        @Override
        public void enterASTReturn(ASTReturn elm) {
            if(elm.getExpr() == null)
                counter++;
        }

        @Override
        public void enterASTSwitch(ASTSwitch elm) {
            //counter++;
            counter += elm.getCases().size();
        }

        @Override
        public void enterASTSynchronized(ASTSynchronized elm) {
            //counter++;
        }

        @Override
        public void enterASTThrow(ASTThrow elm) {
            if(elm.getExpr() == null)
                counter++;
        }

        @Override
        public void enterASTTry(ASTTry elm) {
            counter++;
            //counter += elm.getCatchBranch().size();
            if(elm.getFinallyBranch() != null){
                counter++;
            }
        }

        @Override
        public void enterASTTryResources(ASTTryResources elm) {
            counter++;
            //counter += elm.getCatchBranch().size();
            if(elm.getFinallyBranch() != null){
                counter++;
            }
        }

        @Override
        public void enterASTVariable(ASTVariable elm) {
            counter++;
        }

        @Override
        public void enterASTWhile(ASTWhile elm) {
            counter++;
        }
    }

    public static long get(IASTMethod m){
        CountStatement c = new CountStatement();
        m.visit(c);
        return Math.max(c.getCounter() - m.getParameters().size(), 0);
    }

}
