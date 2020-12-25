// Generated from C:/Users/amirp/Desktop/Files/University/Term 7/Compiler-TA/Sophia-Phases/Phase 3/Sophia-Phase3/src/main/grammar\Sophia.g4 by ANTLR 4.8
package parsers;

    import main.ast.types.*;
    import main.ast.types.functionPointer.*;
    import main.ast.types.list.*;
    import main.ast.types.single.*;
    import main.ast.nodes.*;
    import main.ast.nodes.declaration.*;
    import main.ast.nodes.declaration.classDec.*;
    import main.ast.nodes.declaration.classDec.classMembersDec.*;
    import main.ast.nodes.declaration.variableDec.*;
    import main.ast.nodes.expression.*;
    import main.ast.nodes.expression.operators.*;
    import main.ast.nodes.expression.values.*;
    import main.ast.nodes.expression.values.primitive.*;
    import main.ast.nodes.statement.*;
    import main.ast.nodes.statement.loop.*;

import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class SophiaLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.8", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		DEF=1, EXTENDS=2, CLASS=3, PRINT=4, FUNC=5, NEW=6, CONTINUE=7, BREAK=8, 
		RETURN=9, FOREACH=10, IN=11, FOR=12, IF=13, ELSE=14, BOOLEAN=15, STRING=16, 
		INT=17, VOID=18, NULL=19, LIST=20, TRUE=21, FALSE=22, THIS=23, ARROW=24, 
		GREATER_THAN=25, LESS_THAN=26, NOT_EQUAL=27, EQUAL=28, MULT=29, DIVIDE=30, 
		MOD=31, PLUS=32, MINUS=33, AND=34, OR=35, NOT=36, ASSIGN=37, INCREMENT=38, 
		DECREMENT=39, LPAR=40, RPAR=41, LBRACK=42, RBRACK=43, LBRACE=44, RBRACE=45, 
		SHARP=46, COMMA=47, DOT=48, COLON=49, SEMICOLLON=50, INT_VALUE=51, IDENTIFIER=52, 
		STRING_VALUE=53, COMMENT=54, WS=55;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"DEF", "EXTENDS", "CLASS", "PRINT", "FUNC", "NEW", "CONTINUE", "BREAK", 
			"RETURN", "FOREACH", "IN", "FOR", "IF", "ELSE", "BOOLEAN", "STRING", 
			"INT", "VOID", "NULL", "LIST", "TRUE", "FALSE", "THIS", "ARROW", "GREATER_THAN", 
			"LESS_THAN", "NOT_EQUAL", "EQUAL", "MULT", "DIVIDE", "MOD", "PLUS", "MINUS", 
			"AND", "OR", "NOT", "ASSIGN", "INCREMENT", "DECREMENT", "LPAR", "RPAR", 
			"LBRACK", "RBRACK", "LBRACE", "RBRACE", "SHARP", "COMMA", "DOT", "COLON", 
			"SEMICOLLON", "INT_VALUE", "IDENTIFIER", "STRING_VALUE", "COMMENT", "WS"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'def'", "'extends'", "'class'", "'print'", "'func'", "'new'", 
			"'continue'", "'break'", "'return'", "'foreach'", "'in'", "'for'", "'if'", 
			"'else'", "'bool'", "'string'", "'int'", "'void'", "'null'", "'list'", 
			"'true'", "'false'", "'this'", "'->'", "'>'", "'<'", "'!='", "'=='", 
			"'*'", "'/'", "'%'", "'+'", "'-'", "'&&'", "'||'", "'!'", "'='", "'++'", 
			"'--'", "'('", "')'", "'['", "']'", "'{'", "'}'", "'#'", "','", "'.'", 
			"':'", "';'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "DEF", "EXTENDS", "CLASS", "PRINT", "FUNC", "NEW", "CONTINUE", 
			"BREAK", "RETURN", "FOREACH", "IN", "FOR", "IF", "ELSE", "BOOLEAN", "STRING", 
			"INT", "VOID", "NULL", "LIST", "TRUE", "FALSE", "THIS", "ARROW", "GREATER_THAN", 
			"LESS_THAN", "NOT_EQUAL", "EQUAL", "MULT", "DIVIDE", "MOD", "PLUS", "MINUS", 
			"AND", "OR", "NOT", "ASSIGN", "INCREMENT", "DECREMENT", "LPAR", "RPAR", 
			"LBRACK", "RBRACK", "LBRACE", "RBRACE", "SHARP", "COMMA", "DOT", "COLON", 
			"SEMICOLLON", "INT_VALUE", "IDENTIFIER", "STRING_VALUE", "COMMENT", "WS"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}


	public SophiaLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "Sophia.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\29\u0154\b\1\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
		",\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64\t"+
		"\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\3\2\3\2\3\2\3\2\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\4\3\4\3\4\3\4\3\4\3\4\3\5\3\5\3\5\3\5\3\5\3\5\3\6"+
		"\3\6\3\6\3\6\3\6\3\7\3\7\3\7\3\7\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3"+
		"\t\3\t\3\t\3\t\3\t\3\t\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\13\3\13\3\13\3\13"+
		"\3\13\3\13\3\13\3\13\3\f\3\f\3\f\3\r\3\r\3\r\3\r\3\16\3\16\3\16\3\17\3"+
		"\17\3\17\3\17\3\17\3\20\3\20\3\20\3\20\3\20\3\21\3\21\3\21\3\21\3\21\3"+
		"\21\3\21\3\22\3\22\3\22\3\22\3\23\3\23\3\23\3\23\3\23\3\24\3\24\3\24\3"+
		"\24\3\24\3\25\3\25\3\25\3\25\3\25\3\26\3\26\3\26\3\26\3\26\3\27\3\27\3"+
		"\27\3\27\3\27\3\27\3\30\3\30\3\30\3\30\3\30\3\31\3\31\3\31\3\32\3\32\3"+
		"\33\3\33\3\34\3\34\3\34\3\35\3\35\3\35\3\36\3\36\3\37\3\37\3 \3 \3!\3"+
		"!\3\"\3\"\3#\3#\3#\3$\3$\3$\3%\3%\3&\3&\3\'\3\'\3\'\3(\3(\3(\3)\3)\3*"+
		"\3*\3+\3+\3,\3,\3-\3-\3.\3.\3/\3/\3\60\3\60\3\61\3\61\3\62\3\62\3\63\3"+
		"\63\3\64\3\64\3\64\7\64\u012f\n\64\f\64\16\64\u0132\13\64\5\64\u0134\n"+
		"\64\3\65\3\65\7\65\u0138\n\65\f\65\16\65\u013b\13\65\3\66\3\66\7\66\u013f"+
		"\n\66\f\66\16\66\u0142\13\66\3\66\3\66\3\67\3\67\3\67\3\67\7\67\u014a"+
		"\n\67\f\67\16\67\u014d\13\67\3\67\3\67\38\38\38\38\2\29\3\3\5\4\7\5\t"+
		"\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16\33\17\35\20\37\21!\22#\23"+
		"%\24\'\25)\26+\27-\30/\31\61\32\63\33\65\34\67\359\36;\37= ?!A\"C#E$G"+
		"%I&K\'M(O)Q*S+U,W-Y.[/]\60_\61a\62c\63e\64g\65i\66k\67m8o9\3\2\t\3\2\63"+
		";\3\2\62;\5\2C\\aac|\6\2\62;C\\aac|\3\2$$\4\2\f\f\17\17\5\2\13\f\17\17"+
		"\"\"\2\u0158\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2"+
		"\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2"+
		"\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2"+
		"\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2\2"+
		"\2\2/\3\2\2\2\2\61\3\2\2\2\2\63\3\2\2\2\2\65\3\2\2\2\2\67\3\2\2\2\29\3"+
		"\2\2\2\2;\3\2\2\2\2=\3\2\2\2\2?\3\2\2\2\2A\3\2\2\2\2C\3\2\2\2\2E\3\2\2"+
		"\2\2G\3\2\2\2\2I\3\2\2\2\2K\3\2\2\2\2M\3\2\2\2\2O\3\2\2\2\2Q\3\2\2\2\2"+
		"S\3\2\2\2\2U\3\2\2\2\2W\3\2\2\2\2Y\3\2\2\2\2[\3\2\2\2\2]\3\2\2\2\2_\3"+
		"\2\2\2\2a\3\2\2\2\2c\3\2\2\2\2e\3\2\2\2\2g\3\2\2\2\2i\3\2\2\2\2k\3\2\2"+
		"\2\2m\3\2\2\2\2o\3\2\2\2\3q\3\2\2\2\5u\3\2\2\2\7}\3\2\2\2\t\u0083\3\2"+
		"\2\2\13\u0089\3\2\2\2\r\u008e\3\2\2\2\17\u0092\3\2\2\2\21\u009b\3\2\2"+
		"\2\23\u00a1\3\2\2\2\25\u00a8\3\2\2\2\27\u00b0\3\2\2\2\31\u00b3\3\2\2\2"+
		"\33\u00b7\3\2\2\2\35\u00ba\3\2\2\2\37\u00bf\3\2\2\2!\u00c4\3\2\2\2#\u00cb"+
		"\3\2\2\2%\u00cf\3\2\2\2\'\u00d4\3\2\2\2)\u00d9\3\2\2\2+\u00de\3\2\2\2"+
		"-\u00e3\3\2\2\2/\u00e9\3\2\2\2\61\u00ee\3\2\2\2\63\u00f1\3\2\2\2\65\u00f3"+
		"\3\2\2\2\67\u00f5\3\2\2\29\u00f8\3\2\2\2;\u00fb\3\2\2\2=\u00fd\3\2\2\2"+
		"?\u00ff\3\2\2\2A\u0101\3\2\2\2C\u0103\3\2\2\2E\u0105\3\2\2\2G\u0108\3"+
		"\2\2\2I\u010b\3\2\2\2K\u010d\3\2\2\2M\u010f\3\2\2\2O\u0112\3\2\2\2Q\u0115"+
		"\3\2\2\2S\u0117\3\2\2\2U\u0119\3\2\2\2W\u011b\3\2\2\2Y\u011d\3\2\2\2["+
		"\u011f\3\2\2\2]\u0121\3\2\2\2_\u0123\3\2\2\2a\u0125\3\2\2\2c\u0127\3\2"+
		"\2\2e\u0129\3\2\2\2g\u0133\3\2\2\2i\u0135\3\2\2\2k\u013c\3\2\2\2m\u0145"+
		"\3\2\2\2o\u0150\3\2\2\2qr\7f\2\2rs\7g\2\2st\7h\2\2t\4\3\2\2\2uv\7g\2\2"+
		"vw\7z\2\2wx\7v\2\2xy\7g\2\2yz\7p\2\2z{\7f\2\2{|\7u\2\2|\6\3\2\2\2}~\7"+
		"e\2\2~\177\7n\2\2\177\u0080\7c\2\2\u0080\u0081\7u\2\2\u0081\u0082\7u\2"+
		"\2\u0082\b\3\2\2\2\u0083\u0084\7r\2\2\u0084\u0085\7t\2\2\u0085\u0086\7"+
		"k\2\2\u0086\u0087\7p\2\2\u0087\u0088\7v\2\2\u0088\n\3\2\2\2\u0089\u008a"+
		"\7h\2\2\u008a\u008b\7w\2\2\u008b\u008c\7p\2\2\u008c\u008d\7e\2\2\u008d"+
		"\f\3\2\2\2\u008e\u008f\7p\2\2\u008f\u0090\7g\2\2\u0090\u0091\7y\2\2\u0091"+
		"\16\3\2\2\2\u0092\u0093\7e\2\2\u0093\u0094\7q\2\2\u0094\u0095\7p\2\2\u0095"+
		"\u0096\7v\2\2\u0096\u0097\7k\2\2\u0097\u0098\7p\2\2\u0098\u0099\7w\2\2"+
		"\u0099\u009a\7g\2\2\u009a\20\3\2\2\2\u009b\u009c\7d\2\2\u009c\u009d\7"+
		"t\2\2\u009d\u009e\7g\2\2\u009e\u009f\7c\2\2\u009f\u00a0\7m\2\2\u00a0\22"+
		"\3\2\2\2\u00a1\u00a2\7t\2\2\u00a2\u00a3\7g\2\2\u00a3\u00a4\7v\2\2\u00a4"+
		"\u00a5\7w\2\2\u00a5\u00a6\7t\2\2\u00a6\u00a7\7p\2\2\u00a7\24\3\2\2\2\u00a8"+
		"\u00a9\7h\2\2\u00a9\u00aa\7q\2\2\u00aa\u00ab\7t\2\2\u00ab\u00ac\7g\2\2"+
		"\u00ac\u00ad\7c\2\2\u00ad\u00ae\7e\2\2\u00ae\u00af\7j\2\2\u00af\26\3\2"+
		"\2\2\u00b0\u00b1\7k\2\2\u00b1\u00b2\7p\2\2\u00b2\30\3\2\2\2\u00b3\u00b4"+
		"\7h\2\2\u00b4\u00b5\7q\2\2\u00b5\u00b6\7t\2\2\u00b6\32\3\2\2\2\u00b7\u00b8"+
		"\7k\2\2\u00b8\u00b9\7h\2\2\u00b9\34\3\2\2\2\u00ba\u00bb\7g\2\2\u00bb\u00bc"+
		"\7n\2\2\u00bc\u00bd\7u\2\2\u00bd\u00be\7g\2\2\u00be\36\3\2\2\2\u00bf\u00c0"+
		"\7d\2\2\u00c0\u00c1\7q\2\2\u00c1\u00c2\7q\2\2\u00c2\u00c3\7n\2\2\u00c3"+
		" \3\2\2\2\u00c4\u00c5\7u\2\2\u00c5\u00c6\7v\2\2\u00c6\u00c7\7t\2\2\u00c7"+
		"\u00c8\7k\2\2\u00c8\u00c9\7p\2\2\u00c9\u00ca\7i\2\2\u00ca\"\3\2\2\2\u00cb"+
		"\u00cc\7k\2\2\u00cc\u00cd\7p\2\2\u00cd\u00ce\7v\2\2\u00ce$\3\2\2\2\u00cf"+
		"\u00d0\7x\2\2\u00d0\u00d1\7q\2\2\u00d1\u00d2\7k\2\2\u00d2\u00d3\7f\2\2"+
		"\u00d3&\3\2\2\2\u00d4\u00d5\7p\2\2\u00d5\u00d6\7w\2\2\u00d6\u00d7\7n\2"+
		"\2\u00d7\u00d8\7n\2\2\u00d8(\3\2\2\2\u00d9\u00da\7n\2\2\u00da\u00db\7"+
		"k\2\2\u00db\u00dc\7u\2\2\u00dc\u00dd\7v\2\2\u00dd*\3\2\2\2\u00de\u00df"+
		"\7v\2\2\u00df\u00e0\7t\2\2\u00e0\u00e1\7w\2\2\u00e1\u00e2\7g\2\2\u00e2"+
		",\3\2\2\2\u00e3\u00e4\7h\2\2\u00e4\u00e5\7c\2\2\u00e5\u00e6\7n\2\2\u00e6"+
		"\u00e7\7u\2\2\u00e7\u00e8\7g\2\2\u00e8.\3\2\2\2\u00e9\u00ea\7v\2\2\u00ea"+
		"\u00eb\7j\2\2\u00eb\u00ec\7k\2\2\u00ec\u00ed\7u\2\2\u00ed\60\3\2\2\2\u00ee"+
		"\u00ef\7/\2\2\u00ef\u00f0\7@\2\2\u00f0\62\3\2\2\2\u00f1\u00f2\7@\2\2\u00f2"+
		"\64\3\2\2\2\u00f3\u00f4\7>\2\2\u00f4\66\3\2\2\2\u00f5\u00f6\7#\2\2\u00f6"+
		"\u00f7\7?\2\2\u00f78\3\2\2\2\u00f8\u00f9\7?\2\2\u00f9\u00fa\7?\2\2\u00fa"+
		":\3\2\2\2\u00fb\u00fc\7,\2\2\u00fc<\3\2\2\2\u00fd\u00fe\7\61\2\2\u00fe"+
		">\3\2\2\2\u00ff\u0100\7\'\2\2\u0100@\3\2\2\2\u0101\u0102\7-\2\2\u0102"+
		"B\3\2\2\2\u0103\u0104\7/\2\2\u0104D\3\2\2\2\u0105\u0106\7(\2\2\u0106\u0107"+
		"\7(\2\2\u0107F\3\2\2\2\u0108\u0109\7~\2\2\u0109\u010a\7~\2\2\u010aH\3"+
		"\2\2\2\u010b\u010c\7#\2\2\u010cJ\3\2\2\2\u010d\u010e\7?\2\2\u010eL\3\2"+
		"\2\2\u010f\u0110\7-\2\2\u0110\u0111\7-\2\2\u0111N\3\2\2\2\u0112\u0113"+
		"\7/\2\2\u0113\u0114\7/\2\2\u0114P\3\2\2\2\u0115\u0116\7*\2\2\u0116R\3"+
		"\2\2\2\u0117\u0118\7+\2\2\u0118T\3\2\2\2\u0119\u011a\7]\2\2\u011aV\3\2"+
		"\2\2\u011b\u011c\7_\2\2\u011cX\3\2\2\2\u011d\u011e\7}\2\2\u011eZ\3\2\2"+
		"\2\u011f\u0120\7\177\2\2\u0120\\\3\2\2\2\u0121\u0122\7%\2\2\u0122^\3\2"+
		"\2\2\u0123\u0124\7.\2\2\u0124`\3\2\2\2\u0125\u0126\7\60\2\2\u0126b\3\2"+
		"\2\2\u0127\u0128\7<\2\2\u0128d\3\2\2\2\u0129\u012a\7=\2\2\u012af\3\2\2"+
		"\2\u012b\u0134\7\62\2\2\u012c\u0130\t\2\2\2\u012d\u012f\t\3\2\2\u012e"+
		"\u012d\3\2\2\2\u012f\u0132\3\2\2\2\u0130\u012e\3\2\2\2\u0130\u0131\3\2"+
		"\2\2\u0131\u0134\3\2\2\2\u0132\u0130\3\2\2\2\u0133\u012b\3\2\2\2\u0133"+
		"\u012c\3\2\2\2\u0134h\3\2\2\2\u0135\u0139\t\4\2\2\u0136\u0138\t\5\2\2"+
		"\u0137\u0136\3\2\2\2\u0138\u013b\3\2\2\2\u0139\u0137\3\2\2\2\u0139\u013a"+
		"\3\2\2\2\u013aj\3\2\2\2\u013b\u0139\3\2\2\2\u013c\u0140\7$\2\2\u013d\u013f"+
		"\n\6\2\2\u013e\u013d\3\2\2\2\u013f\u0142\3\2\2\2\u0140\u013e\3\2\2\2\u0140"+
		"\u0141\3\2\2\2\u0141\u0143\3\2\2\2\u0142\u0140\3\2\2\2\u0143\u0144\7$"+
		"\2\2\u0144l\3\2\2\2\u0145\u0146\7\61\2\2\u0146\u0147\7\61\2\2\u0147\u014b"+
		"\3\2\2\2\u0148\u014a\n\7\2\2\u0149\u0148\3\2\2\2\u014a\u014d\3\2\2\2\u014b"+
		"\u0149\3\2\2\2\u014b\u014c\3\2\2\2\u014c\u014e\3\2\2\2\u014d\u014b\3\2"+
		"\2\2\u014e\u014f\b\67\2\2\u014fn\3\2\2\2\u0150\u0151\t\b\2\2\u0151\u0152"+
		"\3\2\2\2\u0152\u0153\b8\2\2\u0153p\3\2\2\2\b\2\u0130\u0133\u0139\u0140"+
		"\u014b\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}