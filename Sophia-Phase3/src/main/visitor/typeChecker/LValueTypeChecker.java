package main.visitor.typeChecker;

import main.ast.nodes.declaration.classDec.ClassDeclaration;
import main.ast.nodes.declaration.classDec.classMembersDec.FieldDeclaration;
import main.ast.nodes.declaration.classDec.classMembersDec.MethodDeclaration;
import main.ast.nodes.declaration.variableDec.VarDeclaration;
import main.ast.nodes.expression.*;
import main.ast.nodes.expression.values.ListValue;
import main.ast.nodes.expression.values.NullValue;
import main.ast.nodes.expression.values.primitive.BoolValue;
import main.ast.nodes.expression.values.primitive.IntValue;
import main.ast.nodes.expression.values.primitive.StringValue;
import main.ast.types.NoType;
import main.ast.types.Type;
import main.ast.types.functionPointer.FptrType;
import main.ast.types.single.ClassType;
import main.symbolTable.SymbolTable;
import main.symbolTable.exceptions.ItemNotFoundException;
import main.symbolTable.items.ClassSymbolTableItem;
import main.symbolTable.utils.graph.Graph;
import main.visitor.Visitor;
import main.symbolTable.utils.graph.Graph;
import main.symbolTable.utils.graph.exceptions.GraphDoesNotContainNodeException;

import java.util.ArrayList;
import java.util.Collection;

// a.foo

public class LValueTypeChecker extends Visitor<Type> {
    private Boolean isLValue = true;
    private final Graph<String> classHierarchy;
    private ClassDeclaration currentClass;

    public LValueTypeChecker(Graph<String> classHierarchy, ClassDeclaration currentClass) {
        this.classHierarchy = classHierarchy;
    }

    public Boolean getLValue() {
        return isLValue;
    }

    @Override
    public Type visit(BinaryExpression binaryExpression) {
        isLValue = false;
        return new NoType();
    }

    @Override
    public Type visit(UnaryExpression unaryExpression) {
        isLValue = false;
        return new NoType();
    }

    @Override
    public Type visit(ObjectOrListMemberAccess objectOrListMemberAccess) {
        objectOrListMemberAccess.getInstance().accept(this);
        return new NoType();
    }

    @Override
    public Type visit(Identifier identifier) {
        return new NoType();
    }

    @Override
    public Type visit(ListAccessByIndex listAccessByIndex) {
        listAccessByIndex.getInstance().accept(this);
        return new NoType();
    }

    @Override
    public Type visit(MethodCall methodCall) {
        isLValue = false;
        return new NoType();
    }

    @Override
    public Type visit(NewClassInstance newClassInstance) {
        isLValue = false;
        return new NoType();
    }

    @Override
    public Type visit(ThisClass thisClass) {
        return new NoType();
    }

    @Override
    public Type visit(ListValue listValue) {
        isLValue = false;
        return new NoType();
    }

    @Override
    public Type visit(NullValue nullValue) {
        isLValue = false;
        return new NoType();
    }

    @Override
    public Type visit(IntValue intValue) {
        isLValue = false;
        return new NoType();
    }

    @Override
    public Type visit(BoolValue boolValue) {
        isLValue = false;
        return new NoType();
    }

    @Override
    public Type visit(StringValue stringValue) {
        isLValue = false;
        return new NoType();
    }

    private FptrType getFunctionPointer(MethodDeclaration methodDec){
        ArrayList<VarDeclaration> args = methodDec.getArgs();
        ArrayList<Type> types = new ArrayList<>();
        for (VarDeclaration arg : args){
            types.add(arg.getType());
        }
        return new FptrType(types, methodDec.getReturnType());
    }

    private Type doesIdentifierExistInClass(String identifierName, ClassType classType){
        try {
            ClassSymbolTableItem checkClassSymbolTableItem = (ClassSymbolTableItem)
                    SymbolTable.root.getItem(ClassSymbolTableItem.START_KEY + classType.getClassName(), true);
            ClassDeclaration classDec = checkClassSymbolTableItem.getClassDeclaration();
            //is in Class?
            ArrayList<FieldDeclaration> classArgs = classDec.getFields();
            for (FieldDeclaration classArg : classArgs) {
                if (identifierName.equals(classArg.getVarDeclaration().getVarName().getName())) {
                    return classArg.getVarDeclaration().getType();
                }
            }
            ArrayList<MethodDeclaration> classMethods = classDec.getMethods();
            for (MethodDeclaration classMethod : classMethods) {
                if (identifierName.equals(classMethod.getMethodName().getName())) {
                    return getFunctionPointer(classMethod);
                }
            }
            //is in parents?
            try {
                Collection<String> parentNames
                        = classHierarchy.getParentsOfNode(classDec.getClassName().getName());
                for (String name : parentNames){
                    try {
                        ClassSymbolTableItem classSymbolTableItem = (ClassSymbolTableItem)
                                SymbolTable.root.getItem(ClassSymbolTableItem.START_KEY + name, true);
                        ArrayList<FieldDeclaration> classFields = classSymbolTableItem.getClassDeclaration().getFields();
                        for (FieldDeclaration classField : classFields) {
                            if (identifierName.equals(classField.getVarDeclaration().getVarName().getName())) {
                                return classField.getVarDeclaration().getType();
                            }
                        }
                        ArrayList<MethodDeclaration> methods = classSymbolTableItem.getClassDeclaration().getMethods();
                        for (MethodDeclaration method : methods) {
                            if (identifierName.equals(method.getMethodName().getName())) {
                                return getFunctionPointer(method);
                            }
                        }
                    } catch (ItemNotFoundException ignored) { }
                }
            } catch (GraphDoesNotContainNodeException e) { return null; }
        } catch (ItemNotFoundException e) { return null; }
        return null;
    }

}