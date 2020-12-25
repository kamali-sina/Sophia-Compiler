package main.compileErrorException.typeErrors;

import main.compileErrorException.CompileErrorException;

public class IncDecOperandNotLvalue extends CompileErrorException {

    public IncDecOperandNotLvalue(int line, String operatorName) {
        super(line, "Lvalue required as " + operatorName + " operand");
    }

}
