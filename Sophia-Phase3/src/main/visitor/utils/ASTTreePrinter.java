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
import main.visitor.Visitor;

public class ASTTreePrinter extends Visitor<Void> {

    private void print(Node node) {
        System.out.println("Line:" + node.getLine() + ":" + node.toString());
    }

    @Override
    public Void visit(Program program) {
        print(program);
        for(ClassDeclaration classDeclaration : program.getClasses()) {
            classDeclaration.accept(this);
        }
        return null;
    }

    @Override
    public Void visit(ClassDeclaration classDeclaration) {
        print(classDeclaration);
        classDeclaration.getClassName().accept(this);
        if(classDeclaration.getParentClassName() != null) {
            classDeclaration.getParentClassName().accept(this);
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
        print(constructorDeclaration);
        constructorDeclaration.getMethodName().accept(this);
        for(VarDeclaration varDeclaration : constructorDeclaration.getArgs()) {
            varDeclaration.accept(this);
        }
        for(VarDeclaration varDeclaration : constructorDeclaration.getLocalVars()) {
            varDeclaration.accept(this);
        }
        for(Statement statement : constructorDeclaration.getBody()) {
            statement.accept(this);
        }
        return null;
    }

    @Override
    public Void visit(MethodDeclaration methodDeclaration) {
        print(methodDeclaration);
        methodDeclaration.getMethodName().accept(this);
        for(VarDeclaration varDeclaration : methodDeclaration.getArgs()) {
            varDeclaration.accept(this);
        }
        for(VarDeclaration varDeclaration : methodDeclaration.getLocalVars()) {
            varDeclaration.accept(this);
        }
        for(Statement statement : methodDeclaration.getBody()) {
            statement.accept(this);
        }
        return null;
    }

    @Override
    public Void visit(FieldDeclaration fieldDeclaration) {
        print(fieldDeclaration);
        fieldDeclaration.getVarDeclaration().accept(this);
        return null;
    }

    @Override
    public Void visit(VarDeclaration varDeclaration) {
        print(varDeclaration);
        varDeclaration.getVarName().accept(this);
        return null;
    }

    @Override
    public Void visit(AssignmentStmt assignmentStmt) {
        print(assignmentStmt);
        assignmentStmt.getlValue().accept(this);
        assignmentStmt.getrValue().accept(this);
        return null;
    }

    @Override
    public Void visit(BlockStmt blockStmt) {
        print(blockStmt);
        for(Statement statement : blockStmt.getStatements()) {
            statement.accept(this);
        }
        return null;
    }

    @Override
    public Void visit(ConditionalStmt conditionalStmt) {
        print(conditionalStmt);
        conditionalStmt.getCondition().accept(this);
        conditionalStmt.getThenBody().accept(this);
        if(conditionalStmt.getElseBody() != null) {
            conditionalStmt.getElseBody().accept(this);
        }
        return null;
    }

    @Override
    public Void visit(MethodCallStmt methodCallStmt) {
        print(methodCallStmt);
        methodCallStmt.getMethodCall().accept(this);
        return null;
    }

    @Override
    public Void visit(PrintStmt print) {
        print(print);
        print.getArg().accept(this);
        return null;
    }

    @Override
    public Void visit(ReturnStmt returnStmt) {
        print(returnStmt);
        returnStmt.getReturnedExpr().accept(this);
        return null;
    }

    @Override
    public Void visit(BreakStmt breakStmt) {
        print(breakStmt);
        return null;
    }

    @Override
    public Void visit(ContinueStmt continueStmt) {
        print(continueStmt);
        return null;
    }

    @Override
    public Void visit(ForeachStmt foreachStmt) {
        print(foreachStmt);
        foreachStmt.getVariable().accept(this);
        foreachStmt.getList().accept(this);
        foreachStmt.getBody().accept(this);
        return null;
    }

    @Override
    public Void visit(ForStmt forStmt) {
        print(forStmt);
        if(forStmt.getInitialize() != null) {
            forStmt.getInitialize().accept(this);
        }
        if(forStmt.getCondition() != null) {
            forStmt.getCondition().accept(this);
        }
        if(forStmt.getUpdate() != null) {
            forStmt.getUpdate().accept(this);
        }
        forStmt.getBody().accept(this);
        return null;
    }

    @Override
    public Void visit(BinaryExpression binaryExpression) {
        print(binaryExpression);
        binaryExpression.getFirstOperand().accept(this);
        binaryExpression.getSecondOperand().accept(this);
        return null;
    }

    @Override
    public Void visit(UnaryExpression unaryExpression) {
        print(unaryExpression);
        unaryExpression.getOperand().accept(this);
        return null;
    }

    @Override
    public Void visit(ObjectOrListMemberAccess objectOrListMemberAccess) {
        print(objectOrListMemberAccess);
        objectOrListMemberAccess.getInstance().accept(this);
        objectOrListMemberAccess.getMemberName().accept(this);
        return null;
    }

    @Override
    public Void visit(Identifier identifier) {
        print(identifier);
        return null;
    }

    @Override
    public Void visit(ListAccessByIndex listAccessByIndex) {
        print(listAccessByIndex);
        listAccessByIndex.getInstance().accept(this);
        listAccessByIndex.getIndex().accept(this);
        return null;
    }

    @Override
    public Void visit(MethodCall methodCall) {
        print(methodCall);
        methodCall.getInstance().accept(this);
        for(Expression expression : methodCall.getArgs()) {
            expression.accept(this);
        }
        return null;
    }

    @Override
    public Void visit(NewClassInstance newClassInstance) {
        print(newClassInstance);
        for(Expression expression : newClassInstance.getArgs()) {
            expression.accept(this);
        }
        return null;
    }

    @Override
    public Void visit(ThisClass thisClass) {
        print(thisClass);
        return null;
    }

    @Override
    public Void visit(ListValue listValue) {
        print(listValue);
        for(Expression expression : listValue.getElements()) {
            expression.accept(this);
        }
        return null;
    }

    @Override
    public Void visit(NullValue nullValue) {
        print(nullValue);
        return null;
    }

    @Override
    public Void visit(IntValue intValue) {
        print(intValue);
        return null;
    }

    @Override
    public Void visit(BoolValue boolValue) {
        print(boolValue);
        return null;
    }

    @Override
    public Void visit(StringValue stringValue) {
        print(stringValue);
        return null;
    }

}
