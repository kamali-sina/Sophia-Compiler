package main.ast.nodes.statement.loop;

import main.ast.nodes.expression.Expression;
import main.ast.nodes.statement.AssignmentStmt;
import main.ast.nodes.statement.Statement;
import main.visitor.IVisitor;

//line -> FOR
public class ForStmt extends Statement {
    private AssignmentStmt initialize;
    private Expression condition;
    private AssignmentStmt update;
    private Statement body;

    public ForStmt() {
    }

    public AssignmentStmt getInitialize() {
        return initialize;
    }

    public void setInitialize(AssignmentStmt initialize) {
        this.initialize = initialize;
    }

    public Expression getCondition() {
        return condition;
    }

    public void setCondition(Expression condition) {
        this.condition = condition;
    }

    public AssignmentStmt getUpdate() {
        return update;
    }

    public void setUpdate(AssignmentStmt update) {
        this.update = update;
    }

    public Statement getBody() {
        return body;
    }

    public void setBody(Statement body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "ForStmt";
    }

    @Override
    public <T> T accept(IVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
