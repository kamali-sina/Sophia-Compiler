package main.compileErrorException.typeErrors;

import main.compileErrorException.CompileErrorException;

public class UnsupportedOperandType extends CompileErrorException {

    public UnsupportedOperandType(int line, String operatorName) {
        super(line, "Unsupported operand type for operator " + operatorName);
    }

}
