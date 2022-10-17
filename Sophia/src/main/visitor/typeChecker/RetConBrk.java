package main.visitor.typeChecker;

public class RetConBrk {
    public boolean doesReturn;
    public boolean doesBreakContinue;

    public RetConBrk(boolean doesReturn, boolean doesBreakContinue) {
        this.doesReturn = doesReturn;
        this.doesBreakContinue = doesBreakContinue;
    }
}
