package at.aau.intermediateModel.structure;

import at.aau.intermediateModel.interfaces.*;
import at.aau.intermediateModel.structure.expression.ASTVariableDeclaration;
import at.aau.intermediateModel.visitors.DefaultASTVisitor;
import at.aau.intermediateModelHelper.envirorment.Env;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Giovanni Liva (@thisthatDC)
 * @version %I%, %G%
 */

public class ASTMethod extends IASTStm implements IASTMethod, IASTHasStms, IASTVisitor {
    public static String lambda = "λ";
    String name;
	String returnType;
	TimeUnit returnTypeUnit = TimeUnit.UNKNOWN;
	List<ASTVariable> parameters;
	List<String> exceptionsThrowed;
	List<IASTStm> stms = new ArrayList<>();
	boolean isSynchronized = false;
	boolean isAbstract = false;
	boolean isStatic = false;

	List<DeclaredVar> declaredVar = new ArrayList<>();
	List<String> timeVars = new ArrayList<>();
	boolean hasTimeCnst;
	AccessModifier visibility = AccessModifier.PRIVATE;
	private String version;


	public ASTMethod(int start, int end, String name, String returnType, List<ASTVariable> parameters, List<String> exceptionsThrowed, boolean isSynchronized, boolean isAbstract, boolean isStatic) {
		super(start,end);
		this.name = name;
		this.returnType = returnType;
		this.parameters = parameters;
		this.exceptionsThrowed = exceptionsThrowed;
		this.isSynchronized = isSynchronized;
		this.isAbstract = isAbstract;
		this.isStatic = isStatic;
	}

	public boolean isSynchronized() {
		return isSynchronized;
	}

	public void setSynchronized(boolean sync) {
		isSynchronized = sync;
	}

	public boolean isAbstract() {
		return isAbstract;
	}

	public void setAbstract(boolean anAbstract) {
		isAbstract = anAbstract;
	}

	public List<String> getExceptionsThrowed() {
		return exceptionsThrowed;
	}

	public void setExceptionsThrowed(List<String> exceptionsThrowed) {
		this.exceptionsThrowed = exceptionsThrowed;
	}

	public void addThrows(String eType){
		exceptionsThrowed.add(eType);
	}

	public String getName() {
		return name;
	}

	public String getReturnType() {
		return returnType;
	}

	public TimeUnit getReturnTypeUnit() {
		return returnTypeUnit;
	}

	public void setReturnTypeUnit(TimeUnit returnTypeUnit) {
		this.returnTypeUnit = returnTypeUnit;
	}

	@Override
	public AccessModifier getAccessModifier() {
		return visibility;
	}

	public void setAccessModifier(AccessModifier v) {
		this.visibility = v;
	}

	public List<ASTVariable> getParameters() {
		return parameters;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ASTMethod)) return false;
		ASTMethod astMethod = (ASTMethod) o;
		if (getName() != null ? !getName().equals(astMethod.getName()) : astMethod.getName() != null) return false;
		if (getReturnType() != null ? !getReturnType().equals(astMethod.getReturnType()) : astMethod.getReturnType() != null)
			return false;
		if (getParameters() != null ? !getParameters().equals(astMethod.getParameters()) : astMethod.getParameters() != null)
			return false;
		if (getStms() != null ? !getStms().equals(astMethod.getStms()) : astMethod.getStms() != null)
			return false;

		return true;
	}

	public String toString(){
		String out;
		out = "\t" + name + "(";

		for(ASTVariable v: parameters){
			out += v.toString() + ",";
		}
		if(parameters.size() > 0){
			out = out.substring(0,out.length()-1);
		}
		out += ") : " + returnType + " ";
		return out;
	}

	public List<String> getSignature(){
		List<String> out = new ArrayList<>();
		for(ASTVariable p : parameters){
			out.add(p.getType());
		}
		return out;
	}

	public void setStms(List<IASTStm> stms) {
		this.stms = stms;
	}
	public void addStms(IASTStm stm) {
		this.stms.add(stm);
	}
	@Override
	public List<IASTStm> getStms() {
		return stms;
	}


	@Override
	public void visit(ASTVisitor visitor) {
		visitor.enterASTMethod(this);
		for(ASTVariable p : parameters){
			p.visit(visitor);
		}
		for(IASTStm s : stms){
			s.visit(visitor);
		}
		visitor.exitASTMethod(this);
	}

	public boolean equalsBySignature(IASTMethod c) {
		if(c == null) return false;
		if(!c.getName().equals(this.name)) return false;
		if(c.getParameters().size() != this.parameters.size()) return false;
		boolean flag = true;
		List<ASTVariable> pars = c.getParameters();
		for(int i = 0; i < this.parameters.size(); i++){
			if(!pars.get(i).getType().equals(this.parameters.get(i).getType())){
				flag = false;
			}
		}
		return flag;
	}

	@Override
	public boolean isStatic() {
		return this.isStatic;
	}


	public List<DeclaredVar> getDeclaredVar() {
		return declaredVar;
	}

	public void setDeclaredVars(Env e) {
		declaredVar.clear();
		HashMap<String,Integer> ids = new HashMap<>();
		for(IASTStm stm : stms) {
			stm.visit(new DefaultASTVisitor() {
				@Override
				public void enterASTVariableDeclaration(ASTVariableDeclaration elm) {
					String name = elm.getName().getCode();
					int c = 0;
					if (ids.containsKey(name)) {
						c = ids.get(name);
						c++;
					}
					ids.put(name, c);
					String id = name + "_" + c;
					IASTVar v = e.getVar(name);
					DeclaredVar d;
					if (v != null && v.getVarTimeType() != null) {
						d = new DeclaredVar(elm.getType(), name, id, v.getVarTimeType().toString());
					} else {
						d = new DeclaredVar(elm.getType(), name, id);
					}
					declaredVar.add(d);
				}
			});
		}
	}

	@Override
	public void setTimeVars(List<String> vars) {
		this.timeVars = vars;
	}

	@Override
	public void setTimeCnst(boolean f) {
		hasTimeCnst = f;
	}

	@Override
	public boolean hasTimeCnst() {
		return hasTimeCnst;
	}

	@Override
	public void setVersion(String v) {
		this.version = v;
	}

	@Override
	public List<String> getTimeVars() {
		return timeVars;
	}
	
	
}
