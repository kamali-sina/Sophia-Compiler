package main.compileErrorException.typeErrors;

import main.compileErrorException.CompileErrorException;

public class LeftSideNotLvalue extends CompileErrorException {

    public LeftSideNotLvalue(int line) {
        super(line, "Left side of assignment must be a valid lvalue");
    }

}
