package at.aau.asserting.logics.FOLConstructors;

import at.aau.asserting.Formula;

public class Variable extends Formula {
    String var;

    public Variable(String v) {
        this.var = v;
    }

    public String print() {
        return this.var;
    }

    @Override
    public Formula negate() {
        return this;
    }


    @Override
    public Formula noQuantifier() {
        return this;
    }
}
