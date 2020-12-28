package main.visitor.typeChecker;

import main.ast.nodes.expression.*;
import main.ast.nodes.expression.values.ListValue;
import main.ast.nodes.expression.values.NullValue;
import main.ast.nodes.expression.values.primitive.BoolValue;
import main.ast.nodes.expression.values.primitive.IntValue;
import main.ast.nodes.expression.values.primitive.StringValue;
import main.ast.types.NoType;
import main.ast.types.Type;
import main.visitor.Visitor;



public class LValueTypeChecker extends Visitor<Type> {
    private Boolean isLValue = true;

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

}