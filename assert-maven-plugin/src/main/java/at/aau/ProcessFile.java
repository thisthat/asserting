package at.aau;

import at.aau.intermediateModel.structure.ASTClass;
import at.aau.intermediateModel.visitors.creation.JDTVisitor;
import at.aau.model.MethodModel;
import at.aau.model.converter.ClassAnalyzer;
import org.apache.maven.plugin.logging.Log;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ProcessFile {
    List<MethodModel> out = new ArrayList<>();

    public ProcessFile(Path input, String basedir, Log log, String classpath) {
        if(classpath.length() == 0){
            classpath = System.getProperty("java.class.path");
        }
        List<ASTClass> cs = JDTVisitor.parse(input.toAbsolutePath().toString(), basedir, classpath);
        log.debug(input.toAbsolutePath().toString());
        log.debug(basedir);
        for(ASTClass c : cs){
            ClassAnalyzer ca = new ClassAnalyzer(c);
            ca.setGetModel(true);
            try {
                out = ca.getSMT();
            } catch (Exception x) {
                System.out.println("Error In file " + c.getPath());
                System.out.println(x);
            }
        }
    }

    public List<MethodModel> getOutputFiles() {
        return out;
    }
}
