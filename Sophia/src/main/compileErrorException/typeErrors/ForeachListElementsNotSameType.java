package main.compileErrorException.typeErrors;

import main.compileErrorException.CompileErrorException;

public class ForeachListElementsNotSameType extends CompileErrorException {

    public ForeachListElementsNotSameType(int line) {
        super(line, "Foreach list elements do not have the same type");
    }

}
