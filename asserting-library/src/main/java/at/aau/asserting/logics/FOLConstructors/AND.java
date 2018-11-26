package at.aau.asserting.logics.FOLConstructors;

import at.aau.asserting.Formula;

public class AND extends Formula {
    Formula lhs;
    Formula rhs;

    public AND(Formula lhs, Formula rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }

    public String print() {
        return String.format("(and %s %s)", this.lhs.print(), this.rhs.print());
    }

    @Override
    public Formula negate() {
        return new OR( this.lhs.negate(), this.rhs.negate() );
    }
}
