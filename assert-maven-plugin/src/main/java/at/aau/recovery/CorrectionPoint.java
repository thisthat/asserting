package at.aau.recovery;

import at.aau.intermediateModel.structure.expression.ASTIdentifier;
import at.aau.intermediateModel.structure.expression.ASTLiteral;
import at.aau.intermediateModel.structure.expression.ASTMethodCall;
import at.aau.intermediateModel.visitors.DefualtASTREVisitor;

import java.util.ArrayList;
import java.util.List;

public class CorrectionPoint {
    int line;
    int start, end;
    List<String> vars = new ArrayList<>();


    public CorrectionPoint(ASTMethodCall elm) {
        this.start = elm.getStart();
        this.end = elm.getEnd();
        this.line = elm.getLine();
        elm.visit(new DefualtASTREVisitor(){
            @Override
            public void enterASTLiteral(ASTLiteral elm) {
                String v = elm.getValue().replace("\"","");
                if(v.matches("[0-9].*"))
                    return;
                if(!vars.contains(v))
                    vars.add(v);
            }

            @Override
            public void enterASTIdentifier(ASTIdentifier elm) {
                String v = elm.getValue();
                if(!vars.contains(v))
                    vars.add(v);
            }
        });
        vars.remove("AssertLibrary");
    }


    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public int getLine() {
        return line;
    }

    public List<String> getVars() {
        return vars;
    }

    @Override
    public String toString() {
        return "CorrectionPoint{" +
            "line=" + line +
            ", start=" + start +
            ", end=" + end +
            '}';
    }
}
