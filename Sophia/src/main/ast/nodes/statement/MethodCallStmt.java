package main.ast.nodes.statement;

import main.ast.nodes.expression.MethodCall;
import main.visitor.IVisitor;

//line -> LPAR before arguments
public class MethodCallStmt extends Statement{
    private MethodCall methodCall;

    public MethodCallStmt(MethodCall methodCall) {
        this.methodCall = methodCall;
    }

    public MethodCall getMethodCall() {
        return methodCall;
    }

    public void setMethodCall(MethodCall methodCall) {
        this.methodCall = methodCall;
    }

    @Override
    public String toString() {
        return "MethodCallStmt";
    }

    @Override
    public <T> T accept(IVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
