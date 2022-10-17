package main.ast.nodes.statement.loop;

import main.ast.nodes.statement.Statement;
import main.visitor.IVisitor;

//line -> BREAK
public class BreakStmt extends Statement {
    @Override
    public String toString() {
        return "BreakStmt";
    }

    @Override
    public <T> T accept(IVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
