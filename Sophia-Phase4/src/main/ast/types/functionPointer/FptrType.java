package main.ast.types.functionPointer;

import main.ast.types.Type;

import java.util.ArrayList;

public class FptrType extends Type {
    private ArrayList<Type> argumentsTypes = new ArrayList<>();
    private Type returnType;

    public FptrType() {
    }

    //empty ArrayList when no arguments
    //NullType on returnType when void
    public FptrType(ArrayList<Type> argumentsTypes, Type returnType) {
        this.argumentsTypes = argumentsTypes;
        this.returnType = returnType;
    }

    public ArrayList<Type> getArgumentsTypes() {
        return argumentsTypes;
    }

    public void setArgumentsTypes(ArrayList<Type> argumentsTypes) {
        this.argumentsTypes = argumentsTypes;
    }

    public Type getReturnType() {
        return returnType;
    }

    public void setReturnType(Type returnType) {
        this.returnType = returnType;
    }

    public void addArgumentType(Type type) {
        this.argumentsTypes.add(type);
    }

    @Override
    public String toString() {
        return "FptrType";
    }
}
