package main.compileErrorException.typeErrors;

import main.compileErrorException.CompileErrorException;

public class CannotExtendFromMainClass extends CompileErrorException {

    public CannotExtendFromMainClass(int line) {
        super(line, "Classes cannot extend from Main class");
    }

}
