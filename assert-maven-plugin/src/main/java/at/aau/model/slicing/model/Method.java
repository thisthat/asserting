package at.aau.model.slicing.model;

import at.aau.model.slicing.model.interfaces.Stm;
import at.aau.model.slicing.model.visitor.ReducedVisitor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by giovanni on 11/07/2017.
 */
public class Method extends Stm {

    List<Stm> body = new ArrayList<>();
    String name;
    String fullName;
    List<String> signature;

    public Method(int start, int end, int line, int lineEnd, String code, String fullName) {
        super(start, end, line, lineEnd, code);
        this.fullName = fullName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getSignature() {
        return signature;
    }

    public void setSignature(List<String> signature) {
        this.signature = signature;
    }

    public List<Stm> getBody() {
        return body;
    }

    public void setBody(List<Stm> body) {
        this.body = body;
    }

    @Override
    public void visit(ReducedVisitor visitor) {
        for(Stm s : body){
            s.visit(visitor);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Method method = (Method) o;
        if(!super.equals(o)) return false;
        if (body != null ? !body.equals(method.body) : method.body != null) return false;
        if (name != null ? !name.equals(method.name) : method.name != null) return false;
        return signature != null ? signature.equals(method.signature) : method.signature == null;
    }

    @Override
    public int hashCode() {
        int result = body != null ? body.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (signature != null ? signature.hashCode() : 0);
        return result;
    }

    public String getFullName() {
        return fullName;
    }
}
