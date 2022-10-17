package main.visitor;

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

public class Visitor<T> implements IVisitor<T> {

    @Override
    public T visit(Program program) {
        return null;
    }

    @Override
    public T visit(ClassDeclaration classDeclaration) {
        return null;
    }

    @Override
    public T visit(ConstructorDeclaration constructorDeclaration) {
        return null;
    }

    @Override
    public T visit(MethodDeclaration methodDeclaration) {
        return null;
    }

    @Override
    public T visit(FieldDeclaration fieldDeclaration) {
        return null;
    }

    @Override
    public T visit(VarDeclaration varDeclaration) {
        return null;
    }

    @Override
    public T visit(AssignmentStmt assignmentStmt) {
        return null;
    }

    @Override
    public T visit(BlockStmt blockStmt) {
        return null;
    }

    @Override
    public T visit(ConditionalStmt conditionalStmt) {
        return null;
    }

    @Override
    public T visit(MethodCallStmt methodCallStmt) {
        return null;
    }

    @Override
    public T visit(PrintStmt print) {
        return null;
    }

    @Override
    public T visit(ReturnStmt returnStmt) {
        return null;
    }

    @Override
    public T visit(BreakStmt breakStmt) {
        return null;
    }

    @Override
    public T visit(ContinueStmt continueStmt) {
        return null;
    }

    @Override
    public T visit(ForeachStmt foreachStmt) {
        return null;
    }

    @Override
    public T visit(ForStmt forStmt) {
        return null;
    }

    @Override
    public T visit(BinaryExpression binaryExpression) {
        return null;
    }

    @Override
    public T visit(UnaryExpression unaryExpression) {
        return null;
    }

    @Override
    public T visit(ObjectOrListMemberAccess objectOrListMemberAccess) {
        return null;
    }

    @Override
    public T visit(Identifier identifier) {
        return null;
    }

    @Override
    public T visit(ListAccessByIndex listAccessByIndex) {
        return null;
    }

    @Override
    public T visit(MethodCall methodCall) {
        return null;
    }

    @Override
    public T visit(NewClassInstance newClassInstance) {
        return null;
    }

    @Override
    public T visit(ThisClass thisClass) {
        return null;
    }

    @Override
    public T visit(ListValue listValue) {
        return null;
    }

    @Override
    public T visit(NullValue nullValue) {
        return null;
    }

    @Override
    public T visit(IntValue intValue) {
        return null;
    }

    @Override
    public T visit(BoolValue boolValue) {
        return null;
    }

    @Override
    public T visit(StringValue stringValue) {
        return null;
    }

}
