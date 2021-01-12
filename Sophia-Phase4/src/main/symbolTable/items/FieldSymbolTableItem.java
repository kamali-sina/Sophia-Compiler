package main.symbolTable.items;


import main.ast.nodes.declaration.classDec.classMembersDec.FieldDeclaration;
import main.ast.types.Type;

public class FieldSymbolTableItem extends SymbolTableItem {
    public static String START_KEY = "Field_";
    protected Type type;

    public FieldSymbolTableItem(FieldDeclaration fieldDeclaration) {
        this.name = fieldDeclaration.getVarDeclaration().getVarName().getName();
        this.type = fieldDeclaration.getVarDeclaration().getType();
    }

    public String getKey() {
        return START_KEY + this.name;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

}
