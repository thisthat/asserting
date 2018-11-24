package at.aau.asserting.logics.FOLConstructors;

import at.aau.asserting.Formula;

public class GTE extends Formula {
    Formula lhs;
    Formula rhs;

    public GTE(Formula lhs, Formula rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }

}
