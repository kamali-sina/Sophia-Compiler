package main.compileErrorException.typeErrors;

import main.compileErrorException.CompileErrorException;

public class VarNotDeclared extends CompileErrorException {

    public VarNotDeclared(int line, String varName) {
        super(line, "Variable " + varName + " is not declared");
    }

}
