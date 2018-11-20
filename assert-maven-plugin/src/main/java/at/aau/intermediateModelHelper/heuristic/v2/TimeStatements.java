package at.aau.intermediateModelHelper.heuristic.v2;

import at.aau.intermediateModel.interfaces.IASTStm;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by giovanni on 10/07/2017.
 */
public class TimeStatements {
    private static TimeStatements instance = null;

    private List<TimeElement> stms = new ArrayList<>();

    public static synchronized TimeStatements getInstance(){
        if(instance == null){
            instance = new TimeStatements();
        }
        return instance;
    }

    public synchronized void addStatements(IASTStm stm, TimeElement.Type type){
        for(TimeElement t : stms){
            if(t.getStm().equals(stm))
                return;
        }
        TimeElement te = new TimeElement(stm, type);
        stms.add(te);
    }

    public synchronized List<TimeElement> getStms(){
        return stms;
    }

    public synchronized void clear(){
        stms.clear();
    }
}
