package at.aau.model.slicing.heuristics;

import at.aau.intermediateModel.interfaces.IASTRE;
import at.aau.intermediateModel.interfaces.IASTStm;
import at.aau.intermediateModel.structure.ASTRE;
import at.aau.intermediateModel.structure.expression.ASTBinary;
import at.aau.intermediateModel.structure.expression.ASTMethodCall;
import at.aau.intermediateModel.visitors.DefualtASTREVisitor;
import at.aau.intermediateModelHelper.CheckExpression;
import at.aau.intermediateModelHelper.envirorment.Env;
import at.aau.intermediateModelHelper.envirorment.temporal.TemporalInfo;
import at.aau.intermediateModelHelper.envirorment.temporal.structure.TimeMethod;
import at.aau.intermediateModelHelper.heuristic.v2.SearchTimeConstraint;
import at.aau.model.slicing.TimeElement;
import at.aau.model.slicing.TimeStatements;

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
