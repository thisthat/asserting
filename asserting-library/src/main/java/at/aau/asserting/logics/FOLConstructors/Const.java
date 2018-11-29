package at.aau.asserting.logics.FOLConstructors;

import at.aau.asserting.Formula;

public class Const extends Formula {
    String var;

    public Const(String v) {
        this.var = v;
    }
    public Const(Number v) {
        this.var = v + "";
    }

    public String print() {
        return var;
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
