package main.compileErrorException.typeErrors;

import main.compileErrorException.CompileErrorException;

public class ConstructorNotSameNameAsClass extends CompileErrorException {

    public ConstructorNotSameNameAsClass(int line) {
        super(line, "Constructor name does not match with class name");
    }

}
