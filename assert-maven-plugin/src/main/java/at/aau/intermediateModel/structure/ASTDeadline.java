package at.aau.intermediateModel.structure;

import at.aau.intermediateModel.interfaces.ASTVisitor;
import at.aau.intermediateModel.interfaces.IASTHasStms;
import at.aau.intermediateModel.interfaces.IASTStm;
import at.aau.intermediateModel.interfaces.IASTVisitor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Giovanni Liva (@thisthatDC)
 * @version %I%, %G%
 */
public class ASTDeadline extends IASTStm implements IASTHasStms, IASTVisitor {
	List<IASTStm> stms = new ArrayList<IASTStm>();;
	ASTRE expr;

	public ASTDeadline(int start, int end) {
		super(start, end);
	}

	public ASTDeadline(int start, int end, ASTRE expr) {
		super(start, end);
		this.expr = expr;
	}


	public List<IASTStm> getStms() {
		return stms;
	}

	public void setStms(List<IASTStm> stms) {
		this.stms = stms;
	}

	public ASTRE getExpr() {
		return expr;
	}

	public void setExpr(ASTRE expr) {
		this.expr = expr;
	}

	@Override
	public void addStms(IASTStm stm) {
		stms.add(stm);
	}

	@Override
	public String toString() {
		String out = "";
		out += expr.toString();
		out += "\n";
		for(IASTStm e : stms){
			out += e.toString() + "\n";
		}
		out += "";
		return out;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ASTDeadline)) return false;

		ASTDeadline astWhile = (ASTDeadline) o;

		if (getStms() != null ? !getStms().equals(astWhile.getStms()) : astWhile.getStms() != null) return false;
		if (getExpr() != null ? !getExpr().equals(astWhile.getExpr()) : astWhile.getExpr() != null) return false;

		return true;
	}

	@Override
	public void visit(ASTVisitor visitor) {
		visitor.enterASTDeadline(this);
		expr.visit(visitor);
		for(IASTStm s : stms){
			s.visit(visitor);
		}
		visitor.exitASTDeadline(this);
	}
}
