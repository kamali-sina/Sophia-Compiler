package main.compileErrorException.typeErrors;

import main.ast.nodes.statement.loop.ForeachStmt;
import main.compileErrorException.CompileErrorException;

public class ForeachVarNotMatchList extends CompileErrorException {

    //compare with first element
    public ForeachVarNotMatchList(ForeachStmt foreachStmt) {
        super(foreachStmt.getLine(), "Foreach variable " + foreachStmt.getVariable().getName() + " and list elements types do not match");
    }

}
