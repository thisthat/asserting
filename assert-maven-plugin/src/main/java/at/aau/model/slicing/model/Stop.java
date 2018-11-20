package at.aau.model.slicing.model;

import at.aau.intermediateModel.structure.ASTIf;
import at.aau.model.slicing.model.interfaces.Stm;
import at.aau.model.slicing.model.visitor.ReducedVisitor;

/**
 * Created by giovanni on 11/07/2017.
 */
public class Stop extends Stm {



    public Stop(int start, int end, int line, int lineEnd, String code) {
        super(start, end, line, lineEnd, code);
    }

    public Stop(ASTIf stm) {
        super(  stm.getStart(),
                stm.getEnd(),
                stm.getLine(),
                stm.getLineEnd(),
                stm.getCode()
        );
    }

    @Override
    public void visit(ReducedVisitor visitor) {

    }
}
