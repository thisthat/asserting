package at.aau.model.slicing.heuristics;

import at.aau.intermediateModel.structure.ASTRE;
import at.aau.intermediateModelHelper.CheckExpression;
import at.aau.intermediateModelHelper.envirorment.Env;
import at.aau.intermediateModelHelper.heuristic.v2.SearchTimeConstraint;

/**
 * The {@link MarkTime} searches for instances of time assignment
 * @author Giovanni Liva (@thisthatDC)
 * @version %I%, %G%
 *
 */
public class MarkTime extends SearchTimeConstraint {

	@Override
	public void next(ASTRE stm, Env env) {
		CheckExpression.checkRE(stm,env);
	}


}
