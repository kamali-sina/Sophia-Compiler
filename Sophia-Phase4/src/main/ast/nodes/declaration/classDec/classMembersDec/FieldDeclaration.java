package main.ast.nodes.declaration.classDec.classMembersDec;

import main.ast.nodes.declaration.Declaration;
import main.ast.nodes.declaration.variableDec.VarDeclaration;
import main.visitor.IVisitor;

//line -> IDENTIFIER
public class FieldDeclaration extends Declaration {
    VarDeclaration varDeclaration;

    public FieldDeclaration(VarDeclaration varDeclaration) {
        this.varDeclaration = varDeclaration;
    }

    public VarDeclaration getVarDeclaration() {
        return varDeclaration;
    }

    public void setVarDeclaration(VarDeclaration varDeclaration) {
        this.varDeclaration = varDeclaration;
    }

    @Override
    public String toString() {
        return "FieldDeclaration_" + this.varDeclaration.getVarName().getName();
    }

    @Override
    public <T> T accept(IVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
