package at.aau.asserting.logics;

import at.aau.asserting.logics.FOLConstructors.*;
import at.aau.asserting.Formula;

public class FOL {
    public static Exists Exists(String v, Formula f){
        return new Exists(new Variable(v), f);
    }

    public static ForAll ForAll(String v, Formula f){
        return new ForAll(new Variable(v), f);
    }


    public static Implies Implies(Formula lhs, Formula rhs){
        return new Implies(lhs, rhs);
    }
    public static Implies Implies(Formula lhs, String rhs){
        return new Implies(lhs, new Variable(rhs));
    }
    public static Implies Implies(String lhs, Formula rhs){
        return new Implies(new Variable(lhs), rhs);
    }
    public static Implies Implies(String lhs, String rhs){
        return new Implies(new Variable(lhs), new Variable(rhs));
    }


    public static AND AND(Formula lhs, Formula rhs){
        return new AND(lhs, rhs);
    }
    public static AND AND(Formula lhs, String rhs){
        return new AND(lhs, new Variable(rhs));
    }
    public static AND AND(String lhs, Formula rhs){
        return new AND(new Variable(lhs), rhs);
    }
    public static AND AND(String lhs, String rhs){
        return new AND(new Variable(lhs), new Variable(rhs));
    }


    public static OR OR(Formula lhs, Formula rhs){
        return new OR(lhs, rhs);
    }
    public static OR OR(Formula lhs, String rhs){
        return new OR(lhs, new Variable(rhs));
    }
    public static OR OR(String lhs, Formula rhs){
        return new OR(new Variable(lhs), rhs);
    }
    public static OR OR(String lhs, String rhs){
        return new OR(new Variable(lhs), new Variable(rhs));
    }


    public static GT GT(Formula lhs, Formula rhs){
        return new GT(lhs, rhs);
    }
    public static GT GT(Formula lhs, String rhs){
        return new GT(lhs, new Variable(rhs));
    }
    public static GT GT(String lhs, Formula rhs){
        return new GT(new Variable(lhs), rhs);
    }
    public static GT GT(String lhs, String rhs){
        return new GT(new Variable(lhs), new Variable(rhs));
    }
    public static GT GT(Formula lhs, Number rhs){
        return new GT(lhs, new Const(rhs));
    }
    public static GT GT(Number lhs, Formula rhs){
        return new GT(new Const(lhs), rhs);
    }
    public static GT GT(Number lhs, Number rhs){
        return new GT(new Const(lhs), new Const(rhs));
    }
    public static GT GT(String lhs, Number rhs){
        return new GT(new Variable(lhs), new Const(rhs));
    }
    public static GT GT(Number lhs, String rhs){
        return new GT(new Const(lhs), new Variable(rhs));
    }

    
    public static GTE GTE(Formula lhs, Formula rhs){
        return new GTE(lhs, rhs);
    }
    public static GTE GTE(Formula lhs, String rhs){
        return new GTE(lhs, new Variable(rhs));
    }
    public static GTE GTE(String lhs, Formula rhs){
        return new GTE(new Variable(lhs), rhs);
    }
    public static GTE GTE(String lhs, String rhs){
        return new GTE(new Variable(lhs), new Variable(rhs));
    }
    public static GTE GTE(Formula lhs, Number rhs){
        return new GTE(lhs, new Const(rhs));
    }
    public static GTE GTE(Number lhs, Formula rhs){
        return new GTE(new Const(lhs), rhs);
    }
    public static GTE GTE(Number lhs, Number rhs){
        return new GTE(new Const(lhs), new Const(rhs));
    }
    public static GTE GTE(String lhs, Number rhs){
        return new GTE(new Variable(lhs), new Const(rhs));
    }
    public static GTE GTE(Number lhs, String rhs){
        return new GTE(new Const(lhs), new Variable(rhs));
    }


    public static LT LT(Formula lhs, Formula rhs){
        return new LT(lhs, rhs);
    }
    public static LT LT(Formula lhs, String rhs){
        return new LT(lhs, new Variable(rhs));
    }
    public static LT LT(String lhs, Formula rhs){
        return new LT(new Variable(lhs), rhs);
    }
    public static LT LT(String lhs, String rhs){
        return new LT(new Variable(lhs), new Variable(rhs));
    }
    public static LT LT(Formula lhs, Number rhs){
        return new LT(lhs, new Const(rhs));
    }
    public static LT LT(Number lhs, Formula rhs){
        return new LT(new Const(lhs), rhs);
    }
    public static LT LT(Number lhs, Number rhs){
        return new LT(new Const(lhs), new Const(rhs));
    }
    public static LT LT(String lhs, Number rhs){
        return new LT(new Variable(lhs), new Const(rhs));
    }
    public static LT LT(Number lhs, String rhs){
        return new LT(new Const(lhs), new Variable(rhs));
    }


    public static LTE LTE(Formula lhs, Formula rhs){
        return new LTE(lhs, rhs);
    }
    public static LTE LTE(Formula lhs, String rhs){
        return new LTE(lhs, new Variable(rhs));
    }
    public static LTE LTE(String lhs, Formula rhs){
        return new LTE(new Variable(lhs), rhs);
    }
    public static LTE LTE(String lhs, String rhs){
        return new LTE(new Variable(lhs), new Variable(rhs));
    }
    public static LTE LTE(Formula lhs, Number rhs){
        return new LTE(lhs, new Const(rhs));
    }
    public static LTE LTE(Number lhs, Formula rhs){
        return new LTE(new Const(lhs), rhs);
    }
    public static LTE LTE(Number lhs, Number rhs){
        return new LTE(new Const(lhs), new Const(rhs));
    }
    public static LTE LTE(String lhs, Number rhs){
        return new LTE(new Variable(lhs), new Const(rhs));
    }
    public static LTE LTE(Number lhs, String rhs){
        return new LTE(new Const(lhs), new Variable(rhs));
    }


    public static Not Not(Formula f){
        return new Not(f);
    }
    public static Not Not(String var){
        return new Not(new Variable(var));
    }



}
