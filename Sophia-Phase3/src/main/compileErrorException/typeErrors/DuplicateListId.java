package main.compileErrorException.typeErrors;

import main.compileErrorException.CompileErrorException;

public class DuplicateListId extends CompileErrorException {

    public DuplicateListId(int line) {
        super(line, "List elements cannot have the same id");
    }

}
