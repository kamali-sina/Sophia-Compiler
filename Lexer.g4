grammar Lexer;

/*TODO: how to add method_call to statement */

/*note: use this for scopes!!! */
main_scope: class_declaration*;
function_scope: LBRACE variable_declaration* statement* RBRACE;
class_scope: LBRACE (variable_declaration* function_declaration* constructor_declaration? function_declaration*) RBRACE;

//expressions
statement: normal_brace | conditional_statement | jump_statement | print_statement | conditional_statement | (assignment SEMICOLON);
normal_brace: LBRACE statement* RBRACE;
//jump_statement
jump_statement: (control_statement | return_statement) SEMICOLON;
return_statement: RETURN (IDENTIFIER | LITERAL | );
control_statement: CONTROL;
//print statement
print_statement: PRINT LPAR value RPAR SEMICOLON;
//conditinal statement
conditional_statement: IF condition statement (else | );
condition: LPAR expression RPAR;
else: ELSE statement;
//loop statements
loop_statement: for_statement | foreach_statement;
for_statement: FOR LPAR (assignment | ) SEMICOLON (expression | ) SEMICOLON (assignment | ) RPAR statement;
foreach_statement: FOREACH LPAR IDENTIFIER IN expression RPAR statement; //maybe IDENTIFIER in varible and make varible a grammer?
//declaration statement
type: PRIMITIVE_TYPE | func_refrense | list_declaration | IDENTIFIER;
variable_declaration: IDENTIFIER COLON type SEMICOLON;
list_declaration: (LIST LPAR (variable_declaration | type)*  RPAR)
                | (LIST LPAR value HASH type RPAR);
func_refrense: FUNC LESS_THAN (types | VOID) ARROW (type | VOID) GREATER_THAN;
class_declaration: CLASSDEC IDENTIFIER (inheritance)? class_scope; // constructor_declaration in scope, variables should come first
inheritance: INHERITANCE IDENTIFIER;
types: type (COMMA type)*;
constructor_declaration: DEF IDENTIFIER LPAR function_parameters RPAR function_scope; //not list
function_declaration: DEF (type | VOID) IDENTIFIER LPAR function_parameters RPAR function_scope; //not list, variables should come first
function_parameters: (variable_declaration (COMMA variable_declaration)*)?;
//TODO: sina.n
/*note: havaset bashe az scope estefade koni, be mesal e if statement am negah kon*/
//we need to add statement and expression
// function_scope: LBRACE (variable_declaration* statements) RBRACE;


//TODO: TOMMOROW
// mathematical and ... operators ... expression can contain several operators
expression: arithmatic_expression | incremental_expression | compatative_expression | assignment | logical_expression | not_expression | value;
arithmatic_expression: expression ARITHMETIC_OPERATOR expression;
incremental_expression:  (expression INCREMENTAL_OPERATOR) | (INCREMENTAL_OPERATOR expression);
compatative_expression: expression (COMPARATIVE_OPERATOR | GREATER_THAN | LESS_THAN) expression;
logical_expression: (expression LOGICAL_OPERATOR expression) | (not_expression);
not_expression: NOT_OPERATOR expression; 

value: LITERAL | variable | class_isntantiation;
class_isntantiation: NEW IDENTIFIER LPAR parameters RPAR;
variable: (IDENTIFIER | list_refrence | method_call) (dot_refrence | bracket_indexing)*;
list_refrence: IDENTIFIER bracket_indexing;
method_call: IDENTIFIER LPAR parameters  RPAR;
parameters: (value (COMMA value)*)?;
dot_refrence: DOT (IDENTIFIER | list_refrence | method_call);
bracket_indexing: LBRACK value RBRACK;
assignment: expression ASSIGNMENT_OPERATOR expression;

//class creation 

//key words
// KEYWORD: CLASSDEC | INHERITANCE | THIS | DEF | FUNC | RETURN | IF | ELSE | LOOP | CONTROL |
//          BOOL_VALUE | VAR_TYPE | VOID | IN | NULL | NEW | PRINT;
// FUNC_TYPE: VOID;
PRIMITIVE_TYPE: 'int' | 'boolean' | 'string';
LOOP: FOR | FOREACH;
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
FOR: 'for';
FOREACH: 'foreach';
CONTROL: 'break' | 'continue';

//symbols
OPERATOR: ARITHMETIC_OPERATOR | COMPARATIVE_OPERATOR | LOGICAL_OPERATOR | ASSIGNMENT_OPERATOR;
ARITHMETIC_OPERATOR: '+' | '-' | '*' | '/' | '%';
INCREMENTAL_OPERATOR: '++' | '--';
COMPARATIVE_OPERATOR: '==' | '!=';
ARROW: '->';
GREATER_THAN: '>';
LESS_THAN: '<';
LOGICAL_OPERATOR: '&&' | '||';
NOT_OPERATOR: '!';
ASSIGNMENT_OPERATOR: '=';
LITERAL: INTEGER | STRING | BOOL_VALUE;
SEMICOLON: ';';
LBRACE: '{';
COLON: ':';
RBRACE: '}';
LPAR: '(';
RPAR: ')';
LBRACK: '[';
DOT: '.';
COMMA: ',';
HASH: '#';
RBRACK: ']';

//typedefs
STRING: '"' ~('"')* '"';
IDENTIFIER: (LETTER | UNDERSCORE) (LETTER | DIGIT | UNDERSCORE)*;
INTEGER: (NONZERODIGIT DIGIT*) | [0];
LIST : 'list';

//helpers
LETTER: [a-zA-Z];
UNDERSCORE: '_';
DIGIT: [0-9];
NONZERODIGIT: [1-9];

//white spaces
WHITESPACE: [ \t\r\n]+ -> skip;
COMMENT: '//' .*? '\n' -> skip;
// MLCOMMENT: '/*' .*? '*/' -> skip;