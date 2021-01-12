package main.compileErrorException.typeErrors;

import main.compileErrorException.CompileErrorException;

public class CantUseValueOfVoidMethod extends CompileErrorException {

    public CantUseValueOfVoidMethod(int line) {
        super(line, "Cannot use return value of a method having void return type");
    }

}
