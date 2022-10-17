package main.visitor.utils;

import main.ast.nodes.Node;
import main.ast.nodes.Program;
import main.ast.nodes.declaration.classDec.ClassDeclaration;
import main.ast.nodes.declaration.classDec.classMembersDec.ConstructorDeclaration;
import main.ast.nodes.declaration.classDec.classMembersDec.FieldDeclaration;
import main.ast.nodes.declaration.classDec.classMembersDec.MethodDeclaration;
import main.ast.nodes.declaration.variableDec.VarDeclaration;
import main.ast.nodes.expression.*;
import main.ast.nodes.expression.values.ListValue;
import main.ast.nodes.expression.values.NullValue;
import main.ast.nodes.expression.values.primitive.BoolValue;
import main.ast.nodes.expression.values.primitive.IntValue;
import main.ast.nodes.expression.values.primitive.StringValue;
import main.ast.nodes.statement.*;
import main.ast.nodes.statement.loop.BreakStmt;
import main.ast.nodes.statement.loop.ContinueStmt;
import main.ast.nodes.statement.loop.ForStmt;
import main.ast.nodes.statement.loop.ForeachStmt;
import main.compileErrorException.CompileErrorException;
import main.visitor.Visitor;

import java.util.ArrayList;

public class ErrorReporter extends Visitor<Integer> {

    private int printErrors(Node node) {
        ArrayList<CompileErrorException> errors = node.flushErrors();
        for(CompileErrorException compileErrorException : errors) {
            System.out.println(compileErrorException.getMessage());
        }
        return errors.size();
    }

    @Override
    public Integer visit(Program program) {
        int numOfErrors = printErrors(program);
        for(ClassDeclaration classDeclaration : program.getClasses()) {
            numOfErrors += classDeclaration.accept(this);
        }
        return numOfErrors;
    }

    @Override
    public Integer visit(ClassDeclaration classDeclaration) {
        int numOfErrors = printErrors(classDeclaration);
        numOfErrors += classDeclaration.getClassName().accept(this);
        if(classDeclaration.getParentClassName() != null) {
            numOfErrors += classDeclaration.getParentClassName().accept(this);
        }
        for(FieldDeclaration fieldDeclaration : classDeclaration.getFields()) {
            numOfErrors += fieldDeclaration.accept(this);
        }
        if(classDeclaration.getConstructor() != null) {
            numOfErrors += classDeclaration.getConstructor().accept(this);
        }
        for(MethodDeclaration methodDeclaration : classDeclaration.getMethods()) {
            numOfErrors += methodDeclaration.accept(this);
        }
        return numOfErrors;
    }

    @Override
    public Integer visit(ConstructorDeclaration constructorDeclaration) {
        return this.visit((MethodDeclaration) constructorDeclaration);
    }

    @Override
    public Integer visit(MethodDeclaration methodDeclaration) {
        int numOfErrors = printErrors(methodDeclaration);
        numOfErrors += methodDeclaration.getMethodName().accept(this);
        for(VarDeclaration varDeclaration : methodDeclaration.getArgs()) {
            numOfErrors += varDeclaration.accept(this);
        }
        for(VarDeclaration varDeclaration : methodDeclaration.getLocalVars()) {
            numOfErrors += varDeclaration.accept(this);
        }
        for(Statement statement : methodDeclaration.getBody()) {
            numOfErrors += statement.accept(this);
        }
        return numOfErrors;
    }

    @Override
    public Integer visit(FieldDeclaration fieldDeclaration) {
        int numOfErrors = printErrors(fieldDeclaration);
        numOfErrors += fieldDeclaration.getVarDeclaration().accept(this);
        return numOfErrors;
    }

    @Override
    public Integer visit(VarDeclaration varDeclaration) {
        int numOfErrors = printErrors(varDeclaration);
        numOfErrors += varDeclaration.getVarName().accept(this);
        return numOfErrors;
    }

    @Override
    public Integer visit(AssignmentStmt assignmentStmt) {
        int numOfErrors = printErrors(assignmentStmt);
        numOfErrors += assignmentStmt.getlValue().accept(this);
        numOfErrors += assignmentStmt.getrValue().accept(this);
        return numOfErrors;
    }

    @Override
    public Integer visit(BlockStmt blockStmt) {
        int numOfErrors = printErrors(blockStmt);
        for(Statement statement : blockStmt.getStatements()) {
            numOfErrors += statement.accept(this);
        }
        return numOfErrors;
    }

    @Override
    public Integer visit(ConditionalStmt conditionalStmt) {
        int numOfErrors = printErrors(conditionalStmt);
        numOfErrors += conditionalStmt.getCondition().accept(this);
        numOfErrors += conditionalStmt.getThenBody().accept(this);
        if(conditionalStmt.getElseBody() != null) {
            numOfErrors += conditionalStmt.getElseBody().accept(this);
        }
        return numOfErrors;
    }

    @Override
    public Integer visit(MethodCallStmt methodCallStmt) {
        int numOfErrors = printErrors(methodCallStmt);
        numOfErrors += methodCallStmt.getMethodCall().accept(this);
        return numOfErrors;
    }

    @Override
    public Integer visit(PrintStmt printErrors) {
        int numOfErrors = printErrors(printErrors);
        numOfErrors += printErrors.getArg().accept(this);
        return numOfErrors;
    }

    @Override
    public Integer visit(ReturnStmt returnStmt) {
        int numOfErrors = printErrors(returnStmt);
        numOfErrors += returnStmt.getReturnedExpr().accept(this);
        return numOfErrors;
    }

    @Override
    public Integer visit(BreakStmt breakStmt) {
        return printErrors(breakStmt);
    }

    @Override
    public Integer visit(ContinueStmt continueStmt) {
        return printErrors(continueStmt);
    }

    @Override
    public Integer visit(ForeachStmt foreachStmt) {
        int numOfErrors = printErrors(foreachStmt);
        numOfErrors += foreachStmt.getVariable().accept(this);
        numOfErrors += foreachStmt.getList().accept(this);
        numOfErrors += foreachStmt.getBody().accept(this);
        return numOfErrors;
    }

    @Override
    public Integer visit(ForStmt forStmt) {
        int numOfErrors = printErrors(forStmt);
        if(forStmt.getInitialize() != null) {
            numOfErrors += forStmt.getInitialize().accept(this);
        }
        if(forStmt.getCondition() != null) {
            numOfErrors += forStmt.getCondition().accept(this);
        }
        if(forStmt.getUpdate() != null) {
            numOfErrors += forStmt.getUpdate().accept(this);
        }
        numOfErrors += forStmt.getBody().accept(this);
        return numOfErrors;
    }

    @Override
    public Integer visit(BinaryExpression binaryExpression) {
        int numOfErrors = printErrors(binaryExpression);
        numOfErrors += binaryExpression.getFirstOperand().accept(this);
        numOfErrors += binaryExpression.getSecondOperand().accept(this);
        return numOfErrors;
    }

    @Override
    public Integer visit(UnaryExpression unaryExpression) {
        int numOfErrors = printErrors(unaryExpression);
        numOfErrors += unaryExpression.getOperand().accept(this);
        return numOfErrors;
    }

    @Override
    public Integer visit(ObjectOrListMemberAccess objectOrListMemberAccess) {
        int numOfErrors = printErrors(objectOrListMemberAccess);
        numOfErrors += objectOrListMemberAccess.getInstance().accept(this);
        numOfErrors += objectOrListMemberAccess.getMemberName().accept(this);
        return numOfErrors;
    }

    @Override
    public Integer visit(Identifier identifier) {
        return printErrors(identifier);
    }

    @Override
    public Integer visit(ListAccessByIndex listAccessByIndex) {
        int numOfErrors = printErrors(listAccessByIndex);
        numOfErrors += listAccessByIndex.getInstance().accept(this);
        numOfErrors += listAccessByIndex.getIndex().accept(this);
        return numOfErrors;
    }

    @Override
    public Integer visit(MethodCall methodCall) {
        int numOfErrors = printErrors(methodCall);
        numOfErrors += methodCall.getInstance().accept(this);
        for(Expression expression : methodCall.getArgs()) {
            numOfErrors += expression.accept(this);
        }
        return numOfErrors;
    }

    @Override
    public Integer visit(NewClassInstance newClassInstance) {
        int numOfErrors = printErrors(newClassInstance);
        for(Expression expression : newClassInstance.getArgs()) {
            numOfErrors += expression.accept(this);
        }
        return numOfErrors;
    }

    @Override
    public Integer visit(ThisClass thisClass) {
        return printErrors(thisClass);
    }

    @Override
    public Integer visit(ListValue listValue) {
        int numOfErrors = printErrors(listValue);
        for(Expression expression : listValue.getElements()) {
            numOfErrors += expression.accept(this);
        }
        return numOfErrors;
    }

    @Override
    public Integer visit(NullValue nullValue) {
        return printErrors(nullValue);
    }

    @Override
    public Integer visit(IntValue intValue) {
        return printErrors(intValue);
    }

    @Override
    public Integer visit(BoolValue boolValue) {
        return printErrors(boolValue);
    }

    @Override
    public Integer visit(StringValue stringValue) {
        return printErrors(stringValue);
    }

}
