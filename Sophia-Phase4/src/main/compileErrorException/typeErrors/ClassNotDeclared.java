package main.compileErrorException.typeErrors;

import main.compileErrorException.CompileErrorException;

public class ClassNotDeclared extends CompileErrorException {

    public ClassNotDeclared(int line, String className) {
        super(line, "Class " + className + " is not declared");
    }

}
