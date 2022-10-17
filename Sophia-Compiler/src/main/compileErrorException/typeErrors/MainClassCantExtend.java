package main.compileErrorException.typeErrors;

import main.compileErrorException.CompileErrorException;

public class MainClassCantExtend extends CompileErrorException {

    public MainClassCantExtend(int line) {
        super(line, "Main class cannot extend from any class");
    }

}
