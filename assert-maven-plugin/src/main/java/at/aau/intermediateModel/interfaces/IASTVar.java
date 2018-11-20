package at.aau.intermediateModel.interfaces;

import at.aau.intermediateModel.structure.ASTRE;
import at.aau.intermediateModel.types.definition.TimeType;

/**
 * @author Giovanni Liva (@thisthatDC)
 * @version %I%, %G%
 */
public interface IASTVar {
	String getType();
	String getTypeNoArray();
	String getName();
	ASTRE getExpr();
	int getLine();
	boolean isTimeCritical();
	void setTimeCritical(boolean timeCritical);
	boolean equals(Object o);
	String getTypePointed();
	TimeType getVarTimeType();
	void setVarTimeType(TimeType texpr);
}
