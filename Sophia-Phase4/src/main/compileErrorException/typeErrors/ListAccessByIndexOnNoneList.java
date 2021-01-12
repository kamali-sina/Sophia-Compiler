package main.compileErrorException.typeErrors;

import main.compileErrorException.CompileErrorException;

public class ListAccessByIndexOnNoneList extends CompileErrorException {

    public ListAccessByIndexOnNoneList(int line) {
        super(line, "Access by index on a non-list");
    }

}
