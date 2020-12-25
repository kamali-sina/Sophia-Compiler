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

public interface IVisitor<T> {

    T visit(Program program);

    T visit(ClassDeclaration classDeclaration);
    T visit(ConstructorDeclaration constructorDeclaration);
    T visit(MethodDeclaration methodDeclaration);
    T visit(FieldDeclaration fieldDeclaration);
    T visit(VarDeclaration varDeclaration);

    T visit(AssignmentStmt assignmentStmt);
    T visit(BlockStmt blockStmt);
    T visit(ConditionalStmt conditionalStmt);
    T visit(MethodCallStmt methodCallStmt);
    T visit(PrintStmt print);
    T visit(ReturnStmt returnStmt);
    T visit(BreakStmt breakStmt);
    T visit(ContinueStmt continueStmt);
    T visit(ForeachStmt foreachStmt);
    T visit(ForStmt forStmt);

    T visit(BinaryExpression binaryExpression);
    T visit(UnaryExpression unaryExpression);
    T visit(ObjectOrListMemberAccess objectOrListMemberAccess);
    T visit(Identifier identifier);
    T visit(ListAccessByIndex listAccessByIndex);
    T visit(MethodCall methodCall);
    T visit(NewClassInstance newClassInstance);
    T visit(ThisClass thisClass);
    T visit(ListValue listValue);
    T visit(NullValue nullValue);
    T visit(IntValue intValue);
    T visit(BoolValue boolValue);
    T visit(StringValue stringValue);

}
