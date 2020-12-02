package main.ast.nodes.declaration.classDec.classMembersDec;

import main.ast.nodes.expression.Identifier;
import main.ast.types.NullType;
import main.visitor.IVisitor;

//line -> DEF
public class ConstructorDeclaration extends MethodDeclaration{

    public ConstructorDeclaration(Identifier methodName) {
        super(methodName, new NullType());
    }

    @Override
    public String toString() {
        return "ConstructorDeclaration_" + this.methodName.getName();
    }

    @Override
    public <T> T accept(IVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
