package at.aau.asserting.logics.FOLConstructors;

import at.aau.asserting.Formula;

public class LT extends Formula {
    Formula lhs;
    Formula rhs;

    public LT(Formula lhs, Formula rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }

    public String print() {
        return String.format("(< %s %s)", this.lhs.print(), this.rhs.print());
    }

    @Override
    public Formula negate() {
        return new GTE(this.lhs.negate(), this.rhs.negate());
    }
}