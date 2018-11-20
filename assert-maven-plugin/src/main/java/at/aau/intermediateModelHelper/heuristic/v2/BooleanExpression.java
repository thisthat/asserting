package at.aau.intermediateModelHelper.heuristic.v2;

import at.aau.intermediateModel.interfaces.IASTRE;
import at.aau.intermediateModel.interfaces.IASTStm;
import at.aau.intermediateModel.structure.ASTMethod;
import at.aau.intermediateModel.structure.ASTRE;
import at.aau.intermediateModelHelper.CheckExpression;
import at.aau.intermediateModelHelper.envirorment.Env;


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
