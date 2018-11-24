package at.aau.asserting.logics.FOLConstructors;

import at.aau.asserting.Formula;

public class LTE extends Formula {
    Formula lhs;
    Formula rhs;

    public LTE(Formula lhs, Formula rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }

}
