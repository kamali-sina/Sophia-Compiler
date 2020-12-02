package main.ast.nodes;

import main.visitor.IVisitor;

public abstract class Node {
    private int line;

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public abstract String toString();

    public abstract <T> T accept(IVisitor<T> visitor);
}

