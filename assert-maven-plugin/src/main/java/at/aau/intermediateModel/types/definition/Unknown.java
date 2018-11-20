package at.aau.intermediateModel.types.definition;

import com.fasterxml.jackson.annotation.JsonValue;

public class Unknown implements TimeType {
    @JsonValue
    @Override
    public String toString() {
        return "Unknown";
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Unknown;
    }
}
