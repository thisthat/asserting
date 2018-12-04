package at.aau.asserting.logics.FOLConstructors;

import at.aau.asserting.Formula;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    public List<String> getVar() {
        List<String> out = new ArrayList<>();
        out.add(var);
        return out;
    }
}
