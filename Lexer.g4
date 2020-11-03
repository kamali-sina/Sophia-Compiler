grammar Lexer;

// line: class_declaration | variable_declaration | function_declaration | conditional_statement | loop_statement| return_line | assignment | print | flow_control;

//TODO: sina.k
conditional_statement: ;
loop_statement: ;
variable_declaration: ;
//TODO: sina.n
class_declaration: ;
constructor_declaration: ;
function_declaration: ;
//TODO: TOMMOROW
// mathematical and ... operators ... 
expression: ;
assignment: IDENTIFIER ASSIGNMENT_OPERATOR ; //...

//--------------------
flow_control : CONTROL;
return_line: RETURN (IDENTIFIER | LITERAL | );
//class creation 
print: PRINT LPAR (IDENTIFIER | LITERAL) RPAR SEMICOLON;


//key words
KEYWORD: CLASSDEC | INHERITANCE | THIS | DEF | FUNC | RETURN | IF | ELSE | LOOP | CONTROL |
         BOOL_VALUE | VAR_TYPE | VOID | IN | NULL | NEW | PRINT;

THIS: 'this';
DEF: 'def';
FUNC: 'func';
VOID: 'void';
IN: 'in';
NEW: 'new';
NULL: 'null';
CLASSDEC: 'class';
INHERITANCE: 'extends';
RETURN: 'return';
PRINT: 'print';
IF: 'if';
ELSE: 'else';
BOOL_VALUE: 'true' | 'false';
VAR_TYPE: 'int' | 'boolean' | 'string' | 'list';
LOOP: FOR | FOREACH;
FOR: 'for';
FOREACH: 'foreach';
CONTROL: 'break' | 'continue';

//symbols
OPERATOR: ARITHMETIC_OPERATOR | COMPARATIVE_OPERATOR | LOGICAL_OPERATOR | ASSIGNMENT_OPERATOR;
ARITHMETIC_OPERATOR: '+' | '-' | '*' | '/' | '%';
INCREMENTAL_OPERATOR: '++' | '--';
COMPARATIVE_OPERATOR: '==' | '!=' | '>' | '<';
LOGICAL_OPERATOR: '&&' | '||' | '!';
ASSIGNMENT_OPERATOR: '=';
LITERAL: INTEGER | STRING | BOOL_VALUE;
SEMICOLON: ';';
LBRACE: '{';
COLON: ':';
RBRACE: '}';
LPAR: '(';
RPAR: ')';

//typedefs
STRING: '"' ~('"')* '"';
IDENTIFIER: (LETTER | UNDERSCORE) (LETTER | DIGIT | UNDERSCORE)*;
INTEGER: (NONZERODIGIT DIGIT*) | [0];

//helpers
LETTER: [a-zA-Z];
UNDERSCORE: '_';
DIGIT: [0-9];
NONZERODIGIT: [1-9];

//white spaces
WHITESPACE: [ \t\r\n]+ -> skip;
COMMENT: '//' .*? '\n' -> skip;
// MLCOMMENT: '/*' .*? '*/' -> skip;