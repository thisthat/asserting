package at.aau;

import at.aau.intermediateModel.interfaces.IASTMethod;
import at.aau.intermediateModel.structure.ASTClass;
import at.aau.intermediateModel.visitors.creation.JDTVisitor;
import at.aau.model.converter.ClassAnalyzer;
import at.aau.model.smt.exception.VariableNotCorrect;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;

public class ProcessFile {
    public ProcessFile(Path input, String basedir) throws Exception {
        List<ASTClass> cs = JDTVisitor.parse(input.toString(), basedir);
        for(ASTClass c : cs){
            ClassAnalyzer ca = new ClassAnalyzer(c);
            ca.setGetModel(true);
            try {
                HashMap<IASTMethod, List<VariableNotCorrect>> err = ca.getErrors();
                for(IASTMethod m : err.keySet()){
                    for (VariableNotCorrect v : err.get(m)) {
                        System.out.println(m.getName());
                        System.out.println(v.getVarName() + "@" + v.getWhere().getLine());
                        System.out.println("Min");
                        System.out.println(v.getMinModel());
                    }
                }
            } catch (Exception x) {
                System.out.println("In file " + c.getPath());
                throw x;
            }
        }
    }

    public String getOutputFiles() {
        return "";
    }
}
