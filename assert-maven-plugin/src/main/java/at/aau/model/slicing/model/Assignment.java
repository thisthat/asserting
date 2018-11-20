package at.aau.model.slicing.model;

import at.aau.intermediateModel.interfaces.IASTRE;
import at.aau.model.slicing.model.interfaces.Stm;
import at.aau.model.slicing.model.visitor.ReducedVisitor;

/**
 * Created by giovanni on 11/07/2017.
 */
public class Assignment extends Stm {

    String left = "";
    IASTRE right = null;

    public Assignment(int start, int end, int line, int lineEnd, String code) {
        super(start, end, line, lineEnd, code);
    }

    public Assignment(IASTRE stm) {
        super(  stm.getStart(),
                stm.getEnd(),
                stm.getLine(),
                stm.getLineEnd(),
                stm.getCode()
                );
    }

    public void setLeft(String left) {
        this.left = left;
    }

    public void setRight(IASTRE right) {
        this.right = right;
    }

    public String getLeft() {
        return left;
    }

    public IASTRE getRight() {
        return right;
    }

    @Override
    public void visit(ReducedVisitor visitor) {
        visitor.enterAssignmet(this);
        if(right != null)
            right.visit(visitor);
        visitor.exitAssignment(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Assignment that = (Assignment) o;

        if(!super.equals(o)) return false;

        if (left != null ? !left.equals(that.left) : that.left != null) return false;
        return right != null ? right.equals(that.right) : that.right == null;
    }

    @Override
    public int hashCode() {
        int result = left != null ? left.hashCode() : 0;
        result = 31 * result + (right != null ? right.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return left + "=" + right.print();
    }
}
