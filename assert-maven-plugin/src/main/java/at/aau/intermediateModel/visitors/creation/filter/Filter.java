package at.aau.intermediateModel.visitors.creation.filter;

import at.aau.intermediateModel.structure.ASTClass;

public interface Filter {
    void filter(ASTClass c);
}
