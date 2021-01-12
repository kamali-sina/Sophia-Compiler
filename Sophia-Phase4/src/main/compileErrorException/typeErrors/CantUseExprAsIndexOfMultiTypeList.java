package main.compileErrorException.typeErrors;

import main.compileErrorException.CompileErrorException;

public class CantUseExprAsIndexOfMultiTypeList extends CompileErrorException {

    public CantUseExprAsIndexOfMultiTypeList(int line) {
        super(line, "Expression cannot be used as index of multi-type list");
    }

}
