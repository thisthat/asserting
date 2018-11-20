package at.aau.intermediateModelHelper.heuristic.v2;

import intermediateModel.interfaces.IASTRE;
import intermediateModel.structure.ASTRE;
import intermediateModel.structure.expression.ASTMethodCall;
import intermediateModel.visitors.DefualtASTREVisitor;
import intermediateModelHelper.CheckExpression;
import intermediateModelHelper.envirorment.Env;
import intermediateModelHelper.heuristic.definition.SearchTimeConstraint;


/**
 * The {@link MinMaxSearch} searches for instances of time assignment
 * @author Giovanni Liva (@thisthatDC)
 * @version %I%, %G%
 *
 */
public class MinMaxSearch extends SearchTimeConstraint {

	TimeStatements listTimeStms;

	public MinMaxSearch() {
		this.listTimeStms = TimeStatements.getInstance();
	}

	@Override
	public void next(ASTRE stm, Env env) {

		if(stm.getExpression() == null){
			return;
		}
		stm.getExpression().visit(new DefualtASTREVisitor() {
			@Override
			public void enterASTMethodCall(ASTMethodCall elm) {
				if(elm.getClassPointed() != null && elm.getClassPointed().equals("java.lang.Math")){
					String name = elm.getMethodName();
					if(name.equals("min") || name.equals("max") || name.equals("abs")){
						checkForTime(elm, env, stm);
					}
				}
			}
		}.setExcludeHiddenClassContinuos(true));
	}

	private void checkForTime(ASTMethodCall elm, Env env, ASTRE stm) {
		//if(1==1) return;
		for(IASTRE exp : elm.getParameters()){
			if(CheckExpression.checkRightHandAssignment(stm, exp, env)){
				elm.setMaxMin(true);
				listTimeStms.addStatements(stm, TimeElement.Type.MinMax);
			}
		}
	}


}
