package at.aau.asserting.logics.FOLConstructors;

import at.aau.asserting.Formula;

public class AND extends Formula {
    Formula lhs;
    Formula rhs;

    public AND(Formula lhs, Formula rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }


}
