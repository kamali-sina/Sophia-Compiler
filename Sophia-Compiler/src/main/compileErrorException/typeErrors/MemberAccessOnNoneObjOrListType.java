package main.compileErrorException.typeErrors;

import main.compileErrorException.CompileErrorException;

public class MemberAccessOnNoneObjOrListType extends CompileErrorException {

    public MemberAccessOnNoneObjOrListType(int line) {
        super(line, "Object or list member access on an expression that is neither a list nor an object");
    }

}
