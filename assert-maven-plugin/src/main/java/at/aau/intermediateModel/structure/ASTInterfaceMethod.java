package at.aau.intermediateModel.structure;

import java.util.List;
import java.util.Objects;

public class ASTInterfaceMethod {
    String interfaceName;
    String methodName;
    List<String> signature;

    public ASTInterfaceMethod(String name, String mName, List<String> signature) {
        this.interfaceName = name;
        this.methodName = mName;
        this.signature = signature;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public String getMethodName() {
        return methodName;
    }

    public List<String> getSignature() {
        return signature;
    }

    public boolean isEqualMethod(ASTMethod m){
        if(m.getParameters().size() != this.signature.size()) return false;
        int size = this.signature.size();
        for(int i = 0; i < size; i++){
            String t1 = this.signature.get(i);
            String t2 = m.getParameters().get(i).getType();
            if(!t1.equals(t2)) return false;
        }
        return methodName.equals(m.getName());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ASTInterfaceMethod that = (ASTInterfaceMethod) o;
        return Objects.equals(interfaceName, that.interfaceName) &&
                Objects.equals(methodName, that.methodName) &&
                Objects.equals(signature, that.signature);
    }

    @Override
    public int hashCode() {

        return Objects.hash(interfaceName, methodName, signature);
    }
}
