package main.ast.nodes.declaration.variableDec;

import main.ast.nodes.declaration.Declaration;
import main.ast.nodes.expression.Identifier;
import main.ast.types.Type;
import main.visitor.IVisitor;

//line -> IDENTIFIER
public class VarDeclaration extends Declaration {
    private Identifier varName;
    private Type type;

    public VarDeclaration(Identifier varName, Type type) {
        this.varName = varName;
        this.type = type;
    }

    public Identifier getVarName() {
        return varName;
    }

    public void setVarName(Identifier varName) {
        this.varName = varName;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "VarDeclaration_" + this.varName.getName();
    }

    @Override
    public <T> T accept(IVisitor<T> visitor) {
        return visitor.visit(this);
    }


}
