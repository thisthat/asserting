package at.aau.intermediateModelHelper.heuristic.v2;

import intermediateModel.interfaces.IASTRE;
import intermediateModel.interfaces.IASTStm;
import intermediateModel.structure.ASTMethod;
import intermediateModel.structure.ASTRE;
import intermediateModelHelper.CheckExpression;
import intermediateModelHelper.envirorment.Env;
import intermediateModelHelper.heuristic.definition.SearchTimeConstraint;


/**
 * The {@link BooleanExpression} searches for instances of time assignment
 * @author Giovanni Liva (@thisthatDC)
 * @version %I%, %G%
 *
 */
public class BooleanExpression extends SearchTimeConstraint {

	TimeStatements listTimeStms;

	public BooleanExpression() {
		this.listTimeStms = TimeStatements.getInstance();
	}

	@Override
	public void nextMethod(ASTMethod method, Env env) {
		super.nextMethod(method, env);
	}

	@Override
	public void next(ASTRE stm, Env env) {
		//works only on ASTRE
		IASTRE expr = stm.getExpression();
		if(expr == null){
			return;
		}

		if(CheckExpression.checkBooleanTimeComparison(stm.getExpression(), env)){
			mark(stm);
		}
	}

	private void mark(IASTStm stm) {
		listTimeStms.addStatements(stm, TimeElement.Type.Boolean);
	}

}
