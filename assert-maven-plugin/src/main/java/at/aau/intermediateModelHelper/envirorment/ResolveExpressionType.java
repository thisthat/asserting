package at.aau.intermediateModelHelper.envirorment;

import at.aau.intermediateModel.interfaces.IASTRE;
import at.aau.intermediateModel.interfaces.IASTVar;
import at.aau.intermediateModel.structure.ASTClass;
import at.aau.intermediateModel.structure.expression.*;
import at.aau.intermediateModel.visitors.DefualtASTREVisitor;
import at.aau.intermediateModel.visitors.interfaces.ParseRE;

import java.util.regex.Pattern;

/**
 * @author Giovanni Liva (@thisthatDC)
 * @version %I%, %G%
 */
class ResolveExpressionType extends ParseRE {

	final String _boolean = "boolean";
	final String _int = "int";
	final String _string = "String";
	private ASTClass _c;

	final static Pattern p = Pattern.compile("[0-9]+(L?)");

	protected ResolveExpressionType(Env e) {
		super(e);
	}

	public String getType(IASTRE r){
		return (String) analyze(r);
	}

	public String getType(IASTRE r, ASTClass c){
		this._c	 = c;
		return (String) analyze(r);
	}

	@Override
	protected Object analyze(ASTArrayInitializer r) {
		if(r.getExprs().size() > 0){
			return analyze(r.getExprs().get(0));
		}
		return null;
	}

	@Override
	protected Object analyze(ASTAssignment r) {
		return analyze(r.getLeft());
	}

	@Override
	protected Object analyze(ASTCast r) {
		return r.getType();
	}

	@Override
	protected Object analyze(ASTConditional r) {
		return analyze(r.getThenExpr());
	}

	@Override
	protected Object analyze(ASTNewObject r) {
		return r.getTypeName();
	}

	@Override
	protected Object analyze(ASTPostOp r) {
		return _int;
	}

	@Override
	protected Object analyze(ASTPreOp r) {
		return _int;
	}


	@Override
	protected Object analyze(ASTVariableDeclaration r) {
		return r.getType();
	}

	@Override
	protected Object analyze(ASTVariableMultipleDeclaration r) {
		return r.getType();
	}


	@Override
	protected Object analyze(ASTAttributeAccess r) {
		IASTRE var = r.getVariableName();
		if(var instanceof ASTIdentifier && ((ASTIdentifier) var).getValue().equals("this")){
			IASTVar v = e.getVar(r.getAttributeName());
			if(v == null){
				//TODO when we add the inheritated env this will never be the case
				return "null";
			}
			return v.getType();
		}
		final String[] t = new String[1];
		r.getVariableName().visit(new DefualtASTREVisitor(){
			@Override
			public void enterASTIdentifier(ASTIdentifier elm) {
				t[0] = elm.getValue();
			}
		});
		if(t[0] == null){
			t[0] = r.getAttributeName();
		}
		else if(t[0].equals("this")) { //search the var
			t[0] = e.existVarName(r.getAttributeName()) ? e.getVar(r.getAttributeName()).getType() : null;
		}
		if(this._c != null){
			String tAttr = t[0];
			if(tAttr.endsWith(".this")){
				tAttr = tAttr.substring(0, tAttr.lastIndexOf("."));
			}
			return tAttr;
		}
		return t[0];
	}

	@Override
	protected Object analyze(ASTBinary r) {
		String type = null;
		switch (r.getOp()){
			case and:
			case or:
			case greater:
			case greaterEqual:
			case less:
			case lessEqual:
			case instanceOf:
			case equality:
			case not:
				type = _boolean; break;
			case plus:
				Object left = analyze(r.getLeft());
				Object right = analyze(r.getRight());
				if( (left != null && left.equals(_string)) || ( right != null && right.equals(_string)) ){
					type = _string;
				} else {
					type = _int;
				}
				break;
			default: type = _int; break;
		}
		return type;
	}

	@Override
	protected Object analyze(ASTUnary r) {
		if(r.getOp() == IASTRE.OPERATOR.not){
			return _boolean;
		} else {
			return _int;
		}
	}

	@Override
	protected Object analyze(ASTMethodCall r) {
		String method = r.getMethodName();
		if(e.existMethod(method)){
			//return e.getMethod(method).getRetType();
		}
		return null;
	}

	@Override
	protected Object analyze(ASTIdentifier r) {
		String expr = r.getValue();
		if(p.matcher(expr).matches()) return "int";
		if(expr.equals("true") || expr.equals("false")) return "boolean";
		if(expr.startsWith("\"") && expr.endsWith("\"")) {
			return "String";
		}
		if(expr.equals("this")) return "this";
		return e.getVar(expr) == null ? null : e.getVar(expr).getType();
	}


}
