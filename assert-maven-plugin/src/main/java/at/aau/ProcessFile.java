package at.aau;

import at.aau.intermediateModel.structure.ASTClass;
import at.aau.intermediateModel.visitors.creation.JDTVisitor;
import at.aau.model.MethodModel;
import at.aau.model.converter.ClassAnalyzer;
import org.apache.maven.plugin.logging.Log;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ProcessFile {
    List<MethodModel> out = new ArrayList<>();

    String classpath;
    Path input;
    String basedir;
    Log log;


    public ProcessFile(Path input, String basedir, Log log, String classpath, long timeout) {
        this.input = input;
        this.basedir = basedir;
        this.classpath = classpath;
        this.log = log;
        //doWork();
        ExecutorService executor = Executors.newFixedThreadPool(1);
        Future<Void> f = (Future<Void>) executor.submit(() -> {
            doWork();
        });
        try {
            f.get(timeout, TimeUnit.SECONDS);
        } catch (InterruptedException | TimeoutException | ExecutionException e) {
            e.printStackTrace();
        } finally {
          executor.shutdownNow();
        }
    }

    public List<MethodModel> getOutputFiles() {
        return out;
    }

    private void doWork(){
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
}
