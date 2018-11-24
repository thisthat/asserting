package at.aau.asserting.logics.FOLConstructors;

import at.aau.asserting.Formula;

public class Exists extends Formula {
    Variable var;
    Formula formula;

    public Exists(Variable var, Formula f) {
        this.var = var;
        this.formula = f;
    }


}
