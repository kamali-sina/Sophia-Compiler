package main.compileErrorException.nameErrors;

import main.ast.nodes.declaration.classDec.classMembersDec.MethodDeclaration;
import main.compileErrorException.CompileErrorException;

public class MethodNameConflictWithField extends CompileErrorException {

    public MethodNameConflictWithField(MethodDeclaration methodDeclaration) {
        super(methodDeclaration.getLine(), "Name of method " + methodDeclaration.getMethodName().getName() + " conflicts with a field's name");
    }

}
