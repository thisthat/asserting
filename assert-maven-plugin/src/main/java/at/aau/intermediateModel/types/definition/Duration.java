package at.aau.intermediateModel.types.definition;

import com.fasterxml.jackson.annotation.JsonValue;

public class Duration implements TimeType {
    @JsonValue
    String jsonValue = "Duration";

    @Override
    public String toString() {
        return "Duration";
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Duration;
    }
}
