package main.ast.nodes.expression.values;

import main.visitor.IVisitor;

//line -> NULL
public class NullValue extends Value{

    @Override
    public String toString() {
        return "NullValue";
    }

    @Override
    public <T> T accept(IVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
