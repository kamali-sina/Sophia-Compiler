package main.ast.nodes.expression;

import main.ast.types.single.ClassType;
import main.visitor.IVisitor;

import java.util.ArrayList;

//line -> NEW
public class NewClassInstance extends Expression{
    private ClassType classType;
    private ArrayList<Expression> args = new ArrayList<>();

    public NewClassInstance(ClassType classType) {
        this.classType = classType;
    }

    public NewClassInstance(ClassType classType, ArrayList<Expression> args) {
        this.classType = classType;
        this.args = args;
    }

    public ClassType getClassType() {
        return classType;
    }

    public void setClassType(ClassType classType) {
        this.classType = classType;
    }

    public ArrayList<Expression> getArgs() {
        return args;
    }

    public void setArgs(ArrayList<Expression> args) {
        this.args = args;
    }

    public void addArg(Expression arg) {
        this.args.add(arg);
    }

    @Override
    public String toString() {
        return "NewClassInstance_" + this.classType.getClassName().getName();
    }

    @Override
    public <T> T accept(IVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
