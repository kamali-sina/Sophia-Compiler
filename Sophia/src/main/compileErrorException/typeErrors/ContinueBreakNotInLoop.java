package main.compileErrorException.typeErrors;

import main.compileErrorException.CompileErrorException;

public class ContinueBreakNotInLoop extends CompileErrorException {

    //type: 0->break  1->continue
    public ContinueBreakNotInLoop (int line, int type) {
        super(line, ((type == 0) ? "Break" : "Continue") + " statement not within loop");
    }

}
