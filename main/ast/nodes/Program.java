package main.ast.nodes;

import main.ast.nodes.declaration.classDec.ClassDeclaration;
import main.visitor.IVisitor;

import java.util.ArrayList;

//line -> 1
public class Program extends Node {
    private ArrayList<ClassDeclaration> classes = new ArrayList<>();

    public Program() {
    }

    public Program(ArrayList<ClassDeclaration> classes) {
        this.classes = classes;
    }

    public ArrayList<ClassDeclaration> getClasses() {
        return classes;
    }

    public void setClasses(ArrayList<ClassDeclaration> classes) {
        this.classes = classes;
    }

    public void addClass(ClassDeclaration classDeclaration) {
        classes.add(classDeclaration);
    }

    @Override
    public String toString() {
        return "Program";
    }

    public <R> R accept(IVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
