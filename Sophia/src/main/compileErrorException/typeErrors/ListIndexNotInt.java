package main.compileErrorException.typeErrors;

import main.compileErrorException.CompileErrorException;

public class ListIndexNotInt extends CompileErrorException {

    public ListIndexNotInt(int line) {
        super(line, "List index is not an integer");
    }

}
