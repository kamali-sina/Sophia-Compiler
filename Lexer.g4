grammar Lexer;

//key words
KEYWORD: CLASSDEC | INHERITANCE | 'this' | 'def' | 'func' | RETURN | CONDITIONAL | LOOP | CONTROL |
         BOOL_VALUE | VAR_TYPE | 'void' | 'in' | NULL | 'new' | BUILT_IN;
NULL: 'null';
CLASSDEC: 'class';
INHERITANCE: 'extends';
RETURN: 'return';
BUILT_IN: 'print';
CONDITIONAL: 'if' | 'else';
BOOL_VALUE: 'true' | 'false';
VAR_TYPE: 'int' | 'boolean' | 'string' | 'list';
LOOP: 'for' | 'foreach';
CONTROL: 'break' | 'continue' ;

//symbols
OPERATOR: ARITHMETIC_OPERATOR | COMPARATIVE_OPERATOR | LOGICAL_OPERATOR | ASSIGNMENT_OPERATOR;
ARITHMETIC_OPERATOR: '+' | '-' | '*' | '/' | '%' | '--' | '++';
COMPARATIVE_OPERATOR: '==' | '!=' | '>' | '<';
LOGICAL_OPERATOR: '&&' | '||' | '!';
ASSIGNMENT_OPERATOR: '=';
//LITERAL: INTEGER | STRING | BOOL_VALUE
SEMICOLON: ';';
LBRACE: '{';
RBRACE: '}';
LPAR: '(';
RPAR: ')';

//typedefs
STRING: '"' ~('"') '"';
IDENTIFIER: LETTER (LETTER | DIGIT)*;
INTEGER: (NONZERODIGIT DIGIT*) | [0];

//helpers
LETTER: [a-zA-Z_];
DIGIT: [0-9];
NONZERODIGIT: [1-9];

//white spaces
WHITESPACE: [ \t\r\n]+ -> skip;
COMMENT: '//' .*? '\n' -> skip;
MLCOMMENT: '/*' .*? '*/' -> skip;


