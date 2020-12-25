package main.compileErrorException.typeErrors;

import main.ast.nodes.statement.Statement;
import main.compileErrorException.CompileErrorException;

public class UnreachableStatements extends CompileErrorException {

    public UnreachableStatements(Statement statement) {
        super(statement.getLine(), "Statements are unreachable");
    }

}
