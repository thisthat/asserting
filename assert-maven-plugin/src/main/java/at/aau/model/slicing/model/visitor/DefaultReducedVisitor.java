package at.aau.model.slicing.model.visitor;

import at.aau.intermediateModel.visitors.DefualtASTREVisitor;
import at.aau.model.slicing.model.*;

/**
 * Created by giovanni on 11/07/2017.
 */
public class DefaultReducedVisitor extends DefualtASTREVisitor implements ReducedVisitor {

    @Override
    public void enterAssignmet(Assignment elm) {

    }

    @Override
    public void enterIf(If elm) {

    }

    @Override
    public void enterMethodCall(MethodCall elm) {

    }

    @Override
    public void enterWhile(While elm) {

    }
    @Override
    public void enterDoWhile(DoWhile elm) {

    }

    @Override
    public void enterExpression(Expression elm) {

    }

    @Override
    public void exitAssignment(Assignment elm) {

    }

    @Override
    public void exitIf(If elm) {

    }

    @Override
    public void exitMethodCall(MethodCall elm) {

    }

    @Override
    public void exitExpression(Expression elm) {

    }

    @Override
    public void exitWhile(While elm) {

    }

    @Override
    public void exitDoWhile(DoWhile elm) {

    }
}
