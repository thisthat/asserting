package at.aau.asserting.logics.FOLConstructors;

import at.aau.asserting.Formula;

import java.util.ArrayList;
import java.util.List;

public class MUL extends Formula {
    Formula lhs;
    Formula rhs;

    public MUL(Formula lhs, Formula rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }

    public String print() {
        return String.format("(* %s %s)", this.lhs.print(), this.rhs.print());
    }

    @Override
    public Formula negate() {
        return new MUL( this.lhs.negate(), this.rhs.negate() );
    }

    @Override
    public Formula noQuantifier() {
        return this;
    }

    @Override
    public List<String> getVar() {
        List<String> out = new ArrayList<>();
        out.addAll(lhs.getVar());
        out.addAll(rhs.getVar());
        return out;
    }
}
