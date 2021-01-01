package main.visitor.typeChecker;

public class Block {
    public boolean doesBreak = false;
    public boolean doesContinue = false;
    public boolean isLoopBlock = false; // Foreach and For
    public boolean hasUnreachableStatements = false;
    public boolean doesReturn = false;
}
