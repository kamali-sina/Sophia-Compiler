package main.visitor.typeChecker;

import main.ast.nodes.Node;
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
import main.compileErrorException.typeErrors.*;
import main.symbolTable.utils.graph.Graph;
import main.visitor.Visitor;

import java.util.ArrayList;

public class TypeChecker extends Visitor<RetConBrk> {
    private final Graph<String> classHierarchy;
    private final ExpressionTypeChecker expressionTypeChecker;
    private ClassDeclaration currentClass;
    private MethodDeclaration currentMethod;
    private boolean isInFor = false;

    public TypeChecker(Graph<String> classHierarchy) {
        this.classHierarchy = classHierarchy;
        this.expressionTypeChecker = new ExpressionTypeChecker(classHierarchy);
    }

    @Override
    public RetConBrk visit(Program program) {
        boolean mainCheck = false;
        for(ClassDeclaration classDeclaration : program.getClasses()) {
            this.expressionTypeChecker.setCurrentClass(classDeclaration);
            this.currentClass = classDeclaration;
            classDeclaration.accept(this);
            if(classDeclaration.getClassName().getName().equals("Main"))
                mainCheck = true;
        }
        if(!mainCheck) {
            NoMainClass exception = new NoMainClass();
            program.addError(exception);
        }
        return null;
    }

    @Override
    public RetConBrk visit(ClassDeclaration classDeclaration) {
        if(classDeclaration.getParentClassName() != null) {
            this.expressionTypeChecker.checkTypeValidation(new ClassType(classDeclaration.getParentClassName()), classDeclaration);
            if(classDeclaration.getClassName().getName().equals("Main")) {
                MainClassCantExtend exception = new MainClassCantExtend(classDeclaration.getLine());
                classDeclaration.addError(exception);
            }
            if(classDeclaration.getParentClassName().getName().equals("Main")) {
                CannotExtendFromMainClass exception = new CannotExtendFromMainClass(classDeclaration.getLine());
                classDeclaration.addError(exception);
            }
        }
        for(FieldDeclaration fieldDeclaration : classDeclaration.getFields()) {
            fieldDeclaration.accept(this);
        }
        if(classDeclaration.getConstructor() != null) {
            this.expressionTypeChecker.setCurrentMethod(classDeclaration.getConstructor());
            this.currentMethod = classDeclaration.getConstructor();
            classDeclaration.getConstructor().accept(this);
        }
        else if(classDeclaration.getClassName().getName().equals("Main")) {
            NoConstructorInMainClass exception = new NoConstructorInMainClass(classDeclaration);
            classDeclaration.addError(exception);
        }
        for(MethodDeclaration methodDeclaration : classDeclaration.getMethods()) {
            this.expressionTypeChecker.setCurrentMethod(methodDeclaration);
            this.currentMethod = methodDeclaration;
            boolean doesReturn = methodDeclaration.accept(this).doesReturn;
            methodDeclaration.setDoesReturn(doesReturn);
            if(!doesReturn && !(methodDeclaration.getReturnType() instanceof NullType)) {
                MissingReturnStatement exception = new MissingReturnStatement(methodDeclaration);
                methodDeclaration.addError(exception);
            }
        }
        return null;
    }

    @Override
    public RetConBrk visit(ConstructorDeclaration constructorDeclaration) {
        if(!this.currentClass.getClassName().getName().equals(constructorDeclaration.getMethodName().getName())) {
            ConstructorNotSameNameAsClass exception = new ConstructorNotSameNameAsClass(constructorDeclaration.getLine());
            constructorDeclaration.addError(exception);
        }
        if(this.currentClass.getClassName().getName().equals("Main")) {
            if(constructorDeclaration.getArgs().size() != 0) {
                MainConstructorCantHaveArgs exception = new MainConstructorCantHaveArgs(constructorDeclaration.getLine());
                constructorDeclaration.addError(exception);
            }
        }
        return this.visit((MethodDeclaration) constructorDeclaration);
    }

    @Override
    public RetConBrk visit(MethodDeclaration methodDeclaration) {
        this.expressionTypeChecker.checkTypeValidation(methodDeclaration.getReturnType(), methodDeclaration);
        for(VarDeclaration varDeclaration : methodDeclaration.getArgs()) {
            varDeclaration.accept(this);
        }
        for(VarDeclaration varDeclaration : methodDeclaration.getLocalVars()) {
            varDeclaration.accept(this);
        }
        boolean doesReturn = false, doesMethodReturn = false;
        for(Statement statement : methodDeclaration.getBody()) {
            if(doesReturn) {
                UnreachableStatements exception = new UnreachableStatements(statement);
                statement.addError(exception);
            }
            doesReturn = statement.accept(this).doesReturn;
            doesMethodReturn = doesReturn || doesMethodReturn;
        }
        return new RetConBrk(doesMethodReturn, false);
    }

    @Override
    public RetConBrk visit(FieldDeclaration fieldDeclaration) {
        fieldDeclaration.getVarDeclaration().accept(this);
        return null;
    }

    @Override
    public RetConBrk visit(VarDeclaration varDeclaration) {
        this.expressionTypeChecker.checkTypeValidation(varDeclaration.getType(), varDeclaration);
        return null;
    }

    @Override
    public RetConBrk visit(AssignmentStmt assignmentStmt) {
        Type firstType = assignmentStmt.getlValue().accept(expressionTypeChecker);
        Type secondType = assignmentStmt.getrValue().accept(expressionTypeChecker);
        boolean isFirstLvalue = expressionTypeChecker.isLvalue(assignmentStmt.getlValue());
        if(!isFirstLvalue) {
            LeftSideNotLvalue exception = new LeftSideNotLvalue(assignmentStmt.getLine());
            assignmentStmt.addError(exception);
        }
        if(firstType instanceof NoType || secondType instanceof NoType) {
            return new RetConBrk(false, false);
        }
        boolean isSubtype = expressionTypeChecker.isFirstSubTypeOfSecond(secondType, firstType);
        if(!isSubtype) {
            UnsupportedOperandType exception = new UnsupportedOperandType(assignmentStmt.getLine(), BinaryOperator.assign.name());
            assignmentStmt.addError(exception);
            return new RetConBrk(false, false);
        }
        return new RetConBrk(false, false);
    }

    @Override
    public RetConBrk visit(BlockStmt blockStmt) {
        boolean doesReturn = false, doesBlockReturn = false;
        boolean doesContinueBreak = false, doesBlockContinueBreak = false;
        for(Statement statement : blockStmt.getStatements()) {
            if(doesReturn) {
                UnreachableStatements exception = new UnreachableStatements(statement);
                statement.addError(exception);
            }
            if(isInFor && doesContinueBreak) {
                UnreachableStatements exception = new UnreachableStatements(statement);
                statement.addError(exception);
            }
            RetConBrk stmtRetConBrk = statement.accept(this);
            doesReturn = stmtRetConBrk.doesReturn;
            doesBlockReturn = doesReturn || doesBlockReturn;
            doesContinueBreak = stmtRetConBrk.doesBreakContinue;
            doesBlockContinueBreak = doesContinueBreak || doesBlockContinueBreak;
        }
        return new RetConBrk(doesBlockReturn, doesBlockContinueBreak);
    }

    @Override
    public RetConBrk visit(ConditionalStmt conditionalStmt) {
        Type condType = conditionalStmt.getCondition().accept(expressionTypeChecker);
        if(!(condType instanceof BoolType || condType instanceof NoType)) {
            ConditionNotBool exception = new ConditionNotBool(conditionalStmt.getLine());
            conditionalStmt.addError(exception);
        }
        RetConBrk thenRetConBrk = conditionalStmt.getThenBody().accept(this);
        if(conditionalStmt.getElseBody() != null) {
            RetConBrk elseRetConBrk = conditionalStmt.getElseBody().accept(this);
            return new RetConBrk(thenRetConBrk.doesReturn && elseRetConBrk.doesReturn,
                    thenRetConBrk.doesBreakContinue && elseRetConBrk.doesBreakContinue);
        }
        return new RetConBrk(false, false);
    }

    @Override
    public RetConBrk visit(MethodCallStmt methodCallStmt) {
        expressionTypeChecker.setIsInMethodCallStmt(true);
        methodCallStmt.getMethodCall().accept(expressionTypeChecker);
        expressionTypeChecker.setIsInMethodCallStmt(false);
        return new RetConBrk(false, false);
    }

    @Override
    public RetConBrk visit(PrintStmt print) {
        Type argType = print.getArg().accept(expressionTypeChecker);
        if(!(argType instanceof IntType || argType instanceof StringType ||
                argType instanceof BoolType || argType instanceof NoType)) {
            UnsupportedTypeForPrint exception = new UnsupportedTypeForPrint(print.getLine());
            print.addError(exception);
        }
        return new RetConBrk(false, false);
    }

    @Override
    public RetConBrk visit(ReturnStmt returnStmt) {
        Type retType = returnStmt.getReturnedExpr().accept(expressionTypeChecker);
        Type actualRetType = this.currentMethod.getReturnType();
        if(!expressionTypeChecker.isFirstSubTypeOfSecond(retType, actualRetType)) {
            ReturnValueNotMatchMethodReturnType exception = new ReturnValueNotMatchMethodReturnType(returnStmt);
            returnStmt.addError(exception);
        }
        return new RetConBrk(true, false);
    }

    @Override
    public RetConBrk visit(BreakStmt breakStmt) {
        if(!isInFor) {
            ContinueBreakNotInLoop exception = new ContinueBreakNotInLoop(breakStmt.getLine(), 0);
            breakStmt.addError(exception);
        }
        return new RetConBrk(false, true);
    }

    @Override
    public RetConBrk visit(ContinueStmt continueStmt) {
        if(!isInFor) {
            ContinueBreakNotInLoop exception = new ContinueBreakNotInLoop(continueStmt.getLine(), 1);
            continueStmt.addError(exception);
        }
        return new RetConBrk(false, true);
    }

    @Override
    public RetConBrk visit(ForeachStmt foreachStmt) {
        Type varType = foreachStmt.getVariable().accept(expressionTypeChecker);
        Type listType = foreachStmt.getList().accept(expressionTypeChecker);
        if(!(listType instanceof ListType || listType instanceof NoType)) {
            ForeachCantIterateNoneList exception = new ForeachCantIterateNoneList(foreachStmt.getLine());
            foreachStmt.addError(exception);
        }
        else if(!(listType instanceof NoType)) {
            ArrayList<Type> types = new ArrayList<>();
            for(ListNameType listNameType : ((ListType) listType).getElementsTypes())
                types.add(listNameType.getType());
            if(!expressionTypeChecker.areAllSameType(types)) {
                ForeachListElementsNotSameType exception = new ForeachListElementsNotSameType(foreachStmt.getLine());
                foreachStmt.addError(exception);
            }
            if((types.size() > 0) && !expressionTypeChecker.isSameType(varType, types.get(0))) {
                ForeachVarNotMatchList exception = new ForeachVarNotMatchList(foreachStmt);
                foreachStmt.addError(exception);
            }
        }
        boolean lastIsInFor = this.isInFor;
        this.isInFor = true;
        foreachStmt.getBody().accept(this);
        this.isInFor = lastIsInFor;
        return new RetConBrk(false, false);
    }

    @Override
    public RetConBrk visit(ForStmt forStmt) {
        if(forStmt.getInitialize() != null) {
            forStmt.getInitialize().accept(this);
        }
        if(forStmt.getCondition() != null) {
            Type type = forStmt.getCondition().accept(expressionTypeChecker);
            if(!(type instanceof BoolType || type instanceof NoType)) {
                ConditionNotBool exception = new ConditionNotBool(forStmt.getLine());
                forStmt.addError(exception);
            }
        }
        if(forStmt.getUpdate() != null) {
            forStmt.getUpdate().accept(this);
        }
        boolean lastIsInFor = this.isInFor;
        this.isInFor = true;
        forStmt.getBody().accept(this);
        this.isInFor = lastIsInFor;
        return new RetConBrk(false, false);
    }

}
