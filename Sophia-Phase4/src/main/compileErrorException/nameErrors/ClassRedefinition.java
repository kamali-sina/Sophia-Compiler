package main.compileErrorException.nameErrors;

import main.ast.nodes.declaration.classDec.ClassDeclaration;
import main.compileErrorException.CompileErrorException;
import main.symbolTable.SymbolTable;
import main.symbolTable.exceptions.ItemAlreadyExistsException;
import main.symbolTable.items.ClassSymbolTableItem;

public class ClassRedefinition extends CompileErrorException {
    private ClassDeclaration classDeclaration;

    public ClassRedefinition(ClassDeclaration classDeclaration) {
        super(classDeclaration.getLine(), "Redefinition of class " + classDeclaration.getClassName().getName());
        this.classDeclaration = classDeclaration;
    }

    public void handleException() {
        String newName = this.classDeclaration.getClassName().getName() + "_";
        this.classDeclaration.getClassName().setName(newName);
        ClassSymbolTableItem symbolTableActorItem = new ClassSymbolTableItem(this.classDeclaration);
        symbolTableActorItem.setClassSymbolTable(SymbolTable.top);
        try {
            SymbolTable.root.put(symbolTableActorItem);
        } catch (ItemAlreadyExistsException exception) {
            this.handleException();
        }
    }

}
