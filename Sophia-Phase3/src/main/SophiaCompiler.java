package main;

import main.ast.nodes.Program;
import main.ast.types.list.ListNameType;
import main.ast.types.list.ListType;
import main.ast.types.single.IntType;
import main.visitor.nameAnalyzer.NameAnalyzer;
import main.visitor.typeChecker.TypeChecker;
import main.visitor.utils.ErrorReporter;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import parsers.SophiaLexer;
import parsers.SophiaParser;
import main.ast.types.*;

import java.util.ArrayList;


public class SophiaCompiler {
    public void compile(CharStream textStream) {
        SophiaLexer sophiaLexer = new SophiaLexer(textStream);
        CommonTokenStream tokenStream = new CommonTokenStream(sophiaLexer);
        SophiaParser sophiaParser = new SophiaParser(tokenStream);
        Program program = sophiaParser.sophia().sophiaProgram;
        ErrorReporter errorReporter = new ErrorReporter();

        NameAnalyzer nameAnalyzer = new NameAnalyzer(program);
        nameAnalyzer.analyze();
        int numberOfErrors = program.accept(errorReporter);
        if(numberOfErrors > 0)
            System.exit(1);

        TypeChecker typeChecker = new TypeChecker(nameAnalyzer.getClassHierarchy());
        program.accept(typeChecker);
        numberOfErrors = program.accept(errorReporter);
        if(numberOfErrors > 0)
            System.exit(1);

        System.out.println("Compilation successful");

//        ArrayList<ListNameType> elementsTypes1 = new ArrayList<>();
//        elementsTypes1.add(new ListNameType(new IntType()));
//        Type lis1 = new ListType(elementsTypes1);
//        ArrayList<ListNameType> elementsTypes2 = new ArrayList<>();
//        elementsTypes2.add(new ListNameType(new IntType()));
//        ListType lis2 = new ListType(elementsTypes2);
//        lis1 = new IntType();
//
//        if (lis1.getClass().equals(lis2.getClass())){
//            System.out.println("fuck yea?");
//        }else{
//            System.out.println("wut");
//        }
    }
}
