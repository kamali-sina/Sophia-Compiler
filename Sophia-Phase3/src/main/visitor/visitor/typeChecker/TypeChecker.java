package main.visitor.typeChecker;

import main.ast.nodes.Program;
import main.ast.nodes.declaration.classDec.ClassDeclaration;
import main.ast.nodes.declaration.classDec.classMembersDec.ConstructorDeclaration;
import main.ast.nodes.declaration.classDec.classMembersDec.FieldDeclaration;
import main.ast.nodes.declaration.classDec.classMembersDec.MethodDeclaration;
import main.ast.nodes.declaration.variableDec.VarDeclaration;
import main.ast.nodes.expression.operators.BinaryOperator;
import main.ast.nodes.statement.*;
import main.ast.nodes.statement.loop.BreakStmt;
import main.ast.nodes.statement.loop.ContinueStmt;
import main.ast.nodes.statement.loop.ForStmt;
import main.ast.nodes.statement.loop.ForeachStmt;
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
import main.compileErrorException.CompileErrorException;
import main.compileErrorException.typeErrors.*;
import main.symbolTable.SymbolTable;
import main.symbolTable.utils.graph.Graph;
import main.visitor.Visitor;
import main.ast.nodes.expression.operators.BinaryOperator;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TypeChecker extends Visitor<Void> {
    private final Graph<String> classHierarchy;
    private final ExpressionTypeChecker expressionTypeChecker;
    private ClassDeclaration currentClass;
    private MethodDeclaration currentMethod;
    private boolean loopBlock = false;
    private boolean hasUnreachableStmt = false;
    private boolean doesVarHaveError = false;

    public ClassDeclaration getCurrentClass() { return this.currentClass;}

    public void setCurrentClass(ClassDeclaration classDec) {
        this.currentClass = classDec;
        this.expressionTypeChecker.setCurrentClass(classDec);
    }

    public MethodDeclaration getCurrentMethod() { return this.currentMethod;}

    public void setCurrentMethod(MethodDeclaration methodDec) {
        this.currentMethod = methodDec;
        this.expressionTypeChecker.setCurrentMethod(methodDec);
    }

    public boolean getLoopBlock() { return this.loopBlock; }

    public void setLoopBlock(boolean isLoopBlock) { this.loopBlock = isLoopBlock; }

    public boolean getHasUnreachableStmt() { return this.hasUnreachableStmt; }

    public void setHasUnreachableStmt(boolean hasUnreachableStmt) { this.hasUnreachableStmt = hasUnreachableStmt; }

    public boolean getDoesVarHaveError() { return this.doesVarHaveError; }

    public void setDoesVarHaveError(boolean doesVarHaveError) { this.doesVarHaveError = doesVarHaveError; }

    public TypeChecker(Graph<String> classHierarchy) {
        this.classHierarchy = classHierarchy;
        this.expressionTypeChecker = new ExpressionTypeChecker(classHierarchy);
    }

    /*
    * 2 - ClassNotDeclared
    * 4 - UnsupportedOperandType
    * 5 - ConditionNotBool
    * 6 - LeftSideNotLvalue
    * 9 - ContinueBreakNotInLoop
    * 10 - UnsupportedTypeForPrint
    * 11 - CannotHaveEmptyList
    * 13 - CantUseValueOfVoidMethod
    * 14 - ReturnValueNotMatchMethodReturnType
    * 17 - ConstructorNotSameNameAsClass
    * 18 - DuplicateListId
    * 19 - ForeachCantIterateNoneList *
    * 20 - ForeachListElementsNotSameType
    * 21 - ForeachVarNotMatchList
    * 25 - NoMainClass
    * 26 - MainClassCantExtend
    * 27 - CannotExtendFromMainClass
    * 28 - NoConstructorInMainClass
    * 29 - MainConstructorCantHaveArgs
    * 31 - MissingReturnStatement
    * 32 - UnreachableStatements
    *
    * If Error 19 occurs omit Errors 20 and 21
    * */

    @Override
    public Void visit(Program program) {
        if(!this.classHierarchy.doesGraphContainNode("Main")) {
            NoMainClass noMainClass = new NoMainClass(); // Error 25
            program.addError(noMainClass);
        }
        // Visiting every classDeclaration
        for(ClassDeclaration classDeclaration : program.getClasses()) {
            classDeclaration.accept(this);
        }
        return null;
    }

    @Override
    public Void visit(ClassDeclaration classDeclaration) {
        this.setCurrentClass(classDeclaration);
        //classDeclaration.getClassName().accept(this.expressionTypeChecker);
        if(classDeclaration.getParentClassName() != null) { // Class has a parent
            if(classDeclaration.getClassName().getName().equals("Main")) {   // Class is Main
                MainClassCantExtend mainClassCantExtend = new MainClassCantExtend(classDeclaration.getLine()); // Error 26
                classDeclaration.addError(mainClassCantExtend);
            }
            if(classDeclaration.getParentClassName().getName().equals("Main")) { // Parent class is Main
                CannotExtendFromMainClass cannotExtendFromMainClass =
                        new CannotExtendFromMainClass(classDeclaration.getLine()); // Error 27
                classDeclaration.addError(cannotExtendFromMainClass);
            }
            ClassType parentClass = new ClassType(classDeclaration.getParentClassName());
            if(!(this.expressionTypeChecker.doesClassExist(parentClass))) {
                ClassNotDeclared classNotDeclared =
                        new ClassNotDeclared(classDeclaration.getLine(),
                                classDeclaration.getParentClassName().getName());  // Error 2
                classDeclaration.addError(classNotDeclared);
            }
        }
        for(FieldDeclaration fieldDeclaration : classDeclaration.getFields()) {
            fieldDeclaration.accept(this);
        }
        if(classDeclaration.getConstructor() != null) { // Class has constructor
            if((classDeclaration.getClassName().getName().equals("Main")) &&
                    (classDeclaration.getConstructor().getArgs().size() != 0)) {
                MainConstructorCantHaveArgs mainConstructorCantHaveArgs =
                        new MainConstructorCantHaveArgs(classDeclaration.getConstructor().getLine()); // Error 29
                classDeclaration.getConstructor().addError(mainConstructorCantHaveArgs);
            }
            classDeclaration.getConstructor().accept(this);
        } else {    // Class doesn't have constructor
            if(classDeclaration.getClassName().getName().equals("Main")) {
                NoConstructorInMainClass noConstructorInMainClass = new NoConstructorInMainClass(classDeclaration); // Error 28
                classDeclaration.addError(noConstructorInMainClass);
            }
        }
        for(MethodDeclaration methodDeclaration : classDeclaration.getMethods()) {
            methodDeclaration.accept(this);
        }
        // IDK
        this.setCurrentClass(null);
        return null;
    }

    @Override
    public Void visit(ConstructorDeclaration constructorDeclaration) {
        if(!constructorDeclaration.getMethodName().getName().equals(this.getCurrentClass().getClassName().getName())) {
            ConstructorNotSameNameAsClass constructorNotSameNameAsClass =
                    new ConstructorNotSameNameAsClass(constructorDeclaration.getLine());  // Error 17
            constructorDeclaration.addError(constructorNotSameNameAsClass);
        }
        this.visit((MethodDeclaration) constructorDeclaration);
        return null;
    }

    @Override
    public Void visit(MethodDeclaration methodDeclaration) {
        this.setCurrentMethod(methodDeclaration);
        //methodDeclaration.getMethodName().accept(this.expressionTypeChecker);
        Type returnType = methodDeclaration.getReturnType();
        for(VarDeclaration varDeclaration : methodDeclaration.getArgs()) {
            varDeclaration.accept(this);
        }
        for(VarDeclaration varDeclaration : methodDeclaration.getLocalVars()) {
            varDeclaration.accept(this);
        }
        for(Statement statement : methodDeclaration.getBody()) {
            statement.accept(this);
        }
        if(!(returnType instanceof NullType)) {  // Return type is not void
            if(!methodDeclaration.getDoesReturn()) {    // There isn't a return statement
                MissingReturnStatement missingReturnStatement = new MissingReturnStatement(methodDeclaration);  // Error 31
                methodDeclaration.addError(missingReturnStatement);
            }
        }
        // IDK
        this.setCurrentMethod(null);
        return null;
    }

    @Override
    public Void visit(FieldDeclaration fieldDeclaration) {
        fieldDeclaration.getVarDeclaration().accept(this);
        return null;
    }

    @Override
    public Void visit(VarDeclaration varDeclaration) {
        Type varType = varDeclaration.getType();
        this.setDoesVarHaveError(false);
        this.isTypeValid(varType, varDeclaration);
        if(this.getDoesVarHaveError()) {
            varDeclaration.setType(new NoType());
        }
        return null;
    }

    @Override
    public Void visit(AssignmentStmt assignmentStmt) {
        Type lValueType = assignmentStmt.getlValue().accept(this.expressionTypeChecker);
        Type rValueType = assignmentStmt.getrValue().accept(this.expressionTypeChecker);
        if(!this.expressionTypeChecker.islValue(assignmentStmt.getlValue())) {
            LeftSideNotLvalue leftSideNotLvalue = new LeftSideNotLvalue(assignmentStmt.getLine()); // Error 6
            assignmentStmt.addError(leftSideNotLvalue);
        }
        if(!this.expressionTypeChecker.isSecondSubtypeOfFirst(lValueType, rValueType)) {
            UnsupportedOperandType unsupportedOperandType =
                    new UnsupportedOperandType(assignmentStmt.getLine(), BinaryOperator.assign.name()); // Error 4
            assignmentStmt.addError(unsupportedOperandType);
        }
        return null;
    }

    @Override
    public Void visit(BlockStmt blockStmt) {
        for(Statement statement : blockStmt.getStatements()) {
            if((this.currentMethod.getDoesReturn() || this.currentMethod.getDoesBreak() ||
                    this.currentMethod.getDoesContinue()) && this.getHasUnreachableStmt()) {
                UnreachableStatements unreachableStatements = new UnreachableStatements(statement); // Error 32
                statement.addError(unreachableStatements);
                this.setHasUnreachableStmt(true);
            }
            statement.accept(this);
        }
        return null;
    }

    @Override
    public Void visit(ConditionalStmt conditionalStmt) {
        Type conditionType = conditionalStmt.getCondition().accept(this.expressionTypeChecker);
        if(!(conditionType instanceof BoolType)) {  // If condition type is not boolean
            ConditionNotBool conditionNotBool = new ConditionNotBool(conditionalStmt.getCondition().getLine()); // Error 5
            conditionalStmt.addError(conditionNotBool);
        }
        if(conditionalStmt.getThenBody() != null) {
            conditionalStmt.getThenBody().accept(this);
        }
        if(conditionalStmt.getElseBody() != null) {
            conditionalStmt.getElseBody().accept(this);
        }
        return null;
    }

    @Override
    public Void visit(MethodCallStmt methodCallStmt) {
        this.expressionTypeChecker.methodCallStatement = true;
        methodCallStmt.getMethodCall().accept(this.expressionTypeChecker);
        this.expressionTypeChecker.methodCallStatement = false;
        return null;
    }

    @Override
    public Void visit(PrintStmt print) {
        Type argType = print.getArg().accept(this.expressionTypeChecker);
        if(argType instanceof IntType || argType instanceof BoolType || argType instanceof StringType
                || argType instanceof NoType) {

        }
        else {    // Print arg is not int, boolean and string
            UnsupportedTypeForPrint unsupportedTypeForPrint = new UnsupportedTypeForPrint(print.getLine()); // Error 10
            print.addError(unsupportedTypeForPrint);
        }
        return null;
    }

    @Override
    public Void visit(ReturnStmt returnStmt) {
        Type returnType = returnStmt.getReturnedExpr().accept(this.expressionTypeChecker);
        this.getCurrentMethod().setDoesReturn(true);
        if(!this.expressionTypeChecker.isSecondSubtypeOfFirst(this.getCurrentMethod().getReturnType(), returnType)) {
            ReturnValueNotMatchMethodReturnType returnValueNotMatchMethodReturnType =
                    new ReturnValueNotMatchMethodReturnType(returnStmt);  // Error 14
            returnStmt.addError(returnValueNotMatchMethodReturnType);
        }
        return null;
    }

    @Override
    public Void visit(BreakStmt breakStmt) {
        this.currentMethod.setDoesBreak(true);
        if(!this.getLoopBlock()) {  // Not within a loop block
            ContinueBreakNotInLoop continueBreakNotInLoop = new ContinueBreakNotInLoop(breakStmt.getLine(), 0); // Error 9
            breakStmt.addError(continueBreakNotInLoop);
        }
        return null;
    }

    @Override
    public Void visit(ContinueStmt continueStmt) {
        this.currentMethod.setDoesContinue(true);
        if(!this.getLoopBlock()) {  // Not within a loop block
            ContinueBreakNotInLoop continueBreakNotInLoop = new ContinueBreakNotInLoop(continueStmt.getLine(), 1);  // Error 9
            continueStmt.addError(continueBreakNotInLoop);
        }
        return null;
    }

    @Override
    public Void visit(ForeachStmt foreachStmt) {
        Type variableType = foreachStmt.getVariable().accept(this.expressionTypeChecker);
        Type listType = foreachStmt.getList().accept(this.expressionTypeChecker);
        if(!(listType instanceof ListType)) {
            if (!(listType instanceof NoType)) {
                ForeachCantIterateNoneList foreachCantIterateNoneList =
                        new ForeachCantIterateNoneList(foreachStmt.getLine()); // Error 19
                foreachStmt.addError(foreachCantIterateNoneList);
            }
        } else {
            if(!(this.expressionTypeChecker.isListSingleType((ListType) listType))) {
                ForeachListElementsNotSameType foreachListElementsNotSameType =
                        new ForeachListElementsNotSameType(foreachStmt.getLine()); // Error 20
                foreachStmt.addError(foreachListElementsNotSameType);
            }
            if(((ListType) listType).getElementsTypes().size() != 0) {
                if(!this.expressionTypeChecker.isSecondSubtypeOfFirst(variableType,
                        ((ListType) listType).getElementsTypes().get(0).getType())) {
                    ForeachVarNotMatchList foreachVarNotMatchList = new ForeachVarNotMatchList(foreachStmt);    // Error 21
                    foreachStmt.addError(foreachVarNotMatchList);
                }
            }
        }
        this.setLoopBlock(true);
        this.setHasUnreachableStmt(false);
        foreachStmt.getBody().accept(this);
        this.setLoopBlock(false);
        return null;
    }

    @Override
    public Void visit(ForStmt forStmt) {
        if(forStmt.getInitialize() != null) {
            forStmt.getInitialize().accept(this);
        }
        if(forStmt.getCondition() != null) {    // For has condition statement
            Type conditionType = forStmt.getCondition().accept(this.expressionTypeChecker);
            if(!(conditionType instanceof BoolType)) {  // Condition statement type is not boolean
                ConditionNotBool conditionNotBool = new ConditionNotBool(forStmt.getCondition().getLine()); // Error 5
                forStmt.addError(conditionNotBool);
            }
        }
        if(forStmt.getUpdate() != null) {
            forStmt.getUpdate().accept(this);
        }
        this.setLoopBlock(true);
        this.setHasUnreachableStmt(false);
        forStmt.getBody().accept(this);
        this.setLoopBlock(false);
        return null;
    }

    public void isTypeValid(Type varType, VarDeclaration varDeclaration) {
        if(varType instanceof ClassType) {
            ClassType varClass = (ClassType) varType;
            if(!(this.expressionTypeChecker.doesClassExist(varClass))) {
                ClassNotDeclared classNotDeclared =
                        new ClassNotDeclared(varDeclaration.getLine(), varClass.getClassName().getName());  // Error 2
                varDeclaration.addError(classNotDeclared);
                this.setDoesVarHaveError(true);
            }
        }
        if(varType instanceof ListType) {
            ListType list = (ListType) varType;
            if(list.getElementsTypes().size() != 0) {
                Set<String> listElements = new HashSet<>();
                for(ListNameType listNameType : list.getElementsTypes()) {
                    String listElementName = listNameType.getName().getName();
                    if (listElementName.equals("")) continue;
                    if(!listElements.contains(listElementName)) {
                        listElements.add(listElementName);
                    } else {
                        DuplicateListId duplicateListId = new DuplicateListId(varDeclaration.getLine());    // Error 18
                        varDeclaration.addError(duplicateListId);
                        this.setDoesVarHaveError(true);
                        break;
                    }
                }
                for(ListNameType listNameType : list.getElementsTypes()) {
                    this.isTypeValid(listNameType.getType(), varDeclaration);
                }
            } else {
                CannotHaveEmptyList cannotHaveEmptyList = new CannotHaveEmptyList(varDeclaration.getLine()); // Error 11
                varDeclaration.addError(cannotHaveEmptyList);
                this.setDoesVarHaveError(true);
            }
        }
        if(varType instanceof FptrType) {
            FptrType fptrType = (FptrType) varType;
            this.isTypeValid(fptrType.getReturnType(), varDeclaration);
            for(Type type : fptrType.getArgumentsTypes()) {
                this.isTypeValid(type, varDeclaration);
            }
        }
    }
}
