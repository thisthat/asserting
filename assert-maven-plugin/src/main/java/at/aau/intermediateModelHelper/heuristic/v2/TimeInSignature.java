package at.aau.intermediateModelHelper.heuristic.v2;

import intermediateModel.interfaces.IASTRE;
import intermediateModel.interfaces.IASTStm;
import intermediateModel.structure.ASTRE;
import intermediateModel.structure.expression.ASTBinary;
import intermediateModel.structure.expression.ASTMethodCall;
import intermediateModel.visitors.DefualtASTREVisitor;
import intermediateModelHelper.CheckExpression;
import intermediateModelHelper.envirorment.Env;
import intermediateModelHelper.envirorment.temporal.TemporalInfo;
import intermediateModelHelper.envirorment.temporal.structure.TimeMethod;
import intermediateModelHelper.heuristic.definition.SearchTimeConstraint;

import java.util.List;


/**
 *
 * TODO: Define how process user annotations.
 *
 * @author Giovanni Liva (@thisthatDC)
 * @version %I%, %G%
 */
public class TimeInSignature extends SearchTimeConstraint {

	List<TimeMethod>  timeMethods = TemporalInfo.getInstance().getMethodsWithTimeInSignature();

	TimeStatements listTimeStms;
	public TimeInSignature() {
		this.listTimeStms = TimeStatements.getInstance();
	}


	/**
	 * @param stm	Statement to process
	 * @param env	Environment visible to that statement
	 */
	@Override
	public void next(ASTRE stm, Env env) {
		//works only on ASTRE
		IASTRE expr = stm.getExpression();
		if(expr == null){
			return;
		}
		DefualtASTREVisitor v = new DefualtASTREVisitor(){
			@Override
			public void enterASTMethodCall(ASTMethodCall elm) {
				String pointer = elm.getClassPointed();
				String name = elm.getMethodName();
				List<IASTRE> pars = elm.getParameters();
				int size = pars.size();
				if(pointer != null && containTimeOut(pointer, name, size)) {
					expr.setTimeCritical(true);
					print(stm);
				} else {
					boolean flag = false;
					for(IASTRE e : pars){
						if(e instanceof ASTBinary && ((ASTBinary) e).isBool()){
							continue;
						} else if(CheckExpression.checkIt(e, env)){
							flag = true;
						}
					}
					if(flag){
						expr.setTimeCritical(true);
						print(stm);
					}
				}
			}
		};
		v.setExcludeHiddenClass(true);
		expr.visit(v);
	}

	private boolean containTimeOut(String pointer, String name, int nPars){
		for(TimeMethod m : timeMethods){
			if(m.getClassName().equals(pointer) && m.getMethodName().equals(name) && m.getSignature().size() == nPars)
				return true;
		}
		return false;
	}


	private void print(IASTStm stm) {
		stm.setTimeCritical(true);
		listTimeStms.addStatements(stm, TimeElement.Type.Usage);
	}


}
