package at.aau.intermediateModel.structure.expression;

import at.aau.intermediateModel.interfaces.ASTREVisitor;
import at.aau.intermediateModel.interfaces.ASTVisitor;
import at.aau.intermediateModel.interfaces.IASTRE;
import at.aau.intermediateModel.interfaces.IASTStm;

/**
 * @author Giovanni Liva (@thisthatDC)
 * @version %I%, %G%
 */
public class ASTBinary extends IASTStm implements IASTRE {

	protected IASTRE left;
	protected IASTRE right;
	protected OPERATOR op;
	String type;

	public ASTBinary(int start, int end, IASTRE left, IASTRE right, OPERATOR op) {
		super(start, end);
		this.left = left;
		this.right = right;
		this.op = op;
	}

	public void refactor(){
		int start = right.getStart();
		int start_left = left.getStart();
		int end_left = left.getEnd() + right.getLength() + 3;
		ASTLiteral lit = new ASTLiteral( start, start + 1, "0");
		ASTBinary newLeft = new ASTBinary(start_left, end_left, this.left, this.right, OPERATOR.minus);
		this.left = newLeft;
		this.right = lit;
	}



	public IASTRE getLeft() {
		return left;
	}

	public void setLeft(IASTRE left) {
		this.left = left;
	}

	public IASTRE getRight() {
		return right;
	}

	public void setRight(IASTRE right) {
		this.right = right;
	}

	public OPERATOR getOp() {
		return op;
	}

	public void setOp(OPERATOR op) {
		this.op = op;
	}

	@Override
	public String toString() {
		return "ASTBinary{" +
				"left=" + left +
				", right=" + right +
				", op=" + op +
				'}';
	}

	@Override
	public void visit(ASTREVisitor visitor) {
		visitor.enterASTbinary(this);
		visitor.enterAll(this);
		if(left != null)
			left.visit(visitor);
		if(right!= null)
			right.visit(visitor);
		visitor.exitASTbinary(this);
		visitor.exitAll(this);
	}

	@Override
	public IASTRE negate() {
		OPERATOR _op = this.op;
		switch (this.getOp()){
			case not: 	_op = OPERATOR.not; 	break;
			case or: 	_op = OPERATOR.and; 	break;
			case and: 	_op = OPERATOR.or; 		break;
			case div: 	_op = OPERATOR.mul;		break;
			case mul: 	_op = OPERATOR.div;		break;
			case plus: 	_op = OPERATOR.minus;	break;
			case minus: _op = OPERATOR.plus;	break;

			case equality: 		_op = OPERATOR.notEqual;		break;
			case notEqual: 		_op = OPERATOR.equality;		break;
			case less: 			_op = OPERATOR.greaterEqual;	break;
			case lessEqual: 	_op = OPERATOR.greater;			break;
			case greater: 		_op = OPERATOR.lessEqual;		break;
			case greaterEqual: 	_op = OPERATOR.less;			break;

		}
		ASTBinary out = new ASTBinary(start,end, left.negate(), right.negate(), _op);
		out.setTimeCriticalNoChild(this.isTimeCritical);
		if(left.isTimeCritical() || right.isTimeCritical()) {
			out.setTimeCriticalNoChild(true);
		}
		return out;
	}

	@Override
	public String print() {
		return (left != null ? left.print() + " " : "")
				+ op.print() + " "
				+ (right != null ? right.print() : "");
	}

	@Override
	public void visit(ASTVisitor visitor) {
		visitor.enterAll(this);
		visitor.enterASTbinary(this);
		if(left != null)
			left.visit(visitor);
		if(right!= null)
			right.visit(visitor);
		visitor.exitASTbinary(this);
		visitor.exitAll(this);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		ASTBinary astBinary = (ASTBinary) o;

		if (left != null ? !left.equals(astBinary.left) : astBinary.left != null) return false;
		if (right != null ? !right.equals(astBinary.right) : astBinary.right != null) return false;
		return op == astBinary.op;
	}

	@Override
	public int hashCode() {
		int result = left != null ? left.hashCode() : 0;
		result = 31 * result + (right != null ? right.hashCode() : 0);
		result = 31 * result + (op != null ? op.hashCode() : 0);
		return result;
	}

	@Override
	public void setTimeCritical(boolean timeCritical) {
		super.setTimeCritical(timeCritical);
		left.setTimeCritical(timeCritical);
		right.setTimeCritical(timeCritical);
	}

	public void setTimeCriticalNoChild(boolean timeCritical) {
		super.setTimeCritical(timeCritical);
	}

    public boolean isBool() {
		switch (op) {
			case less:
			case lessEqual:
			case greater:
			case greaterEqual:
			case equality:
			case notEqual:
				return true;
		}
		return false;
    }

	public void setType(String qualifiedName) {
		this.type = qualifiedName;
	}

	public String getType() {
		return type;
	}

	public boolean isString() {
		return type != null ? type.equals("java.lang.String") : false;
	}
}
