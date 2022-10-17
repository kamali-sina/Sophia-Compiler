package main.ast.nodes.declaration.classDec.classMembersDec;

import main.ast.nodes.declaration.Declaration;
import main.ast.nodes.declaration.variableDec.VarDeclaration;
import main.ast.nodes.expression.Identifier;
import main.ast.nodes.statement.Statement;
import main.ast.types.Type;
import main.visitor.IVisitor;

import java.util.ArrayList;

//line -> DEF
public class MethodDeclaration extends Declaration {
    protected Identifier methodName;
    protected Type returnType;
    protected ArrayList<VarDeclaration> args = new ArrayList<>();
    protected ArrayList<VarDeclaration> localVars = new ArrayList<>();
    protected ArrayList<Statement> body = new ArrayList<>();
    //don't need this until code generation
    protected boolean doesReturn = false;

    //returnType NullType on when it is Void
    public MethodDeclaration(Identifier methodName, Type returnType) {
        this.methodName = methodName;
        this.returnType = returnType;
    }

    public Identifier getMethodName() {
        return methodName;
    }

    public void setMethodName(Identifier methodName) {
        this.methodName = methodName;
    }

    public Type getReturnType() {
        return returnType;
    }

    public void setReturnType(Type returnType) {
        this.returnType = returnType;
    }

    public ArrayList<VarDeclaration> getArgs() {
        return args;
    }

    public void setArgs(ArrayList<VarDeclaration> args) {
        this.args = args;
    }

    public ArrayList<VarDeclaration> getLocalVars() {
        return localVars;
    }

    public void setLocalVars(ArrayList<VarDeclaration> localVars) {
        this.localVars = localVars;
    }

    public ArrayList<Statement> getBody() {
        return body;
    }

    public void setBody(ArrayList<Statement> body) {
        this.body = body;
    }

    public void addArg(VarDeclaration varDeclaration) {
        this.args.add(varDeclaration);
    }

    public void addLocalVar(VarDeclaration varDeclaration) {
        this.localVars.add(varDeclaration);
    }

    public void addBodyStatement(Statement statement) {
        this.body.add(statement);
    }

    public boolean getDoesReturn() {
        return doesReturn;
    }

    public void setDoesReturn(boolean doesReturn) {
        this.doesReturn = doesReturn;
    }

    @Override
    public String toString() {
        return "MethodDeclaration_" + this.methodName.getName();
    }

    @Override
    public <T> T accept(IVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
