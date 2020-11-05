grammar Lexer;

main_scope: class_declaration*;
function_scope: LBRACE (variable_declaration_statement)* statement* RBRACE;
class_scope: LBRACE ((variable_declaration_statement)* function_declaration* constructor_declaration? function_declaration*) RBRACE;

//expressions
statement: normal_brace | conditional_statement | loop_statement | jump_statement | print_statement | method_call_statement | conditional_statement | assignment_statement;
normal_brace: LBRACE statement* RBRACE;
//jump_statement
jump_statement: (control_statement | return_statement) SEMICOLON;
return_statement: RETURN (expression)?{System.out.println("Return");};
control_statement: CONTROL {System.out.println("Control:" + $CONTROL.getText());};
//print statement
print_statement: PRINT LPAR expression RPAR SEMICOLON{System.out.println("Built-in:print");};
//conditinal statement
conditional_statement: IF {System.out.println("Conditional:if");} condition statement else_statement? ;
condition: LPAR expression RPAR;
else_statement: ELSE {System.out.println("Conditional:else");} statement;
//loop statements
loop_statement: for_statement | foreach_statement;
for_statement: FOR {System.out.println("Loop:for");} LPAR (assignment | ) SEMICOLON (expression | ) SEMICOLON (assignment | ) RPAR statement;
foreach_statement: FOREACH {System.out.println("Loop:foreach");} LPAR IDENTIFIER IN expression RPAR statement;
//declaration statement
class_declaration: CLASSDEC IDENTIFIER {System.out.print("ClassDec:" + $IDENTIFIER.getText());} (inheritance | {System.out.println();}) class_scope;
inheritance: INHERITANCE IDENTIFIER {System.out.println("," + $IDENTIFIER.getText());};
constructor_declaration: DEF IDENTIFIER {System.out.println("ConstructorDec:" + $IDENTIFIER.getText());} LPAR function_parameters RPAR function_scope;
function_declaration: DEF (type | VOID) IDENTIFIER {System.out.println("MethodDec:" + $IDENTIFIER.getText());} LPAR function_parameters RPAR function_scope;
function_parameters: (variable_declaration (COMMA variable_declaration)*)?;
variable_declaration_statement: IDENTIFIER COLON type SEMICOLON {System.out.println("VarDec:" + $IDENTIFIER.getText());};
variable_declaration: IDENTIFIER COLON type;
//method call statement
method_call_statement: variable {System.out.println("MethodCall");} extra_parantheses SEMICOLON;
assignment_statement: assignment SEMICOLON ;
assignment: expression ASSIGNMENT_OPERATOR expression {System.out.println("Operator:=");};

//TODO: expressions
// expression: arithmatic_expression | incremental_expression | compatative_expression | assignment | logical_expression | not_expression | value;
expression: LPAR expression RPAR 
            | expression postfix_operator
            | prefix_operator expression
            | expression X=(SLASH | STAR | MOD) expression {System.out.println("Operator:" + $X.getText());}
            | expression Y=(PLUS | DASH) expression {System.out.println("Operator:" + $Y.getText());}
            | expression Z=(GREATER_THAN | LESS_THAN) expression {System.out.println("Operator:" + $Z.getText());}
            | expression F=(EQUAL | NOT_EQUAL) expression {System.out.println("Operator:" + $F.getText());}
            | expression LAND expression {System.out.println("Operator:&&");}
            | expression LOR expression {System.out.println("Operator:||");}
            | expression ASSIGNMENT_OPERATOR expression {System.out.println("Operator:=");}
            | value;
// e0: LPAR expression RPAR;
// e1: shit postfix_operator;
// e2: prefix_operator shit;
// e3: shit (SLASH | STAR | MOD) shit;
// e4: shit (PLUS | DASH) shit;
// e5: shit (GREATER_THAN | LESS_THAN) shit;
// e6: shit (EQUAL | NOT_EQUAL) shit;
// e7: shit LAND shit;
// e8: shit LOR shit;
// e9: shit ASSIGNMENT_OPERATOR shit;
postfix_operator: INC | DEC;
prefix_operator: DASH | NOT_OPERATOR | INC | DEC;


//values and variables
value: LITERAL | variable | class_isntantiation | empty_bracket;
empty_bracket: LBRACK parameters RBRACK;
class_isntantiation: NEW IDENTIFIER LPAR parameters RPAR;
variable: (IDENTIFIER | list_refrence | method_call | THIS) (dot_refrence | bracket_indexing | extra_parantheses)*;
extra_parantheses: LPAR parameters RPAR;
list_refrence: IDENTIFIER bracket_indexing;
method_call: IDENTIFIER LPAR parameters RPAR;
parameters: (expression (COMMA expression)*)?;
dot_refrence: DOT (IDENTIFIER | list_refrence | method_call);
bracket_indexing: LBRACK expression RBRACK;

//--------------------------------------------------------------------------------

//TOKENS
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
PLUS: '+';
STAR: '*';
SLASH: '/';
MOD: '%';
DASH: '-';
INC: '++';
DEC: '--';
EQUAL: '==';
NOT_EQUAL: '!=';
ARROW: '->';
GREATER_THAN: '>';
LESS_THAN: '<';
LAND: '&&';
LOR: '||';
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