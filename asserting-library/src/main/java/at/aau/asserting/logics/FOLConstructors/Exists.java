package at.aau.asserting.logics.FOLConstructors;

import at.aau.asserting.Formula;

import java.util.ArrayList;
import java.util.List;

public class Exists extends Formula implements Quantifier {

    Variable var;
    Formula formula;

    public Exists(Variable var, Formula f) {
        this.var = var;
        this.formula = f;
    }


    @Override
    public String print() {
        return String.format("(exists ((%s Int)) %s)", var.print(), formula.print() );
    }

    @Override
    public Formula negate() {
        return new ForAll(var, formula.negate());
    }


    @Override
    public Formula noQuantifier() {
        return formula.noQuantifier();
    }

    @Override
    public List<String> getVar() {
        List<String> out = new ArrayList<>();
        out.add(var.var);
        out.addAll(formula.getVar());
        return out;
    }
}
