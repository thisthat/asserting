package at.aau.asserting.logics.FOLConstructors;

import at.aau.asserting.Formula;

import java.util.ArrayList;
import java.util.List;

public class Not extends Formula {
    Formula formula;

    public Not(Formula f) {
        this.formula = f;
    }

    public String print() {
        return String.format("(not %s)", this.formula.print());
    }

    @Override
    public Formula negate() {
        return formula;
    }

    @Override
    public Formula noQuantifier() {
        return this;
    }
    @Override
    public List<String> getVar() {
        List<String> out = new ArrayList<>();
        out.addAll(formula.getVar());
        return out;
    }
}
