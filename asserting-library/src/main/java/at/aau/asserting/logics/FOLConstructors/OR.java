package at.aau.asserting.logics.FOLConstructors;

import at.aau.asserting.Formula;

public class OR extends Formula {
    Formula lhs;
    Formula rhs;

    public OR(Formula lhs, Formula rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }

    public String print() {
        return String.format("(or %s %s)", this.lhs.print(), this.rhs.print());
    }

    @Override
    public Formula negate() {
        return new AND( this.lhs.negate(), this.rhs.negate() );
    }

    @Override
    public Formula noQuantifier() {
        return this;
    }
}
