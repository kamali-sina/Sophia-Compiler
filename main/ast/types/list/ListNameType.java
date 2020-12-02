package main.ast.types.list;

import main.ast.nodes.declaration.variableDec.VarDeclaration;
import main.ast.nodes.expression.Identifier;
import main.ast.types.Type;

public class ListNameType {
    private Identifier name;
    private Type type;

    public ListNameType(Type type) {
        this.name = new Identifier("");
        this.type = type;
    }

    public ListNameType(VarDeclaration varDeclaration) {
        this.name = varDeclaration.getVarName();
        this.type = varDeclaration.getType();
    }

    public ListNameType(Identifier name, Type type) {
        this.name = name;
        this.type = type;
    }

    public Identifier getName() {
        return name;
    }

    public void setName(Identifier name) {
        this.name = name;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
