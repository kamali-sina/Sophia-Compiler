package main.compileErrorException.typeErrors;

import main.compileErrorException.CompileErrorException;

public class ListMemberNotFound extends CompileErrorException {

    public ListMemberNotFound(int line, String name) {
        super(line, "There is no element named " + name + " in the list");
    }

}
