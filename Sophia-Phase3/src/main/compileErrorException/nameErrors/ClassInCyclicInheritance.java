package main.compileErrorException.nameErrors;

import main.ast.nodes.declaration.classDec.ClassDeclaration;
import main.compileErrorException.CompileErrorException;

public class ClassInCyclicInheritance extends CompileErrorException {

    public ClassInCyclicInheritance(ClassDeclaration classDeclaration) {
        super(classDeclaration.getLine(), "Class " + classDeclaration.getClassName().getName() + " is in an inheritance cycle");
    }

}
