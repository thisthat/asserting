package at.aau.asserting.logics.FOLConstructors;

import at.aau.asserting.Formula;

import java.util.ArrayList;
import java.util.List;

public class Implies extends Formula {
    Formula lhs;
    Formula rhs;

    public Implies(Formula lhs, Formula rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }


    public String print() {
        return (new OR(new Not(lhs), rhs)).print();
    }

    @Override
    public Formula negate() {
        return new AND(this.lhs, this.rhs.negate());
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
