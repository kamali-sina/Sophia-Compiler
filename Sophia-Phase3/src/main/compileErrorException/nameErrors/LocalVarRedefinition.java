package main.compileErrorException.nameErrors;

import main.ast.nodes.declaration.variableDec.VarDeclaration;
import main.compileErrorException.CompileErrorException;

public class LocalVarRedefinition extends CompileErrorException {

    public LocalVarRedefinition(VarDeclaration varDeclaration) {
        super(varDeclaration.getLine(), "Redefinition of local variable " + varDeclaration.getVarName().getName());
    }

}
