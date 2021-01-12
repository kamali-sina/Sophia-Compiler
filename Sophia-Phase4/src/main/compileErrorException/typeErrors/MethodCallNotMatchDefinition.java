package main.compileErrorException.typeErrors;

import main.compileErrorException.CompileErrorException;

public class MethodCallNotMatchDefinition extends CompileErrorException {

    public MethodCallNotMatchDefinition(int line) {
        super(line, "Args in method call do not match with definition");
    }

}
