package at.aau.intermediateModel.types.definition;

public class Unknown implements TimeType {

    @Override
    public String toString() {
        return "Unknown";
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Unknown;
    }
}
