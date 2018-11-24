package at.aau.asserting.logics.FOLConstructors;

import at.aau.asserting.Formula;

public class OR extends Formula {
    Formula lhs;
    Formula rhs;

    public OR(Formula lhs, Formula rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }

}
