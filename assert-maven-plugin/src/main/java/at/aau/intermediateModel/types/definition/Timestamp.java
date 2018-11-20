package at.aau.intermediateModel.types.definition;

import com.fasterxml.jackson.annotation.JsonValue;

public class Timestamp implements TimeType {
    @JsonValue
    @Override
    public String toString() {
        return "Timestamp";
    }
    @Override
    public boolean equals(Object obj) {
        return obj instanceof Timestamp;
    }
}
