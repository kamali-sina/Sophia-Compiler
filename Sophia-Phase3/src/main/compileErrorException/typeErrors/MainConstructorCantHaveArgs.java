package main.compileErrorException.typeErrors;

import main.compileErrorException.CompileErrorException;

public class MainConstructorCantHaveArgs extends CompileErrorException {

    public MainConstructorCantHaveArgs(int line) {
        super(line, "Main constructor cannot have arguments");
    }

}
