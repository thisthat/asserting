package at.aau.intermediateModel.types.definition;

public class Duration implements TimeType {

    @Override
    public String toString() {
        return "Duration";
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Duration;
    }
}
