package at.aau.intermediateModelHelper.envirorment.temporalTypes;

import at.aau.intermediateModelHelper.envirorment.temporal.ParseCSV;
import at.aau.intermediateModelHelper.envirorment.temporalTypes.structure.TimeMethod;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by giovanni on 07/03/2017.
 */
public class ParseCSVMethods extends ParseCSV {

    List<TimeMethod> methods = new ArrayList<>();

    public ParseCSVMethods(String path) {
        super(path);
        super.setSplitSignature(true);
        super.start();
    }

    public ParseCSVMethods(File file) {
        super(file);
        super.setSplitSignature(true);
        super.start();
    }

    public ParseCSVMethods(InputStream stream) {
        super(stream);
        super.setSplitSignature(true);
        super.start();
    }

    public List<TimeMethod> getMethods(){
        return methods;
    }

    @Override
    protected void handleHeader(String[] header) {
        //we do not need headers
    }

    @Override
    protected void handleRow(String className, String methodName, String[] signature) {
        methods.add(new TimeMethod(className, methodName, Arrays.asList(signature)));
    }

}
