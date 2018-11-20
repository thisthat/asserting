package at.aau.intermediateModel.structure.expression;

import intermediateModel.interfaces.ASTREVisitor;
import intermediateModel.interfaces.ASTVisitor;
import intermediateModel.interfaces.IASTRE;
import intermediateModel.interfaces.IASTStm;

/**
 * @author Giovanni Liva (@thisthatDC)
 * @version %I%, %G%
 */
public class ASTIdentifier extends IASTStm implements IASTRE {
	private String value;

	public ASTIdentifier(int start, int end, String value) {
		super(start, end);
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "ASTIdentifier{" +
				"value='" + value + '\'' +
				'}';
	}

	@Override
	public void visit(ASTREVisitor visitor) {
		visitor.enterAll(this);
		visitor.enterASTIdentifier(this);
		visitor.exitASTIdentifier(this);
		visitor.exitAll(this);
	}

	@Override
	public IASTRE negate() {
		return this;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		ASTIdentifier that = (ASTIdentifier) o;

		return value != null ? value.equals(that.value) : that.value == null;
	}

	@Override
	public String print() {
		return value;
	}

	@Override
	public void visit(ASTVisitor visitor) {
		visitor.enterAll(this);
		visitor.enterASTIdentifier(this);
		visitor.exitASTIdentifier(this);
		visitor.exitAll(this);
	}

	@Override
	public int hashCode() {
		return value != null ? value.hashCode() : 0;
	}
}
