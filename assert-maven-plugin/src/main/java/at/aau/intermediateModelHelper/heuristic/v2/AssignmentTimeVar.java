package at.aau.intermediateModelHelper.heuristic.v2;

import intermediateModel.interfaces.IASTRE;
import intermediateModel.interfaces.IASTStm;
import intermediateModel.interfaces.IASTVar;
import intermediateModel.structure.ASTRE;
import intermediateModel.structure.expression.*;
import intermediateModel.visitors.DefaultASTVisitor;
import intermediateModel.visitors.DefualtASTREVisitor;
import intermediateModelHelper.envirorment.Env;
import intermediateModelHelper.heuristic.definition.SearchTimeConstraint;


/**
 * The {@link AssignmentTimeVar} searches for instances of time assignment
 * @author Giovanni Liva (@thisthatDC)
 * @version %I%, %G%
 *
 */
public class AssignmentTimeVar extends SearchTimeConstraint {

	TimeStatements listTimeStms;

	public AssignmentTimeVar() {
		this.listTimeStms = TimeStatements.getInstance();
	}

	@Override
	public void next(ASTRE stm, Env env) {
		//works only on ASTRE
		IASTRE expr = stm.getExpression();
		if(expr == null){
			return;
		}

		//we assume that the variable assigned is already in the environment
		//this is assured by the class that trigger this method
		//we only cover the case of Math.max/min because all the others are already covered
		DefaultASTVisitor v = new DefaultASTVisitor(){
			@Override
			public void enterASTVariableDeclaration(ASTVariableDeclaration elm) {
				IASTVar var = env.getVar(elm.getNameString());
				if(var != null && elm.getExpr() != null && elm.getExpr().isTimeCritical()){
					var.setTimeCritical(true);
					mark(stm);
					elm.setTimeCritical(true);
				}
			}

			@Override
			public void enterASTAssignment(ASTAssignment elm) {
				if(elm.getRight() != null && elm.getRight().isTimeCritical()){
					IASTRE lexpr = elm.getLeft();
					DefualtASTREVisitor v = new DefualtASTREVisitor(){
						@Override
						public void enterASTLiteral(ASTLiteral literal) {
							IASTVar var = env.getVar(literal.getValue());
							if(var != null){
								var.setTimeCritical(true);
								mark(stm);
								elm.setTimeCritical(true);
							}
						}
						@Override
						public void enterASTIdentifier(ASTIdentifier identifier) {
							IASTVar var = env.getVar(identifier.getValue());
							if(var != null){
								var.setTimeCritical(true);
								mark(stm);
								elm.setTimeCritical(true);
							}
						}

						@Override
						public void enterASTAttributeAccess(ASTAttributeAccess attAccess) {
							IASTVar var = env.getVar(attAccess.getAttributeName());
							if(var != null){
								var.setTimeCritical(true);
								mark(stm);
								elm.setTimeCritical(true);
							}
						}
					};
					v.setExcludeHiddenClass(true);
					lexpr.visit(v);
				}
			}
		};
		v.setExcludeHiddenClass(true);
		expr.visit(v);

	}

	private void mark(IASTStm stm) {
		listTimeStms.addStatements(stm, TimeElement.Type.Assignment);
		stm.setTimeCritical(true);
	}

}
