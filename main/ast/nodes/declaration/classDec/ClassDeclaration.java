package main.ast.nodes.declaration.classDec;

import main.ast.nodes.declaration.Declaration;
import main.ast.nodes.declaration.classDec.classMembersDec.ConstructorDeclaration;
import main.ast.nodes.declaration.classDec.classMembersDec.FieldDeclaration;
import main.ast.nodes.declaration.classDec.classMembersDec.MethodDeclaration;
import main.ast.nodes.expression.Identifier;
import main.visitor.IVisitor;

import java.util.ArrayList;

//line -> CLASS
public class ClassDeclaration extends Declaration {
    private Identifier className;
    private Identifier parentClassName;
    private ArrayList<FieldDeclaration> fields = new ArrayList<>();
    private ConstructorDeclaration constructor;
    private ArrayList<MethodDeclaration> methods = new ArrayList<>();

    public ClassDeclaration(Identifier className) {
        this.className = className;
    }

    public Identifier getClassName() {
        return className;
    }

    public void setClassName(Identifier className) {
        this.className = className;
    }

    public Identifier getParentClassName() {
        return parentClassName;
    }

    public void setParentClassName(Identifier parentClassName) {
        this.parentClassName = parentClassName;
    }

    public ConstructorDeclaration getConstructor() {
        return constructor;
    }

    public void setConstructor(ConstructorDeclaration constructor) {
        this.constructor = constructor;
    }

    public ArrayList<MethodDeclaration> getMethods() {
        return methods;
    }

    public void setMethods(ArrayList<MethodDeclaration> methods) {
        this.methods = methods;
    }

    public ArrayList<FieldDeclaration> getFields() {
        return fields;
    }

    public void setFields(ArrayList<FieldDeclaration> fields) {
        this.fields = fields;
    }

    public void addMethod(MethodDeclaration methodDeclaration) {
        this.methods.add(methodDeclaration);
    }

    public void addField(FieldDeclaration fieldDeclaration) {
        this.fields.add(fieldDeclaration);
    }

    @Override
    public String toString() {
        return "ClassDeclaration_" + this.className.getName();
    }

    @Override
    public <T> T accept(IVisitor<T> visitor) {
        return visitor.visit(this);
    }
}