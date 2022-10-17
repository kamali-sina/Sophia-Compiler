package main.compileErrorException.typeErrors;

import main.compileErrorException.CompileErrorException;

public class NoMainClass extends CompileErrorException {

    public NoMainClass() {
        super(1, "Main class is not found");
    }

}
