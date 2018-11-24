package at.aau.asserting.logics.FOLConstructors;

import at.aau.asserting.Formula;

public class ForAll extends Formula {
    Variable var;
    Formula formula;

    public ForAll(Variable var, Formula f) {
        this.var = var;
        this.formula = f;
    }


}
