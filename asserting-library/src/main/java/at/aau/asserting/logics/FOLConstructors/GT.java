package at.aau.asserting.logics.FOLConstructors;

import at.aau.asserting.Formula;

public class GT extends Formula {
    Formula lhs;
    Formula rhs;

    public GT(Formula lhs, Formula rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }

}
