package main.visitor.typeChecker;

import main.ast.nodes.Node;
import main.ast.nodes.declaration.classDec.ClassDeclaration;
import main.ast.nodes.declaration.classDec.classMembersDec.MethodDeclaration;
import main.ast.nodes.expression.*;
import main.ast.nodes.expression.operators.BinaryOperator;
import main.ast.nodes.expression.operators.UnaryOperator;
import main.ast.nodes.expression.values.ListValue;
import main.ast.nodes.expression.values.NullValue;
import main.ast.nodes.expression.values.primitive.BoolValue;
import main.ast.nodes.expression.values.primitive.IntValue;
import main.ast.nodes.expression.values.primitive.StringValue;
import main.ast.types.NoType;
import main.ast.types.NullType;
import main.ast.types.Type;
import main.ast.types.functionPointer.FptrType;
import main.ast.types.list.ListNameType;
import main.ast.types.list.ListType;
import main.ast.types.single.BoolType;
import main.ast.types.single.ClassType;
import main.ast.types.single.IntType;
import main.ast.types.single.StringType;
import main.compileErrorException.typeErrors.*;
import main.symbolTable.SymbolTable;
import main.symbolTable.exceptions.ItemNotFoundException;
import main.symbolTable.items.ClassSymbolTableItem;
import main.symbolTable.items.FieldSymbolTableItem;
import main.symbolTable.items.LocalVariableSymbolTableItem;
import main.symbolTable.items.MethodSymbolTableItem;
import main.symbolTable.utils.graph.Graph;
import main.visitor.Visitor;

import java.util.ArrayList;

public class ExpressionTypeChecker extends Visitor<Type> {
    private final Graph<String> classHierarchy;
    private ClassDeclaration currentClass;
    private MethodDeclaration currentMethod;
    private int typeValidationNumberOfErrors;
    private boolean seenNoneLvalue = false;
    private boolean isInMethodCallStmt = false;

    public ExpressionTypeChecker(Graph<String> classHierarchy) {
        this.classHierarchy = classHierarchy;
    }

    public void setCurrentClass(ClassDeclaration currentClass) {
        this.currentClass = currentClass;
    }

    public void setCurrentMethod(MethodDeclaration currentMethod) {
        this.currentMethod = currentMethod;
    }

    public void setIsInMethodCallStmt(boolean inIsMethodCallStmt) {
        isInMethodCallStmt = inIsMethodCallStmt;
    }

    public boolean isFirstSubTypeOfSecondMultiple(ArrayList<Type> first, ArrayList<Type> second) {
        if(first.size() != second.size())
            return false;
        for(int i = 0; i < first.size(); i++) {
            if(!isFirstSubTypeOfSecond(first.get(i), second.get(i)))
                return false;
        }
        return true;
    }

    public boolean isFirstSubTypeOfSecond(Type first, Type second) {
        if(first instanceof NoType)
            return true;
        else if(first instanceof IntType || first instanceof BoolType || first instanceof StringType)
            return first.toString().equals(second.toString());
        else if(first instanceof NullType)
            return second instanceof NullType || second instanceof FptrType || second instanceof ClassType;
        else if(first instanceof ClassType) {
            if(!(second instanceof ClassType))
                return false;
            return this.classHierarchy.isSecondNodeAncestorOf(((ClassType) first).getClassName().getName(), ((ClassType) second).getClassName().getName());
        }
        else if(first instanceof FptrType) {
            if(!(second instanceof FptrType))
                return false;
            Type firstRetType = ((FptrType) first).getReturnType();
            Type secondRetType = ((FptrType) second).getReturnType();
            if(!isFirstSubTypeOfSecond(firstRetType, secondRetType))
                return false;
            ArrayList<Type> firstArgsTypes = ((FptrType) first).getArgumentsTypes();
            ArrayList<Type> secondArgsTypes = ((FptrType) second).getArgumentsTypes();
            return isFirstSubTypeOfSecondMultiple(secondArgsTypes, firstArgsTypes);
        }
        else if(first instanceof ListType) {
            if(!(second instanceof ListType))
                return false;
            ArrayList<ListNameType> firstElements = ((ListType)first).getElementsTypes();
            ArrayList<ListNameType> secondElements = ((ListType)second).getElementsTypes();
            if(firstElements.size() != secondElements.size())
                return false;
            for(int i = 0; i < firstElements.size(); i++) {
                if (!isFirstSubTypeOfSecond(firstElements.get(i).getType(), secondElements.get(i).getType()))
                    return false;
            }
            return true;
        }
        return false;
    }

    public Type refineType(Type type) {
        typeValidationNumberOfErrors = 0;
        this.checkTypeValidation(type, new NullValue());
        if(typeValidationNumberOfErrors > 0)
            return new NoType();
        return type;
    }

    public void checkTypeValidation(Type type, Node node) {
        if(!(type instanceof ClassType || type instanceof FptrType || type instanceof ListType))
            return;
        if(type instanceof ListType) {
            ArrayList<ListNameType> types = ((ListType) type).getElementsTypes();
            if(types.size() == 0) {
                CannotHaveEmptyList exception = new CannotHaveEmptyList(node.getLine());
                node.addError(exception);
                typeValidationNumberOfErrors += 1;
                return;
            }
            boolean flag = false;
            for(int i = 0; i < types.size()-1; i++) {
                for(int j = i+1; j < types.size(); j++) {
                    String first = types.get(i).getName().getName();
                    String second = types.get(j).getName().getName();
                    if(first.equals("") || second.equals(""))
                        continue;
                    if(first.equals(second)) {
                        DuplicateListId exception = new DuplicateListId(node.getLine());
                        node.addError(exception);
                        typeValidationNumberOfErrors += 1;
                        flag = true;
                        break;
                    }
                }
                if(flag)
                    break;
            }
            for(ListNameType listNameType : types)
                this.checkTypeValidation(listNameType.getType(), node);
        }
        if(type instanceof ClassType) {
            String className = ((ClassType)type).getClassName().getName();
            if(!this.classHierarchy.doesGraphContainNode(className)) {
                ClassNotDeclared exception = new ClassNotDeclared(node.getLine(), className);
                node.addError(exception);
                typeValidationNumberOfErrors += 1;
            }
        }
        if(type instanceof FptrType) {
            Type retType = ((FptrType) type).getReturnType();
            ArrayList<Type> argsType = ((FptrType) type).getArgumentsTypes();
            this.checkTypeValidation(retType, node);
            for(Type argType : argsType)
                this.checkTypeValidation(argType, node);
        }
    }

    public boolean areAllSameType(ArrayList<Type> types) {
        if(types.size() == 0)
            return true;
        Type firstType = types.get(0);
        for(Type type : types)
            if(!isSameType(firstType, type))
                return false;
        return true;
    }

    public boolean isSameType(Type t1, Type t2) {
        return (t1 instanceof NoType) || (t2 instanceof NoType) || (isFirstSubTypeOfSecond(t1, t2) && isFirstSubTypeOfSecond(t2, t1));
    }

    public boolean isLvalue(Expression expression) {
        boolean prevIsCatchErrorsActive = Node.isCatchErrorsActive;
        boolean prevSeenNoneLvalue = this.seenNoneLvalue;
        Node.isCatchErrorsActive = false;
        this.seenNoneLvalue = false;
        expression.accept(this);
        boolean isLvalue = !this.seenNoneLvalue;
        this.seenNoneLvalue = prevSeenNoneLvalue;
        Node.isCatchErrorsActive = prevIsCatchErrorsActive;
        return isLvalue;
    }

    @Override
    public Type visit(BinaryExpression binaryExpression) {
        this.seenNoneLvalue = true;
        BinaryOperator operator = binaryExpression.getBinaryOperator();
        Type firstType = binaryExpression.getFirstOperand().accept(this);
        Type secondType = binaryExpression.getSecondOperand().accept(this);
        if((operator == BinaryOperator.eq) || (operator == BinaryOperator.neq)) {
            if(firstType instanceof NoType && secondType instanceof NoType)
                return new NoType();
            else if((firstType instanceof NoType && secondType instanceof ListType) ||
                    (secondType instanceof NoType && firstType instanceof ListType)) {
                UnsupportedOperandType exception = new UnsupportedOperandType(binaryExpression.getLine(), operator.name());
                binaryExpression.addError(exception);
                return new NoType();
            }
            else if(firstType instanceof NoType || secondType instanceof NoType)
                return new NoType();
            if(firstType instanceof IntType || firstType instanceof BoolType || firstType instanceof StringType)
                if(firstType.toString().equals(secondType.toString()))
                    return new BoolType();
            if((firstType instanceof ClassType && secondType instanceof NullType) ||
                    (firstType instanceof NullType && secondType instanceof ClassType) ||
                    (firstType instanceof ClassType && secondType instanceof ClassType &&
                            ((ClassType)firstType).getClassName().getName().equals(((ClassType)secondType).getClassName().getName()))) {
                return new BoolType();
            }
            if((firstType instanceof FptrType && secondType instanceof NullType) ||
                    (firstType instanceof NullType && secondType instanceof FptrType) ||
                    (firstType instanceof FptrType && secondType instanceof FptrType)) {
                return new BoolType();
            }
            if(firstType instanceof NullType && secondType instanceof NullType)
                return new BoolType();
        }
        if((operator == BinaryOperator.gt) || (operator == BinaryOperator.lt)) {
            if(firstType instanceof NoType && secondType instanceof NoType)
                return new NoType();
            else if((firstType instanceof NoType && !(secondType instanceof IntType)) ||
                    (secondType instanceof NoType && !(firstType instanceof IntType))) {
                UnsupportedOperandType exception = new UnsupportedOperandType(binaryExpression.getLine(), operator.name());
                binaryExpression.addError(exception);
                return new NoType();
            }
            else if(firstType instanceof NoType || secondType instanceof NoType)
                return new NoType();
            if((firstType instanceof IntType) && (secondType instanceof IntType))
                return new BoolType();
        }
        if((operator == BinaryOperator.add) || (operator == BinaryOperator.sub) ||
                (operator == BinaryOperator.mult) || (operator == BinaryOperator.div) || (operator == BinaryOperator.mod)) {
            if(firstType instanceof NoType && secondType instanceof NoType)
                return new NoType();
            else if((firstType instanceof NoType && !(secondType instanceof IntType)) ||
                    (secondType instanceof NoType && !(firstType instanceof IntType))) {
                UnsupportedOperandType exception = new UnsupportedOperandType(binaryExpression.getLine(), operator.name());
                binaryExpression.addError(exception);
                return new NoType();
            }
            else if(firstType instanceof NoType || secondType instanceof NoType)
                return new NoType();
            if((firstType instanceof IntType) && (secondType instanceof IntType))
                return new IntType();
        }

        if((operator == BinaryOperator.or) || (operator == BinaryOperator.and)) {
            if(firstType instanceof NoType && secondType instanceof NoType)
                return new NoType();
            else if((firstType instanceof NoType && !(secondType instanceof BoolType)) ||
                    (secondType instanceof NoType && !(firstType instanceof BoolType))) {
                UnsupportedOperandType exception = new UnsupportedOperandType(binaryExpression.getLine(), operator.name());
                binaryExpression.addError(exception);
                return new NoType();
            }
            else if(firstType instanceof NoType || secondType instanceof NoType)
                return new NoType();
            if((firstType instanceof BoolType) && (secondType instanceof BoolType))
                return new BoolType();
        }
        if(operator == BinaryOperator.assign) {
            boolean isFirstLvalue = this.isLvalue(binaryExpression.getFirstOperand());
            if(!isFirstLvalue) {
                LeftSideNotLvalue exception = new LeftSideNotLvalue(binaryExpression.getLine());
                binaryExpression.addError(exception);
            }
            if(firstType instanceof NoType || secondType instanceof NoType) {
                return new NoType();
            }
            boolean isSubtype = this.isFirstSubTypeOfSecond(secondType, firstType);
            if(isSubtype) {
                if(isFirstLvalue)
                    return secondType;
                return new NoType();
            }
            UnsupportedOperandType exception = new UnsupportedOperandType(binaryExpression.getLine(), operator.name());
            binaryExpression.addError(exception);
            return new NoType();
        }
        UnsupportedOperandType exception = new UnsupportedOperandType(binaryExpression.getLine(), operator.name());
        binaryExpression.addError(exception);
        return new NoType();
    }

    @Override
    public Type visit(UnaryExpression unaryExpression) {
        this.seenNoneLvalue = true;
        Type operandType = unaryExpression.getOperand().accept(this);
        UnaryOperator operator = unaryExpression.getOperator();
        if(operator == UnaryOperator.not) {
            if(operandType instanceof NoType)
                return new NoType();
            if(operandType instanceof BoolType)
                return operandType;
            UnsupportedOperandType exception = new UnsupportedOperandType(unaryExpression.getLine(), operator.name());
            unaryExpression.addError(exception);
            return new NoType();
        }
        else if(operator == UnaryOperator.minus) {
            if(operandType instanceof NoType)
                return new NoType();
            if(operandType instanceof IntType)
                return operandType;
            UnsupportedOperandType exception = new UnsupportedOperandType(unaryExpression.getLine(), operator.name());
            unaryExpression.addError(exception);
            return new NoType();
        }
        else {
            boolean isOperandLvalue = this.isLvalue(unaryExpression.getOperand());
            if(!isOperandLvalue) {
                IncDecOperandNotLvalue exception = new IncDecOperandNotLvalue(unaryExpression.getLine(), operator.name());
                unaryExpression.addError(exception);
            }
            if(operandType instanceof NoType)
                return new NoType();
            if(operandType instanceof IntType) {
                if(isOperandLvalue)
                    return operandType;
                return new NoType();
            }
            UnsupportedOperandType exception = new UnsupportedOperandType(unaryExpression.getLine(), operator.name());
            unaryExpression.addError(exception);
            return new NoType();
        }
    }

    @Override
    public Type visit(ObjectOrListMemberAccess objectOrListMemberAccess) {
        boolean prevSeenNoneLvalue = this.seenNoneLvalue;
        Type instanceType = objectOrListMemberAccess.getInstance().accept(this);
        if(objectOrListMemberAccess.getInstance() instanceof ThisClass)
            this.seenNoneLvalue = prevSeenNoneLvalue;
        String memberName = objectOrListMemberAccess.getMemberName().getName();
        if(instanceType instanceof NoType)
            return new NoType();
        else if(instanceType instanceof ClassType) {
            String className = ((ClassType) instanceType).getClassName().getName();
            SymbolTable classSymbolTable;
            try {
                classSymbolTable = ((ClassSymbolTableItem) SymbolTable.root.getItem(ClassSymbolTableItem.START_KEY + className, true)).getClassSymbolTable();
            } catch (ItemNotFoundException classNotFound) {
                return new NoType();
            }
            try {
                FieldSymbolTableItem fieldSymbolTableItem = (FieldSymbolTableItem) classSymbolTable.getItem(FieldSymbolTableItem.START_KEY + memberName, true);
                return this.refineType(fieldSymbolTableItem.getType());
            } catch (ItemNotFoundException memberNotField) {
                try {
                    MethodSymbolTableItem methodSymbolTableItem = (MethodSymbolTableItem) classSymbolTable.getItem(MethodSymbolTableItem.START_KEY + memberName, true);
                    this.seenNoneLvalue = true;
                    return new FptrType(methodSymbolTableItem.getArgTypes(), methodSymbolTableItem.getReturnType());
                } catch (ItemNotFoundException memberNotFound) {
                    if(memberName.equals(className)) {
                        this.seenNoneLvalue = true;
                        return new FptrType(new ArrayList<>(), new NullType());
                    }
                    MemberNotAvailableInClass exception = new MemberNotAvailableInClass(objectOrListMemberAccess.getLine(), memberName, className);
                    objectOrListMemberAccess.addError(exception);
                    return new NoType();
                }
            }
        }
        else if(instanceType instanceof ListType) {
            ArrayList<ListNameType> elementsTypes = ((ListType) instanceType).getElementsTypes();
            for(ListNameType elementType : elementsTypes) {
                if(elementType.getName().getName().equals(memberName))
                    return this.refineType(elementType.getType());
            }
            ListMemberNotFound exception = new ListMemberNotFound(objectOrListMemberAccess.getLine(), memberName);
            objectOrListMemberAccess.addError(exception);
            return new NoType();
        }
        else {
            MemberAccessOnNoneObjOrListType exception = new MemberAccessOnNoneObjOrListType(objectOrListMemberAccess.getLine());
            objectOrListMemberAccess.addError(exception);
            return new NoType();
        }
    }

    @Override
    public Type visit(Identifier identifier) {
        try {
            ClassSymbolTableItem classSymbolTableItem = (ClassSymbolTableItem) SymbolTable.root.getItem(ClassSymbolTableItem.START_KEY + this.currentClass.getClassName().getName(), true);
            SymbolTable classSymbolTable = classSymbolTableItem.getClassSymbolTable();
            MethodSymbolTableItem methodSymbolTableItem = (MethodSymbolTableItem) classSymbolTable.getItem(MethodSymbolTableItem.START_KEY + this.currentMethod.getMethodName().getName(), true);
            SymbolTable methodSymbolTable = methodSymbolTableItem.getMethodSymbolTable();
            LocalVariableSymbolTableItem localVariableSymbolTableItem = (LocalVariableSymbolTableItem) methodSymbolTable.getItem(LocalVariableSymbolTableItem.START_KEY + identifier.getName(), true);
            return this.refineType(localVariableSymbolTableItem.getType());
        } catch (ItemNotFoundException e) {
            VarNotDeclared exception = new VarNotDeclared(identifier.getLine(), identifier.getName());
            identifier.addError(exception);
            return new NoType();
        }
    }

    @Override
    public Type visit(ListAccessByIndex listAccessByIndex) {
        Type instanceType = listAccessByIndex.getInstance().accept(this);
        boolean prevSeenNoneLvalue = this.seenNoneLvalue;
        Type indexType = listAccessByIndex.getIndex().accept(this);
        this.seenNoneLvalue = prevSeenNoneLvalue;
        boolean indexErrored = false;
        if(!(indexType instanceof NoType || indexType instanceof IntType)) {
            ListIndexNotInt exception = new ListIndexNotInt(listAccessByIndex.getLine());
            listAccessByIndex.addError(exception);
            indexErrored = true;
        }
        if(instanceType instanceof ListType) {
            ArrayList<Type> types = new ArrayList<>();
            for(ListNameType listNameType : ((ListType)instanceType).getElementsTypes())
                types.add(listNameType.getType());
            boolean areAllSame = this.areAllSameType(types);
            if(!(listAccessByIndex.getIndex() instanceof IntValue) && !areAllSame) {
                CantUseExprAsIndexOfMultiTypeList exception = new CantUseExprAsIndexOfMultiTypeList(listAccessByIndex.getLine());
                listAccessByIndex.addError(exception);
                return new NoType();
            }
            if(indexErrored)
                return new NoType();
            if((listAccessByIndex.getIndex() instanceof IntValue) && !areAllSame && (((IntValue)listAccessByIndex.getIndex()).getConstant() < ((ListType)instanceType).getElementsTypes().size())) {
                int index = ((IntValue)listAccessByIndex.getIndex()).getConstant();
                return this.refineType(((ListType) instanceType).getElementsTypes().get(index).getType());
            }
            else {
                return this.refineType(((ListType) instanceType).getElementsTypes().get(0).getType());
            }
        }
        else if(!(instanceType instanceof NoType)) {
            ListAccessByIndexOnNoneList exception = new ListAccessByIndexOnNoneList(listAccessByIndex.getLine());
            listAccessByIndex.addError(exception);
        }
        return new NoType();
    }

    @Override
    public Type visit(MethodCall methodCall) {
        this.seenNoneLvalue = true;
        Type instanceType = methodCall.getInstance().accept(this);
        boolean prevIsInMethodCallStmt = this.isInMethodCallStmt;
        this.setIsInMethodCallStmt(false);
        ArrayList<Type> argsTypes = new ArrayList<>();
        for(Expression arg : methodCall.getArgs()) {
            argsTypes.add(arg.accept(this));
        }
        this.setIsInMethodCallStmt(prevIsInMethodCallStmt);
        if(!(instanceType instanceof FptrType || instanceType instanceof NoType)) {
            CallOnNoneFptrType exception = new CallOnNoneFptrType(methodCall.getLine());
            methodCall.addError(exception);
            return new NoType();
        }
        else if(instanceType instanceof NoType) {
            return new NoType();
        }
        else {
            ArrayList<Type> actualArgsTypes = ((FptrType) instanceType).getArgumentsTypes();
            Type returnType = ((FptrType) instanceType).getReturnType();
            boolean hasError = false;
            if(!isInMethodCallStmt && (returnType instanceof NullType)) {
                CantUseValueOfVoidMethod exception = new CantUseValueOfVoidMethod(methodCall.getLine());
                methodCall.addError(exception);
                hasError = true;
            }
            if(this.isFirstSubTypeOfSecondMultiple(argsTypes, actualArgsTypes)) {
                if(hasError)
                    return new NoType();
                return this.refineType(returnType);
            }
            else {
                MethodCallNotMatchDefinition exception = new MethodCallNotMatchDefinition(methodCall.getLine());
                methodCall.addError(exception);
                return new NoType();
            }
        }
    }

    @Override
    public Type visit(NewClassInstance newClassInstance) {
        this.seenNoneLvalue = true;
        String className = newClassInstance.getClassType().getClassName().getName();
        ArrayList<Type> newInstanceTypes = new ArrayList<>();
        for(Expression expression : newClassInstance.getArgs())
            newInstanceTypes.add(expression.accept(this));
        if(this.classHierarchy.doesGraphContainNode(className)) {
            try {
                ClassSymbolTableItem classSymbolTableItem = (ClassSymbolTableItem) SymbolTable.root.getItem(ClassSymbolTableItem.START_KEY + className, true);
                MethodSymbolTableItem methodSymbolTableItem = (MethodSymbolTableItem) classSymbolTableItem.getClassSymbolTable().getItem(MethodSymbolTableItem.START_KEY + className, true);
                ArrayList<Type> constructorActualTypes = methodSymbolTableItem.getArgTypes();
                if(this.isFirstSubTypeOfSecondMultiple(newInstanceTypes, constructorActualTypes)) {
                    return newClassInstance.getClassType();
                }
                else {
                    ConstructorArgsNotMatchDefinition exception = new ConstructorArgsNotMatchDefinition(newClassInstance);
                    newClassInstance.addError(exception);
                    return new NoType();
                }
            } catch (ItemNotFoundException ignored) {
                if(newInstanceTypes.size() != 0) {
                    ConstructorArgsNotMatchDefinition exception = new ConstructorArgsNotMatchDefinition(newClassInstance);
                    newClassInstance.addError(exception);
                    return new NoType();
                }
                else {
                    return newClassInstance.getClassType();
                }
            }
        }
        else {
            ClassNotDeclared exception = new ClassNotDeclared(newClassInstance.getLine(), className);
            newClassInstance.addError(exception);
            return new NoType();
        }
    }

    @Override
    public Type visit(ThisClass thisClass) {
        this.seenNoneLvalue = true;
        return new ClassType(currentClass.getClassName());
    }

    @Override
    public Type visit(ListValue listValue) {
        this.seenNoneLvalue = true;
        ArrayList<ListNameType> types = new ArrayList<>();
        for(Expression element : listValue.getElements()) {
            Type elementType = element.accept(this);
            types.add(new ListNameType(elementType));
        }
        return new ListType(types);
    }

    @Override
    public Type visit(NullValue nullValue) {
        this.seenNoneLvalue = true;
        return new NullType();
    }

    @Override
    public Type visit(IntValue intValue) {
        this.seenNoneLvalue = true;
        return new IntType();
    }

    @Override
    public Type visit(BoolValue boolValue) {
        this.seenNoneLvalue = true;
        return new BoolType();
    }

    @Override
    public Type visit(StringValue stringValue) {
        this.seenNoneLvalue = true;
        return new StringType();
    }
}
