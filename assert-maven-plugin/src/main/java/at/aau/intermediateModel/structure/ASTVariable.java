package at.aau.intermediateModel.structure;

import at.aau.intermediateModel.interfaces.*;
import at.aau.intermediateModel.types.definition.TimeType;

/**
 * @author Giovanni Liva (@thisthatDC)
 * @version %I%, %G%
 */
public class  ASTVariable extends IASTStm implements IASTVar, IASTVisitor {
	String name;
	String type;
	TimeUnit unit = TimeUnit.UNKNOWN;
	private String typePointed;
	private TimeType timeType;


	public ASTVariable(int start, int end, String name, String type) {
		super(start, end);
		this.name = name;
		this.type = type;
	}

	public String getName() {
		return name;
	}

	@Override
	public ASTRE getExpr() {
		return null;
	}

	public String getType() {
		if(type.contains("<")){
			return type.substring(0, type.indexOf("<"));
		} else
			return type;
	}

	@Override
	public String getTypeNoArray() {
		if(type.endsWith("]")) return type.substring(0, type.indexOf("["));
		if(type.contains(".")) return type.substring(type.indexOf(".") +1);
		return type;
	}

	@Override
	public String toString() {
		return type + " " + name;
	}

	@Override
	public boolean equals(Object o) {
		if(o instanceof ASTAttribute) return equals((ASTAttribute) o);
		if (this == o) return true;
		if (!(o instanceof ASTVariable)) return false;

		ASTVariable that = (ASTVariable) o;

		if (getName() != null ? !getName().equals(that.getName()) : that.getName() != null) return false;
		if (getType() != null ? !getType().equals(that.getType()) : that.getType() != null) return false;

		return true;
	}

	public void setTypePointed(String typePointed) {
		this.typePointed = typePointed;
	}

	public String getTypePointed() {
		return typePointed;
	}

	public TimeUnit getUnit() {
		return unit;
	}

	public void setUnit(TimeUnit unit) {
		this.unit = unit;
	}

	public boolean equals(ASTAttribute o){
		return this.getName() == o.getName() && this.getType() == o.getType();
	}

	@Override
	public void visit(ASTVisitor visitor) {
		visitor.enterASTVariable(this);
		visitor.enterSTM(this);
		visitor.exitSTM(this);
		visitor.exitASTVariable(this);
	}

	@Override
	public TimeType getVarTimeType() {
		return timeType;
	}

	@Override
	public void setVarTimeType(TimeType texpr) {
		this.timeType = texpr;
	}
}
