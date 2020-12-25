package main.compileErrorException;

public class CompileErrorException extends Exception {
    private int line;
    private String message;

    public CompileErrorException(int line, String message) {
        this.line = line;
        this.message = message;
    }

    public String getMessage() {
        return "Line:" + this.line + ":" + this.message;
    }
}
