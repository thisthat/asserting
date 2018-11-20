package at.aau.model.slicing.heuristics;

import at.aau.intermediateModel.interfaces.IASTRE;
import at.aau.intermediateModel.interfaces.IASTStm;
import at.aau.intermediateModel.structure.ASTRE;
import at.aau.intermediateModelHelper.CheckExpression;
import at.aau.intermediateModelHelper.envirorment.Env;
import at.aau.intermediateModelHelper.heuristic.v2.SearchTimeConstraint;
import at.aau.model.slicing.TimeElement;
import at.aau.model.slicing.TimeStatements;

/**
 * The {@link ReturnExpression} searches for instances of time assignment
 * @author Giovanni Liva (@thisthatDC)
 * @version %I%, %G%
 *
 */
public class ReturnExpression extends SearchTimeConstraint {

	TimeStatements listTimeStms;

	public ReturnExpression() {
		this.listTimeStms = TimeStatements.getInstance();
	}

	@Override
	public void next(ASTRE stm, Env env) {
		//works only on ASTRE Return
		IASTRE expr = stm.getExpression();
		if(expr == null){
			return;
		}

		if(CheckExpression.checkRightHandAssignment(stm, expr, env)){
			mark(stm);
		}
	}

	private void mark(IASTStm stm) {
		listTimeStms.addStatements(stm, TimeElement.Type.Return);
	}

}
