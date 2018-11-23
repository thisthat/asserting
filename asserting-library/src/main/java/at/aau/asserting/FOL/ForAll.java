package at.aau.asserting.FOL;

import at.aau.asserting.Formula;

public class ForAll extends Formula {
    String var;

    private ForAll(String var) {
        this.var = var;
    }

    public static ForAll ForAll(String v){
        return new ForAll(v);
    }
}
