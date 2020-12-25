package main.compileErrorException.typeErrors;

import main.ast.nodes.declaration.classDec.classMembersDec.MethodDeclaration;
import main.compileErrorException.CompileErrorException;

public class MissingReturnStatement extends CompileErrorException {

    public MissingReturnStatement(MethodDeclaration methodDeclaration) {
        super(methodDeclaration.getLine(), "Method " + methodDeclaration.getMethodName().getName() + " needs return statement");
    }

}
