package at.aau.asserting.logics.FOLConstructors;

import at.aau.asserting.Formula;

public class LT extends Formula {
    Formula lhs;
    Formula rhs;

    public LT(Formula lhs, Formula rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }

}
