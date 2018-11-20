package at.aau.model.slicing.model.visitor;

import at.aau.intermediateModel.interfaces.ASTREVisitor;
import at.aau.model.slicing.model.*;

/**
 * Created by giovanni on 11/07/2017.
 */
public interface ReducedVisitor extends ASTREVisitor {
    void enterAssignmet(Assignment elm);
    void enterIf(If elm);
    void enterMethodCall(MethodCall elm);
    void enterWhile(While elm);
    void enterDoWhile(DoWhile elm);
    void enterExpression(Expression elm);
    void exitAssignment(Assignment elm);
    void exitIf(If elm);
    void exitMethodCall(MethodCall elm);
    void exitWhile(While elm);
    void exitDoWhile(DoWhile elm);
    void exitExpression(Expression elm);

}
