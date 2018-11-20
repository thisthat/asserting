package at.aau.intermediateModelHelper.heuristic.v2;

import intermediateModel.interfaces.IASTRE;
import intermediateModel.interfaces.IASTStm;
import intermediateModel.structure.ASTRE;
import intermediateModelHelper.CheckExpression;
import intermediateModelHelper.envirorment.Env;
import intermediateModelHelper.heuristic.definition.SearchTimeConstraint;


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
