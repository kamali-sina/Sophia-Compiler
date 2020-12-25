package main.compileErrorException.typeErrors;

import main.compileErrorException.CompileErrorException;

public class MemberNotAvailableInClass extends CompileErrorException {

    public MemberNotAvailableInClass(int line, String memberName, String className) {
        super(line, "There is no member named " + memberName + " in class " + className);
    }

}
