package at.aau.intermediateModelHelper.envirorment.temporalTypes;

import intermediateModel.types.definition.Duration;
import intermediateModel.types.definition.TimeType;
import intermediateModel.types.definition.Timestamp;
import intermediateModelHelper.envirorment.temporal.ParseCSV;
import intermediateModelHelper.envirorment.temporalTypes.structure.TimeParameterMethod;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by giovanni on 07/03/2017.
 */
public class ParseCSVTimeParameterMethods extends ParseCSV {

    List<TimeParameterMethod> methods = new ArrayList<>();

    public ParseCSVTimeParameterMethods(String path) {
        super(path);
        super.setSplitSignature(true);
        super.start();
    }

    public ParseCSVTimeParameterMethods(File file) {
        super(file);
        super.setSplitSignature(true);
        super.start();
    }

    public ParseCSVTimeParameterMethods(InputStream stream) {
        super(stream);
        super.setSplitSignature(true);
        super.start();
    }

    public List<TimeParameterMethod> getMethods(){
        return methods;
    }

    @Override
    protected void start(InputStreamReader file){
        boolean notHeader = false;
        try (BufferedReader br = new BufferedReader(file)) {
            String line;
            while ((line = br.readLine()) != null) {
                // use comma as separator
                String[] row = line.split(super.getSeparator());
                if(notHeader) {
                    String[] index_type = row[3].split(",");
                    List<Integer> indexs = new ArrayList<>();
                    List<TimeType> types = new ArrayList<>();
                    for(int i = 0; i < index_type.length; i++){
                        if(i%2 == 0){
                            indexs.add(Integer.parseInt(index_type[i]));
                        } else {
                            String t = index_type[i];
                            if(t.equals("Duration"))
                                types.add(new Duration());
                            else
                                types.add(new Timestamp());
                        }
                    }
                    handleRow(row[0],row[1], row[2].split(","),
                            indexs.stream().mapToInt(Integer::intValue).toArray(), types.toArray(new TimeType[0]));
                }
                else {
                    notHeader = true;
                    handleHeader(row);
                }
            }
        } catch (IOException e) {
            //e.printStackTrace();
        }
    }

    @Override
    protected void handleHeader(String[] header) {
        //we do not need headers
    }

    @Override
    protected void handleRow(String className, String methodName, String[] signature) {
        System.err.println("We will never reach this. If you read this.... the programmer should be fired!");
    }

    protected void handleRow(String className, String methodName, String[] signature, int[] index, TimeType[] types) {
        methods.add(new TimeParameterMethod(className, methodName, Arrays.asList(signature), index, types));
    }

}
