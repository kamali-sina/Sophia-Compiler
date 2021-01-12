package main.compileErrorException.typeErrors;

import main.compileErrorException.CompileErrorException;

public class CannotHaveEmptyList extends CompileErrorException {

    public CannotHaveEmptyList(int line) {
        super(line, "Cannot have empty list");
    }

}
