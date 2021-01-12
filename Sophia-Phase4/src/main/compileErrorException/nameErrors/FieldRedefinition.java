package main.compileErrorException.nameErrors;

import main.ast.nodes.declaration.classDec.classMembersDec.FieldDeclaration;
import main.compileErrorException.CompileErrorException;

public class FieldRedefinition extends CompileErrorException {

    public FieldRedefinition(FieldDeclaration fieldDeclaration) {
        super(fieldDeclaration.getLine(), "Redefinition of field " + fieldDeclaration.getVarDeclaration().getVarName().getName());
    }

}
