package main;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;

import java.io.IOException;

public class Sophia {
    public static void main(String[] args) throws IOException {
        CharStream reader = CharStreams.fromFileName(args[0]);
        SophiaCompiler sophiaCompiler = new SophiaCompiler();
        sophiaCompiler.compile(reader);
    }
}
