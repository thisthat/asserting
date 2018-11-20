package at.aau.intermediateModel.visitors.creation.filter;

import intermediateModel.structure.ASTClass;

public interface Filter {
    void filter(ASTClass c);
}
