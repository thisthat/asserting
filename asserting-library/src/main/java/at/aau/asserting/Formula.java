package at.aau.asserting;

public abstract class Formula {

    @Override
    public String toString() {
        return "(check-sat)\n(get-model)";
    }

    public String pretty() {
        return this.toString().replace("(check-sat)\n(get-model)", "");
    }
}
