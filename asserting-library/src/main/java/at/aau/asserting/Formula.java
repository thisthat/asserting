package at.aau.asserting;

import java.util.List;

public abstract class Formula {

    private String preamble = "";


    protected void addPreamble(String s){
        preamble += s;
    }

    public abstract String print();
    public abstract Formula negate();


    public String PostModel(){
        return "(maximize time)\n(check-sat)\n(get-model)";
    }

    public String pretty() {
        return this.toString().replace(this.PostModel(), "");
    }

    public String prepare() {
        return prepare(true);
    }

    public String prepare(boolean negate) {
        Formula f = negate ? this.negate() : this;
        return String.format("(assert %s)\r\n%s", f.print(), this.PostModel());
    }

    @Override
    public String toString() {
        return this.print();
    }

    public abstract Formula noQuantifier();

    public abstract List<String> getVar();

    public boolean validVar(String v){
        return !Character.isDigit(v.charAt(0)) && v.charAt(0) != '-';
    }
}
