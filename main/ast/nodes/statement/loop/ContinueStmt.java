package main.ast.nodes.statement.loop;

import main.ast.nodes.statement.Statement;
import main.visitor.IVisitor;

//line -> CONTINUE
public class ContinueStmt extends Statement {
    @Override
    public String toString() {
        return "ContinueStmt";
    }

    @Override
    public <T> T accept(IVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
