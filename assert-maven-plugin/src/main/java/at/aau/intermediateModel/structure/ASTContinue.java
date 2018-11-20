package at.aau.intermediateModel.structure;

import at.aau.intermediateModel.interfaces.ASTVisitor;
import at.aau.intermediateModel.interfaces.IASTStm;
import at.aau.intermediateModel.interfaces.IASTVisitor;

/**
 * @author Giovanni Liva (@thisthatDC)
 * @version %I%, %G%
 */
public class ASTContinue extends IASTStm implements IASTVisitor {

	private String target = "";

	public ASTContinue(int start, int end) {
		super(start, end);
	}
	public ASTContinue(int start, int end, String target) {
		super(start, end);
		this.target = target;
	}

	@Override
	public String toString() {
		return "";
	}


	public String getTarget() {
		return target;
	}

	@Override
	public boolean equals(Object obj) {
		return true;
	}

	@Override
	public void visit(ASTVisitor visitor) {
		visitor.enterASTContinue(this);
		visitor.enterSTM(this);
		visitor.exitSTM(this);
		visitor.exitASTContinue(this);
	}
}
