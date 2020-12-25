package main.visitor.nameAnalyzer;

import main.ast.nodes.Program;
import main.ast.nodes.declaration.classDec.ClassDeclaration;
import main.ast.nodes.declaration.classDec.classMembersDec.ConstructorDeclaration;
import main.ast.nodes.declaration.classDec.classMembersDec.FieldDeclaration;
import main.ast.nodes.declaration.classDec.classMembersDec.MethodDeclaration;
import main.ast.nodes.declaration.variableDec.VarDeclaration;
import main.compileErrorException.nameErrors.ClassRedefinition;
import main.compileErrorException.nameErrors.FieldRedefinition;
import main.compileErrorException.nameErrors.LocalVarRedefinition;
import main.compileErrorException.nameErrors.MethodRedefinition;
import main.symbolTable.SymbolTable;
import main.symbolTable.exceptions.ItemAlreadyExistsException;
import main.symbolTable.items.ClassSymbolTableItem;
import main.symbolTable.items.FieldSymbolTableItem;
import main.symbolTable.items.LocalVariableSymbolTableItem;
import main.symbolTable.items.MethodSymbolTableItem;
import main.visitor.Visitor;

public class NameCollector extends Visitor<Void> {

    @Override
    public Void visit(Program program) {
        SymbolTable.push(new SymbolTable());
        SymbolTable.root = SymbolTable.top;
        for(ClassDeclaration classDeclaration : program.getClasses()) {
            classDeclaration.accept(this);
        }
        return null;
    }

    @Override
    public Void visit(ClassDeclaration classDeclaration) {
        ClassSymbolTableItem classSymbolTableItem = new ClassSymbolTableItem(classDeclaration);
        SymbolTable.push(new SymbolTable(SymbolTable.top));
        classSymbolTableItem.setClassSymbolTable(SymbolTable.top);
        try {
            SymbolTable.root.put(classSymbolTableItem);
        } catch (ItemAlreadyExistsException e) {
            ClassRedefinition exception = new ClassRedefinition(classDeclaration);
            classDeclaration.addError(exception);
            exception.handleException();
        }
        for(FieldDeclaration fieldDeclaration : classDeclaration.getFields()) {
            fieldDeclaration.accept(this);
        }
        if(classDeclaration.getConstructor() != null) {
            classDeclaration.getConstructor().accept(this);
        }
        for(MethodDeclaration methodDeclaration : classDeclaration.getMethods()) {
            methodDeclaration.accept(this);
        }
        SymbolTable.pop();
        return null;
    }

    @Override
    public Void visit(ConstructorDeclaration constructorDeclaration) {
        this.visit((MethodDeclaration) constructorDeclaration);
        return null;
    }

    @Override
    public Void visit(MethodDeclaration methodDeclaration) {
        MethodSymbolTableItem methodSymbolTableItem = new MethodSymbolTableItem(methodDeclaration);
        SymbolTable methodSymbolTable = new SymbolTable(SymbolTable.top);
        methodSymbolTableItem.setMethodSymbolTable(methodSymbolTable);
        try {
            SymbolTable.top.put(methodSymbolTableItem);
        } catch (ItemAlreadyExistsException e) {
            MethodRedefinition exception = new MethodRedefinition(methodDeclaration);
            methodDeclaration.addError(exception);
        }
        SymbolTable.push(methodSymbolTable);
        for(VarDeclaration varDeclaration : methodDeclaration.getArgs()) {
            varDeclaration.accept(this);
        }
        for(VarDeclaration varDeclaration : methodDeclaration.getLocalVars()) {
            varDeclaration.accept(this);
        }
        SymbolTable.pop();
        return null;
    }

    @Override
    public Void visit(FieldDeclaration fieldDeclaration) {
        try {
            SymbolTable.top.put(new FieldSymbolTableItem(fieldDeclaration));
        } catch (ItemAlreadyExistsException e) {
            FieldRedefinition exception = new FieldRedefinition(fieldDeclaration);
            fieldDeclaration.addError(exception);
        }
        return null;
    }

    @Override
    public Void visit(VarDeclaration varDeclaration) {
        try {
            SymbolTable.top.put(new LocalVariableSymbolTableItem(varDeclaration));
        } catch (ItemAlreadyExistsException e) {
            LocalVarRedefinition exception = new LocalVarRedefinition(varDeclaration);
            varDeclaration.addError(exception);
        }
        return null;
    }

}
