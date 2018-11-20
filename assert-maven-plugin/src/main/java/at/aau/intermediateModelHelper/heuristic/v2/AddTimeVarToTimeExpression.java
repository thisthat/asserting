package at.aau.intermediateModelHelper.heuristic.v2;

import at.aau.intermediateModel.interfaces.IASTRE;
import at.aau.intermediateModel.interfaces.IASTVar;
import at.aau.intermediateModel.structure.ASTRE;
import at.aau.intermediateModel.structure.ASTWhile;
import at.aau.intermediateModel.structure.expression.ASTIdentifier;
import at.aau.intermediateModel.structure.expression.ASTMethodCall;
import at.aau.intermediateModel.visitors.DefaultASTVisitor;
import at.aau.intermediateModel.visitors.DefualtASTREVisitor;
import at.aau.intermediateModelHelper.envirorment.Env;


/**
 * The {@link AddTimeVarToTimeExpression} searches for instances of time assignment
 * @author Giovanni Liva (@thisthatDC)
 * @version %I%, %G%
 *
 */

public class AddTimeVarToTimeExpression extends SearchTimeConstraint {

	TimeStatements listTimeStms;

	public AddTimeVarToTimeExpression() {
		this.listTimeStms = TimeStatements.getInstance();
	}

	@Override
	public void next(ASTRE stm, Env env) {
		//works only on ASTRE
		IASTRE expr = stm.getExpression();
		if(expr == null){
			return;
		}

		//record time vars avoiding going into hidden classes
		//hidden classes are handled in ApplyHeuristics
		DefaultASTVisitor v = new DefaultASTVisitor(){
			@Override
			public void enterASTMethodCall(ASTMethodCall call) {
				call.visit(new DefualtASTREVisitor(){
					@Override
					public void enterASTIdentifier(ASTIdentifier elm) {
						IASTVar v = env.getVar(elm.getValue());
						if(v != null && v.isTimeCritical()){
							mark(v, call);
						}
					}
				}.setExcludeHiddenClassContinuos(true));//.setExcludeParsContinuos(true));
			}
		};
		v.setExcludeHiddenClass(true);
		stm.visit(v);
	}

	@Override
	public void nextWhileExpr(ASTRE stm, Env env, ASTWhile w) {
		stm.getExpression().visit(new DefualtASTREVisitor(){
			@Override
			public void enterASTIdentifier(ASTIdentifier elm) {
				IASTVar v = env.getVar(elm.getValue());
				if(v != null && v.isTimeCritical()){
					w.addTimeVar(v.getName());
				}
			}
		});
	}

	private void mark(IASTVar var, ASTMethodCall call) {

		if(call.getExprCallee() != null && var.getName().equals(call.getExprCallee().print()))
			return;
		call.addTimeVar(var.getName());
	}

}
