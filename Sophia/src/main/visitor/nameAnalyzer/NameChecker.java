package main.visitor.nameAnalyzer;

import main.ast.nodes.Program;
import main.ast.nodes.declaration.classDec.ClassDeclaration;
import main.ast.nodes.declaration.classDec.classMembersDec.ConstructorDeclaration;
import main.ast.nodes.declaration.classDec.classMembersDec.FieldDeclaration;
import main.ast.nodes.declaration.classDec.classMembersDec.MethodDeclaration;
import main.compileErrorException.nameErrors.ClassInCyclicInheritance;
import main.compileErrorException.nameErrors.FieldRedefinition;
import main.compileErrorException.nameErrors.MethodNameConflictWithField;
import main.compileErrorException.nameErrors.MethodRedefinition;
import main.symbolTable.SymbolTable;
import main.symbolTable.exceptions.ItemNotFoundException;
import main.symbolTable.items.ClassSymbolTableItem;
import main.symbolTable.items.FieldSymbolTableItem;
import main.symbolTable.items.MethodSymbolTableItem;
import main.symbolTable.utils.graph.Graph;
import main.visitor.Visitor;

public class NameChecker extends Visitor<Void> {
    private String currentClassName;
    private Graph<String> classHierarchy;
    Program root;

    public NameChecker(Graph<String> classHierarchy) {
        this.classHierarchy = classHierarchy;
    }

    private SymbolTable getCurrentClassSymbolTable() {
        try {
            ClassSymbolTableItem classSymbolTableItem = (ClassSymbolTableItem)
                    SymbolTable.root.getItem(ClassSymbolTableItem.START_KEY + this.currentClassName, true);
            return classSymbolTableItem.getClassSymbolTable();
        } catch (ItemNotFoundException ignored) {
            return null;
        }
    }

    @Override
    public Void visit(Program program) {
        this.root = program;
        for(ClassDeclaration classDeclaration : program.getClasses()) {
            this.currentClassName = classDeclaration.getClassName().getName();
            classDeclaration.accept(this);
        }
        return null;
    }

    @Override
    public Void visit(ClassDeclaration classDeclaration) {
        if(classDeclaration.getParentClassName() != null) {
            if (this.classHierarchy.isSecondNodeAncestorOf(classDeclaration.getParentClassName().getName(), classDeclaration.getClassName().getName())) {
                ClassInCyclicInheritance exception = new ClassInCyclicInheritance(classDeclaration);
                classDeclaration.addError(exception);
            }
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
        return null;
    }

    @Override
    public Void visit(ConstructorDeclaration constructorDeclaration) {
        this.visit((MethodDeclaration) constructorDeclaration);
        return null;
    }

    @Override
    public Void visit(MethodDeclaration methodDeclaration) {
        if(!methodDeclaration.hasError()) {
            try {
                SymbolTable classSymbolTable = this.getCurrentClassSymbolTable();
                classSymbolTable.getItem(MethodSymbolTableItem.START_KEY + methodDeclaration.getMethodName().getName(), false);
                MethodRedefinition exception = new MethodRedefinition(methodDeclaration);
                methodDeclaration.addError(exception);
            } catch (ItemNotFoundException ignored) {
            }
        }
        boolean errored = false;
        try {
            SymbolTable classSymbolTable = this.getCurrentClassSymbolTable();
            classSymbolTable.getItem(FieldSymbolTableItem.START_KEY + methodDeclaration.getMethodName().getName(), true);
            MethodNameConflictWithField exception = new MethodNameConflictWithField(methodDeclaration);
            methodDeclaration.addError(exception);
            errored = true;
        } catch (ItemNotFoundException ignored) {
        }
        if(!errored)
            for(ClassDeclaration classDeclaration : root.getClasses()) {
                String childName = classDeclaration.getClassName().getName();
                if(classHierarchy.isSecondNodeAncestorOf(childName, currentClassName)) {
                    try {
                        ClassSymbolTableItem childSymbolTableItem = (ClassSymbolTableItem) SymbolTable.root.getItem(ClassSymbolTableItem.START_KEY + childName, true);
                        SymbolTable childSymbolTable = childSymbolTableItem.getClassSymbolTable();
                        childSymbolTable.getItem(FieldSymbolTableItem.START_KEY + methodDeclaration.getMethodName().getName(), true);
                        MethodNameConflictWithField exception = new MethodNameConflictWithField(methodDeclaration);
                        methodDeclaration.addError(exception);
                        break;
                    } catch (ItemNotFoundException ignored) {
                    }
                }
            }
        return null;
    }

    @Override
    public Void visit(FieldDeclaration fieldDeclaration) {
        if(!fieldDeclaration.hasError()) {
            try {
                SymbolTable classSymbolTable = this.getCurrentClassSymbolTable();
                classSymbolTable.getItem(FieldSymbolTableItem.START_KEY + fieldDeclaration.getVarDeclaration().getVarName().getName(), false);
                FieldRedefinition exception = new FieldRedefinition(fieldDeclaration);
                fieldDeclaration.addError(exception);
            } catch (ItemNotFoundException ignored) {
            }
        }
        return null;
    }

}
