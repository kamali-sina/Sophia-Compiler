package main.ast.nodes.expression;

import main.visitor.IVisitor;

//line -> THIS
public class ThisClass extends Expression {
    @Override
    public String toString() {
        return "ThisClass";
    }

    @Override
    public <T> T accept(IVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
