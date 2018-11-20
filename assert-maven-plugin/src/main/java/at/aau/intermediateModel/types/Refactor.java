package at.aau.intermediateModel.types;

import intermediateModel.types.rules.exception.TimeTypeRecommendation;

import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Refactor {
    public static String refactorFile(byte[] buffer, List<TimeTypeRecommendation> errors) throws IOException {
        errors.sort(Comparator.comparing(TimeTypeRecommendation::getLine));
        StringBuilder sb = new StringBuilder();
        int lastStartPoint = 0;
        for(int i = 0; i < errors.size(); i++){
            TimeTypeRecommendation e = errors.get(i);
            e.refactor();
            int start = e.getStartProblem();
            int end = e.getEndProblem();
            byte[] pre = Arrays.copyOfRange(buffer, lastStartPoint, start);
            lastStartPoint = end;
            sb.append(new String(pre));
            sb.append(e.getExpr().print());
        }
        byte[] post = Arrays.copyOfRange(buffer, lastStartPoint, buffer.length);
        sb.append(new String(post));
        return sb.toString();
    }
}
