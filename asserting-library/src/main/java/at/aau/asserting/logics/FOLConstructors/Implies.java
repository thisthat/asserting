package at.aau.asserting.logics.FOLConstructors;

import at.aau.asserting.Formula;

public class Implies extends Formula {
    Formula lhs;
    Formula rhs;

    public Implies(Formula lhs, Formula rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }


}
