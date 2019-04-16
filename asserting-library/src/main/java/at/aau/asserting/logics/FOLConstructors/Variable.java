package at.aau.asserting.logics.FOLConstructors;

import at.aau.asserting.Formula;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    public List<String> getVar() {
        List<String> out = new ArrayList<>();
        if(validVar(var))
            out.add(var);
        return out;
    }
}
