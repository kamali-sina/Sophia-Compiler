package main.visitor.typeChecker;

import main.ast.nodes.declaration.classDec.ClassDeclaration;
import main.ast.nodes.declaration.classDec.classMembersDec.FieldDeclaration;
import main.ast.nodes.declaration.classDec.classMembersDec.MethodDeclaration;
import main.ast.nodes.declaration.variableDec.VarDeclaration;
import main.ast.nodes.expression.*;
import main.ast.nodes.expression.operators.BinaryOperator;
import main.ast.nodes.expression.operators.UnaryOperator;
import main.ast.nodes.expression.values.ListValue;
import main.ast.nodes.expression.values.NullValue;
import main.ast.nodes.expression.values.Value;
import main.ast.nodes.expression.values.primitive.BoolValue;
import main.ast.nodes.expression.values.primitive.IntValue;
import main.ast.nodes.expression.values.primitive.StringValue;
import main.ast.types.NoType;
import main.ast.types.NullType;
import main.ast.types.Type;
import main.ast.types.list.ListNameType;
import main.ast.types.list.ListType;
import main.ast.types.single.BoolType;
import main.ast.types.single.ClassType;
import main.ast.types.single.IntType;
import main.ast.types.functionPointer.FptrType;
import main.ast.types.single.StringType;
import main.symbolTable.exceptions.ItemNotFoundException;
import main.symbolTable.utils.graph.Graph;
import main.symbolTable.utils.graph.exceptions.GraphDoesNotContainNodeException;
import main.visitor.Visitor;
import main.symbolTable.SymbolTable;
import main.symbolTable.items.*;
import main.compileErrorException.typeErrors.*;
/*
1-varNotDeclared
2-ClassNotDeclared
3-check if member exists
4-check operand types for operators
6-left side must be lvalue not rvalue
7-lvalue boodan operator haye ++ and --
*8-cant call not callable -------> dont check 13,15
12-multi type list index must be integerVal mostaghiman
15-args in method call must match definition
16-constructor args must match definition(check in new classInstance)
18-list elements cant have same ids (maybe Sina's job)
22-cant index non list objects
23-list index must be integer
24-id must exist in list
30-object or list member access on a expression that is neither a list not object
 */
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;


public class ExpressionTypeChecker extends Visitor<Type> {
    private final Graph<String> classHierarchy;
    private ClassDeclaration currentClass;
    private MethodDeclaration currentMethod;

    public void setCurrentClass(ClassDeclaration classDec){ this.currentClass = classDec;}

    public void setCurrentMethod(MethodDeclaration methodDec){ this.currentMethod = methodDec;}

    public ClassDeclaration getCurrentClass(){ return this.currentClass;}

    public MethodDeclaration getCurrentMethod(){ return this.currentMethod;}

    public ExpressionTypeChecker(Graph<String> classHierarchy) {
        this.classHierarchy = classHierarchy;
    }

    @Override
    public Type visit(BinaryExpression binaryExpression) {
        Type type1st = binaryExpression.getFirstOperand().accept(this);
        Type type2nd = binaryExpression.getSecondOperand().accept(this);
        BinaryOperator op = binaryExpression.getBinaryOperator();
        if (op == BinaryOperator.eq || op == BinaryOperator.neq){
            //TODO:both must be same shit except list and null for funcPtr
        }else if (op == BinaryOperator.and || op == BinaryOperator.or){
            //TODO:must be bool
        }else {
            //TODO:must be int
        }
        return null;
    }

    @Override
    public Type visit(UnaryExpression unaryExpression) {
        Type operandType = unaryExpression.getOperand().accept(this);
        UnaryOperator op = unaryExpression.getOperator();
        //TODO: check that lvalue rvalue shit
        if (op == UnaryOperator.not){
            //TODO: must be bool
            if (operandType instanceof BoolType){
                return new BoolType();
            }
//            unaryExpression.addError(new UnsupportedOperandType());
        }else{
            //TODO: must be int
        }
        return null;
    }

    @Override
    public Type visit(ObjectOrListMemberAccess objectOrListMemberAccess) {
        //TODO
        return null;
    }

    @Override
    public Type visit(Identifier identifier) {
        Type type = this.doesIdentifierExist(identifier.getName());
        if (type == null){
            identifier.addError(new VarNotDeclared(identifier.getLine(), identifier.getName()));
            return new NoType();
        }else{
            return type;
        }
    }

    @Override
    public Type visit(ListAccessByIndex listAccessByIndex) {
        //TODO
        return null;
    }

    @Override
    public Type visit(MethodCall methodCall) {
        Type insType = methodCall.getInstance().accept(this);
        ArrayList<Expression> args = methodCall.getArgs();
        if (insType instanceof FptrType){
            FptrType castedInsType = (FptrType)insType;
            ArrayList<Type> argTypes = castedInsType.getArgumentsTypes();
            //check if count is correct
            if (argTypes.size() != args.size()){
                methodCall.addError(new MethodCallNotMatchDefinition(methodCall.getLine()));
                return new NoType();
            }
            for (int i = 0 ; i < argTypes.size() ; i++){
                Type argType = args.get(i).accept(this);
                if (!this.isSecondSubtypeOfFirst(argTypes.get(i), argType)){
                    methodCall.addError(new MethodCallNotMatchDefinition(methodCall.getLine()));
                    return new NoType();
                }
            }
            return castedInsType.getReturnType();
        }else if(insType instanceof NoType){
            return new NoType();
        }else{
            methodCall.addError(new CallOnNoneFptrType(methodCall.getLine()));
            return new NoType();
        }
    }

    @Override
    public Type visit(NewClassInstance newClassInstance) {
        ClassType classType = newClassInstance.getClassType();
        try {
            ClassSymbolTableItem classSymbolTableItem = (ClassSymbolTableItem) SymbolTable.root.getItem
                    (ClassSymbolTableItem.START_KEY + classType.getClassName().getName(), true);
            ArrayList<FieldDeclaration> classFields = classSymbolTableItem.getClassDeclaration().getFields();
            ArrayList<Expression> args = newClassInstance.getArgs();
            if (args.size() != classFields.size()){
                newClassInstance.addError(new ConstructorArgsNotMatchDefinition(newClassInstance));
                return new NoType();
            }
            for (int i = 0 ; i < args.size() ; i++){
                Type argType = args.get(i).accept(this);
                Type classArgType = classFields.get(i).getVarDeclaration().getType();
                if (!this.isSecondSubtypeOfFirst(classArgType, argType)){
                    newClassInstance.addError(new ConstructorArgsNotMatchDefinition(newClassInstance));
                    return new NoType();
                }
            }
        } catch (ItemNotFoundException e) {
            newClassInstance.addError(new ClassNotDeclared(newClassInstance.getLine(),
                    newClassInstance.getClassType().getClassName().getName()));
            return new NoType();
        }

        return newClassInstance.getClassType();
    }

    @Override
    public Type visit(ThisClass thisClass) {
        return new ClassType(this.getCurrentClass().getClassName());
    }

    @Override
    public Type visit(ListValue listValue) {
        ArrayList<ListNameType> elementsTypes = new ArrayList<>();
        for (Expression exp : listValue.getElements()){
            Type type = exp.accept(this);
            elementsTypes.add(new ListNameType(type));
        }
        return new ListType(elementsTypes);
    }

    @Override
    public Type visit(NullValue nullValue) {
        return new NullType();
    }

    @Override
    public Type visit(IntValue intValue) {
        return new IntType();
    }

    @Override
    public Type visit(BoolValue boolValue) {
        return new BoolType();
    }

    @Override
    public Type visit(StringValue stringValue) {
        return new StringType();
    }

    private Boolean isSecondSubtypeOfFirst(Type first, Type second){
        if (first instanceof NoType || second instanceof NoType){
            return true;
        }
        if (first instanceof FptrType || first instanceof ClassType){
            if (second instanceof NullType) return true;
            if (!first.getClass().equals(second.getClass())) return false;
            if (first instanceof ClassType){
                return ((ClassType) first).getClassName().getName().equals(((ClassType) second).getClassName().getName());
            }else{
                FptrType castedFirst = (FptrType) first;
                FptrType castedSecond = (FptrType) second;
                if (!this.isSecondSubtypeOfFirst(castedFirst.getReturnType(),castedSecond.getReturnType())){
                    return false;
                }
                for (int i = 0 ; i < castedFirst.getArgumentsTypes().size() ; i++){
                    Type arg1 = castedFirst.getArgumentsTypes().get(i);
                    Type arg2 = castedSecond.getArgumentsTypes().get(i);
                    if (!this.isSecondSubtypeOfFirst(arg1,arg2)){
                        return false;
                    }
                }
                return true;
            }
        }else if (first instanceof ListType){
            if (!first.getClass().equals(second.getClass())) return false;
            ListType lis1 = (ListType)first;
            ListType lis2 = (ListType)second;
            if (lis1.getElementsTypes().size() != lis2.getElementsTypes().size()) return false;
            for (int i = 0 ; i < lis1.getElementsTypes().size() ; i++){
                if (!lis1.getElementsTypes().get(i).getClass().equals(lis2.getElementsTypes().get(i).getClass()))
                    return false;
            }
        }else{
            return first.getClass().equals(second.getClass());
        }
        return true;
    }

    private Type doesIdentifierExist(String identifierName){
        //is in method?
        ArrayList<VarDeclaration> methodArgs = this.getCurrentMethod().getLocalVars();
        for (VarDeclaration methodArg : methodArgs) {
            if (identifierName.equals(methodArg.getVarName().getName())) {
                return methodArg.getType();
            }
        }
        //is in Class?
        ArrayList<FieldDeclaration> classArgs = this.getCurrentClass().getFields();
        for (FieldDeclaration classArg : classArgs) {
            if (identifierName.equals(classArg.getVarDeclaration().getVarName().getName())) {
                return classArg.getVarDeclaration().getType();
            }
        }
        //is in parents?
        while (true) {
            try {
                Collection<String> parentNames
                        = classHierarchy.getParentsOfNode(this.getCurrentClass().getClassName().getName());
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
                    } catch (ItemNotFoundException ignored) { }
                }
            } catch (GraphDoesNotContainNodeException e) { break; }
        }
        return null;
    }
}
