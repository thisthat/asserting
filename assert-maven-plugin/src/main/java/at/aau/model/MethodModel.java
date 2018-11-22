package at.aau.model;

import at.aau.model.slicing.model.Assert;

public class MethodModel {
    String model;
    Assert _assertion;

    public MethodModel(String model, Assert _assertion) {
        this.model = model;
        this._assertion = _assertion;
    }
}
