package at.aau.model;

import at.aau.model.slicing.model.Assert;
import at.aau.model.slicing.model.Method;

import java.util.Objects;

public class MethodModel {
    String model;
    Assert _assertion;
    Method method;

    public MethodModel(String model, Assert _assertion, Method method) {
        this.model = model.replace("(check-sat)", "");
        this._assertion = _assertion;
        this.method = method;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MethodModel)) return false;
        MethodModel that = (MethodModel) o;
        return Objects.equals(model, that.model);
        //&& Objects.equals(_assertion, that._assertion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(model, _assertion);
    }

    @Override
    public String toString() {
        return "ModelName: " + this.getModelName();
    }

    public String getModelName() {
        return this.method.getFullName() + "_" + _assertion.getLine();
    }

    public String getModel(){
        return this.model;
    }

    public boolean emptyModel() {
        return this.model.trim().length() <= 0;
    }
}
