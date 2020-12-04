package main;

import main.ast.nodes.Program;
import main.visitor.ASTTreePrinter;
import main.visitor.InheritanceHandler;
import main.visitor.NameCollector;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import parsers.SophiaLexer;
import parsers.SophiaParser;

public class SophiaCompiler {
    public void compile(CharStream textStream) {
        SophiaLexer sophiaLexer = new SophiaLexer(textStream);
        CommonTokenStream tokenStream = new CommonTokenStream(sophiaLexer);
        SophiaParser sophiaParser = new SophiaParser(tokenStream);
        Program program = sophiaParser.sophia().sophiaProgram;
        NameCollector nameCollector = new NameCollector();
        nameCollector.visit(program);
        InheritanceHandler inheritanceHandler = new InheritanceHandler();
        inheritanceHandler.setInfo(nameCollector);
        inheritanceHandler.visit(program);
        if (inheritanceHandler.getNumberErrors() == 0) {
            ASTTreePrinter astTreePrinter = new ASTTreePrinter();
            astTreePrinter.visit(program);
        }
    }
}
