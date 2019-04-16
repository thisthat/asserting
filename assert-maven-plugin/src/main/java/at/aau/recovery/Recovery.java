package at.aau.recovery;

import at.aau.intermediateModel.structure.ASTClass;
import at.aau.intermediateModel.structure.expression.ASTMethodCall;
import at.aau.intermediateModel.visitors.DefaultASTVisitor;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Recovery {

    public static List<CorrectionPoint> findPoint(ASTClass c) {
        List<CorrectionPoint> out = new ArrayList<>();
        c.visit(new DefaultASTVisitor(){
            @Override
            public void enterASTMethodCall(ASTMethodCall elm) {
                if(elm.getClassPointed() != null && elm.getClassPointed().equals("at.aau.asserting.AssertLibrary")){
                    out.add(new CorrectionPoint(elm));
                }
            }
        });
        return out;
    }

    public static void recoverFile(String file, List<CorrectionPoint> correctionPoint) {
        //sort the points from the latest to the earliest
        Collections.sort(correctionPoint, (c1, c2) -> (int) (c2.getStart() - c1.getStart()));
        try {
            for(CorrectionPoint c : correctionPoint){
                String fileString = new String(Files.readAllBytes(Paths.get(file)), StandardCharsets.UTF_8);
                int s = c.getStart();
                int e = c.getEnd();
                String init = fileString.substring(0, s);
                String edit = fileString.substring(s, e+1);
                String end  = fileString.substring(e+1);

                //edit
                StringBuilder sb = new StringBuilder();
                for(String v : c.getVars()){
                    if(v.equals("time")) continue;
                    sb.append(v).append(" = assertingException.aCorrectValueFor(\"").append(sanitize(v)).append("\",")
                        .append(v).append(");\n");
                }

                //writing
                Path path = Paths.get(file);
                try (BufferedWriter writer = Files.newBufferedWriter(path))
                {
                    writer.write(init);
                    writer.write("try {  ");
                    writer.write(edit);
                    writer.write("\n} catch (at.aau.asserting.AssertingException assertingException) {\n");
                    writer.write(sb.toString());
                    writer.write("}\n");
                    writer.write(end);
                    writer.flush();
                }
            }
        } catch (IOException e) {
            System.err.println("Cannot open file: " + file);
        }

    }

    private static String sanitize(String v) {
        if(v.contains(".")){
            return v.substring(v.lastIndexOf(".")).replace(".", "");
        }
        return v;
    }
}
