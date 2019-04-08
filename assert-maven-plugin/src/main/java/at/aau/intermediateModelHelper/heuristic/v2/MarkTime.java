package at.aau.intermediateModelHelper.heuristic.v2;

import at.aau.Options;
import at.aau.intermediateModel.structure.ASTRE;
import at.aau.intermediateModel.structure.expression.ASTMethodCall;
import at.aau.intermediateModel.visitors.DefaultASTVisitor;
import at.aau.intermediateModelHelper.CheckExpression;
import at.aau.intermediateModelHelper.envirorment.Env;
import at.aau.recovery.CorrectionPoint;

/**
 * The {@link MarkTime} searches for instances of time assignment
 * @author Giovanni Liva (@thisthatDC)
 * @version %I%, %G%
 *
 */
public class MarkTime extends SearchTimeConstraint {

	@Override
	public void next(ASTRE stm, Env env) {
		if(Options.isTimeEnabled())
			CheckExpression.checkRE(stm,env);

		stm.visit(new DefaultASTVisitor(){
			@Override
			public void enterASTMethodCall(ASTMethodCall elm) {
				if(elm.getClassPointed() != null && elm.getClassPointed().equals("at.aau.asserting.AssertLibrary")){
					elm.addVar(env);
				}
			}
		});
	}


}
