package at.aau.asserting;

public abstract class Formula {

    private String preamble = "";


    protected void addPreamble(String s){
        preamble += s;
    }

    public abstract String print();
    public abstract Formula negate();


    public String PostModel(){
        return "(check-sat)\n(get-model)";
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
}
