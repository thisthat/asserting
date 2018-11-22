package at.aau.intermediateModel.structure;

import at.aau.intermediateModel.interfaces.ASTVisitor;
import at.aau.intermediateModel.interfaces.IASTStm;
import at.aau.intermediateModel.interfaces.IASTVisitor;

/**
 * @author Giovanni Liva (@thisthatDC)
 * @version %I%, %G%
 */
public class ASTAssert extends IASTStm implements IASTVisitor {

	private ASTRE expr;

	public ASTAssert(int start, int end, ASTRE expr) {
		super(start, end);
		this.expr = expr;
	}

	public ASTRE getExpr() {
		return expr;
	}

	@Override
	public String toString() {
		return expr.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ASTAssert)) return false;

		ASTAssert astReturn = (ASTAssert) o;

		if (expr != null ? !expr.equals(astReturn.expr) : astReturn.expr != null) return false;

		return true;
	}

	@Override
	public void visit(ASTVisitor visitor) {
		visitor.enterASTAssert(this);
		visitor.enterSTM(this);
		visitor.exitSTM(this);
		expr.visit(visitor);
		visitor.exitASTAssert(this);
	}
}
