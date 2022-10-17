package main.compileErrorException.nameErrors;

import main.ast.nodes.declaration.classDec.classMembersDec.MethodDeclaration;
import main.compileErrorException.CompileErrorException;

public class MethodRedefinition extends CompileErrorException {

    public MethodRedefinition(MethodDeclaration methodDeclaration) {
        super(methodDeclaration.getLine(), "Redefinition of method " + methodDeclaration.getMethodName().getName());
    }

}
