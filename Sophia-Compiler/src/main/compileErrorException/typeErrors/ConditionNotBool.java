package main.compileErrorException.typeErrors;

import main.compileErrorException.CompileErrorException;

public class ConditionNotBool extends CompileErrorException {

    public ConditionNotBool(int line) {
        super(line, "Condition must be bool");
    }

}
