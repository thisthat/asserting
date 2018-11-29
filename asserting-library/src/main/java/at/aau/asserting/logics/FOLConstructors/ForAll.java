package at.aau.asserting.logics.FOLConstructors;

import at.aau.asserting.Formula;

public class ForAll extends Formula implements Quantifier {
    Variable var;
    Formula formula;

    public ForAll(Variable var, Formula f) {
        this.var = var;
        this.formula = f;
    }


    @Override
    public String print() {
        return String.format("(forall ((%s Int)) %s)", var.print(), formula.print());
    }

    @Override
    public Formula negate() {
        return new Exists(var, formula.negate());
    }


    @Override
    public Formula noQuantifier() {
        return formula.noQuantifier();
    }
}
