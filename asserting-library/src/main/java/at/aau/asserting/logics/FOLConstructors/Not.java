package at.aau.asserting.logics.FOLConstructors;

import at.aau.asserting.Formula;

public class Not extends Formula {
    Formula formula;

    public Not(Formula f) {
        this.formula = f;
    }

}
