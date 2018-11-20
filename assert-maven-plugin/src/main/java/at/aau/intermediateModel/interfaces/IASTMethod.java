package at.aau.intermediateModel.interfaces;


import intermediateModel.structure.ASTVariable;
import intermediateModelHelper.envirorment.Env;
import org.javatuples.Pair;

import java.util.List;

/**
 * @author Giovanni Liva (@thisthatDC)
 * @version %I%, %G%
 */
public interface IASTMethod extends IASTHasStms, IASTVisitor {

	enum AccessModifier {
		PUBLIC,
		PRIVATE,
		PROTECTED,
	}

	AccessModifier getAccessModifier();
	void setAccessModifier(AccessModifier v);
	List<ASTVariable> getParameters();
	String getName();
	List<String> getExceptionsThrowed();
	String getReturnType();
	List<String> getSignature();
	int getLine();
	int getLineEnd();
	int getStart();
	int getEnd();
	String getCode();
	boolean equalsBySignature(IASTMethod c);
	boolean equalsBySignature(String pkg, String name, List<Pair<String, String>> signature);
	boolean isStatic();
	List<DeclaredVar> getDeclaredVar();
	void setDeclaredVars(Env e);
	List<String> getTimeVars();
	void setTimeVars(List<String> vars);
	void setTimeCnst(boolean f);
	boolean hasTimeCnst();
	void setSouceCode(String s);
	void setVersion(String v);

}
