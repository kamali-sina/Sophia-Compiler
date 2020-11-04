grammar Lexer;

main_scope: class_declaration*;
function_scope: LBRACE (variable_declaration SEMICOLON)* statement* RBRACE;
class_scope: LBRACE ((variable_declaration SEMICOLON)* function_declaration* constructor_declaration? function_declaration*) RBRACE;

//expressions
statement: normal_brace | conditional_statement | jump_statement | print_statement | conditional_statement | (assignment SEMICOLON);
normal_brace: LBRACE statement* RBRACE;
//jump_statement
jump_statement: (control_statement | return_statement) SEMICOLON;
return_statement: RETURN (IDENTIFIER | LITERAL | );
control_statement: CONTROL;
//print statement
print_statement: PRINT LPAR expression RPAR SEMICOLON;
//conditinal statement
conditional_statement: IF condition statement else_statement?;
condition: LPAR expression RPAR;
else_statement: ELSE statement;
//loop statements
loop_statement: for_statement | foreach_statement;
for_statement: FOR LPAR (assignment | ) SEMICOLON (expression | ) SEMICOLON (assignment | ) RPAR statement;
foreach_statement: FOREACH LPAR IDENTIFIER IN expression RPAR statement;
//declaration statement

class_declaration: CLASSDEC IDENTIFIER (inheritance)? class_scope;
inheritance: INHERITANCE IDENTIFIER;
constructor_declaration: DEF IDENTIFIER LPAR function_parameters RPAR function_scope;
function_declaration: DEF (type | VOID) IDENTIFIER LPAR function_parameters RPAR function_scope;
function_parameters: (variable_declaration (COMMA variable_declaration)*)?;
variable_declaration: IDENTIFIER COLON type;


//TODO: expressions
// expression: arithmatic_expression | incremental_expression | compatative_expression | assignment | logical_expression | not_expression | value;
expression: value;
assignment: expression ASSIGNMENT_OPERATOR expression;
// expression_handler: ;
// arithmatic_expression:  ARITHMETIC_OPERATOR expression;
// incremental_expression:  (expression INCREMENTAL_OPERATOR) | (INCREMENTAL_OPERATOR expression);
// compatative_expression: expression (COMPARATIVE_OPERATOR | GREATER_THAN | LESS_THAN) expression;
// logical_expression: (expression LOGICAL_OPERATOR expression) | (not_expression);
// term: prefix_operator? value;
// prefix_operator: ;
// not_expression: NOT_OPERATOR expression;

value: LITERAL | variable | class_isntantiation;
class_isntantiation: NEW IDENTIFIER LPAR parameters RPAR;
variable: (IDENTIFIER | list_refrence | method_call | THIS) (dot_refrence | bracket_indexing)*;
list_refrence: IDENTIFIER bracket_indexing;
method_call: IDENTIFIER LPAR parameters  RPAR;
parameters: (value (COMMA value)*)?;
dot_refrence: DOT (IDENTIFIER | list_refrence | method_call);
bracket_indexing: LBRACK value RBRACK;

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
FOR: 'for';
FOREACH: 'foreach';
CONTROL: 'break' | 'continue';
types: type (COMMA type)*;
type: PRIMITIVE_TYPE | func_refrense | list_declaration | IDENTIFIER;
list_declaration: (LIST LPAR list_parameters RPAR)
                | (LIST LPAR value HASH type RPAR);
list_parameters: (variable_declaration | type) (COMMA (variable_declaration | type))*;
func_refrense: FUNC LESS_THAN (types | VOID) ARROW (type | VOID) GREATER_THAN;
PRIMITIVE_TYPE: 'int' | 'boolean' | 'string';

//symbols
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
INTEGER: (NONZERODIGIT DIGIT*) | [0];
LIST : 'list';
BOOL_VALUE: 'true' | 'false';
IDENTIFIER: (LETTER | UNDERSCORE) (LETTER | DIGIT | UNDERSCORE)*;

//helpers
LETTER: [a-zA-Z];
UNDERSCORE: '_';
DIGIT: [0-9];
NONZERODIGIT: [1-9];

//white spaces
WHITESPACE: [ \t\r\n]+ -> skip;
COMMENT: '//' .*? '\n' -> skip;
// MLCOMMENT: '/*' .*? '*/' -> skip;