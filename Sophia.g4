grammar Sophia;

@header{
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
}

sophia returns[Program sophiaProgram]: p=program {$sophiaProgram = $p.programRet;} EOF;

program returns[Program programRet]: (c=sophiaClass {$programRet.addClass($c.classDec);})*;
//TODO: line sets and fix nodes that need new
sophiaClass returns[ClassDeclaration classDec]: CLASS {$classDec = new ClassDeclaration(); $classDec.setLine($CLASS.getLine());} 
            cn=identifier{$classDec.setClassName($cn.identity);}
            (EXTENDS pc=identifier {$classDec.setParentClassName($pc.identity);})?
            LBRACE
            (((vd1=varDeclaration {$classDec.addField(new FieldDeclaration($vd1.varDec));}
            | m1=method {$classDec.addMethod($m1.methodDec);})* 
            cc=constructor{$classDec.setConstructor($cc.constructorDec);}
            (vd2=varDeclaration {$classDec.addField(new FieldDeclaration($vd2.varDec));} 
            | m2=method {$classDec.addMethod($m2.methodDec);})*) |
            ((vd3=varDeclaration {$classDec.addField(new FieldDeclaration($vd3.varDec));} 
            | m2=method {$classDec.addMethod($m2.methodDec);})*))
            RBRACE;

varDeclaration returns[VarDeclaration varDec]: idd=identifier {$varDec.setVarName($idd.identity);} COLON 
            tp=type {$varDec.setType($tp.absType)} SEMICOLLON;

method returns[MethodDeclaration methodDec]: DEF (abs=type {$methodDec.setReturnType($abs.absType);} 
            | VOID {$methodDec.setReturnType(new NullType());})
            id=identifier {$methodDec.setMethodName($id.identity);} 
            LPAR ma=methodArguments {$methodDec.setArgs($ma.methodArgs)} RPAR 
            LBRACE mb=methodBody {$methodDec.setLocalVars($mb.localVars); $methodDec.setBody($mb.body);} RBRACE;

constructor returns[ConstructorDeclaration constructorDec]: DEF id=identifier {$constructorDec = new ConstructorDeclaration($id.identity);} 
            LPAR ma=methodArguments {$constructorDec.setArgs($ma.methodArgs)} RPAR 
            LBRACE mb=methodBody {$constructorDec.setLocalVars($mb.localVars); $constructorDec.setBody($mb.body);} RBRACE;

methodArguments returns[ArrayList<VarDeclaration> methodArgs]: (vwt1=variableWithType {$methodArgs.add($vwt1.varDecwType);} 
            (COMMA vwt2=variableWithType {$methodArgs.add($vwt2.varDecwType);})*)?;

variableWithType returns[VarDeclaration varDecwType]: vid=identifier {$varDecwType.setVarName($vid.identity);} 
            COLON vtp=type {$varDecwType.setType($vtp.absType);};

type returns[Type absType]: pdt=primitiveDataType {$absType = $pdt.pType;} 
            | lt=listType {$absType = $lt.list_type;}
            | fpt=functionPointerType {$absType = $fpt.functionPointer;}
            | ct=classType {$absType = $ct.cType;}
            ;

classType returns[ClassType cType]: id=identifier{$cType.setClassName($id.identity);};

listType returns [ListType list_type]: LIST LPAR ((INT_VALUE SHARP tp=type {$list_type = new ListType($INT_VALUE.getInt(), tp.absType)}) 
            | (lits=listItemsTypes {$list_type = new ListType($lits.elementTypes)})) RPAR;

listItemsTypes returns [ArrayList<ListNameType> elementTypes]: lit1=listItemType {$elementTypes.add($lit1.listnametype)} 
            (COMMA lit2=listItemType {$elementTypes.add($lit2.listnametype)})*;

listItemType returns [ListNameType listnametype]: vwt=variableWithType {$listnametype = new ListNameType($vwt.varDecwType);}
            | tp=type {$listnametype = new ListNameType($tp.absType);};

functionPointerType returns[FptrType functionPointer]: FUNC LESS_THAN 
            (VOID /*NOTE: nothing because list must be empty */| twc=typesWithComma {$functionPointer.setArgumentsTypes($twc.types);}) 
            ARROW (VOID {$functionPointer.setReturnType(new NullType());} | tp=type {$functionPointer.setReturnType($tp.absType);}) 
            GREATER_THAN;

typesWithComma returns[ArrayList<Type> types]: t1=type {$types.add($t1.absType);} 
            (COMMA t2=type {$types.add($t2.absType);} )*;

primitiveDataType returns[Type pType]: INT {$pType = new IntType();} 
            | STRING {$pType = new StringType();}
            | BOOLEAN {$pType = new BoolType();};

methodBody returns[ArrayList<VarDeclaration> localVars, ArrayList<Statement> body]: (varDecs=varDeclaration {$localVars.add($varDecs.varDec)})* 
            (stats=statement {$body.add($stats.statemnt)})*;

statement returns[Statement statemnt]: fs=forStatement {$statemnt = $fs.forstmnt;}
            | fes=foreachStatement {$statemnt = $fes.foreachstmnt;}
            | is=ifStatement {$statemnt = $is.ifelsestmnt;}
            | ass=assignmentStatement {$statemnt = $ass.assignmentstmnt;}
            | ps=printStatement {$statemnt = $ps.printstmnt;}
            | cbs=continueBreakStatement {$statemnt = $cbs.controlstmnt;}
            | mcs=methodCallStatement {$statemnt = $mcs.methodcallstmnt;}
            | rs=returnStatement {$statemnt = $rs.returnstmnt;}
            | b=block {$statemnt = $b.blockstmnt;}
            ;

block returns[BlockStmt blockstmnt]: LBRACE (stm=statement {$blockstmnt.addStatement($stm.statemnt);})* RBRACE;

assignmentStatement returns[AssignmentStmt assignmentstmnt]: ass=assignment {$assignmentstmnt = $ass.assignments} SEMICOLLON;

assignment returns[AssignmentStmt assignments]: ox=orExpression {$assignments.setlVal($ox.expr);} 
            ASSIGN  ex=expression {$assignments.setlVal($ex.expr);};

printStatement returns[PrintStmt printstmnt]: PRINT LPAR exp=expression {$printstmnt.setArg($exp.expr);} RPAR SEMICOLLON;

returnStatement returns[RetrunStmt returnstmnt]: RETURN (exp=expression {$returnstmnt.setReturnedExpr($exp.expr);})? SEMICOLLON;

methodCallStatement returns[MethodCallStmt methodcallstmnt]: mc=methodCall {$methodcallstmnt.setMethodCall($mc.mCall);} SEMICOLLON;

methodCall returns[MethodCall mCall] locals[Expression exp]: oex=otherExpression  {$exp = $oex.otherExpr;} 
            (
            (LPAR mca=methodCallArguments {$exp = new MethodCall($exp, $mca.arguments);} RPAR) 
            | (DOT id=identifier {$exp = new ObjectOrListMemberAccess($exp, $id.identity);}) 
            | (LBRACK ex=expression {$exp = new ListAccessByIndex($exp, $ex.expr);} RBRACK)
            )* 
            (LPAR mca2=methodCallArguments {mCall = new MethodCall($exp, $mca2.arguments);} RPAR);

methodCallArguments returns[ArrayList<Expression> arguments]: (expr1=expression {$arguments.add($expr1.expr);} 
            (COMMA expr2=expression {$arguments.add($expr2.expr);})*)?;

continueBreakStatement returns[statement controlstmnt]: (BREAK {$controlstmnt = new BreakStmt();}
            | CONTINUE {$controlstmnt = new ContinueStmt();}) 
            SEMICOLLON;

forStatement returns[ForStmt forstmnt]: FOR 
            LPAR (ass1=assignment {$forstmnt.setInitialize($ass1.assignments);} )? SEMICOLLON 
            (xp=expression {$forstmnt.setCondition($xp.expr);})? SEMICOLLON 
            (ass2=assignment {$forstmnt.setUpdate($ass2.assignments);})? RPAR 
            stm=statement {$forstmnt.setBody($stm.statemnt);}
            ;

foreachStatement returns[ForeachStmt foreachstmnt]: FOREACH 
            LPAR id=identifier {$foreachstmnt.setVariable($id.identity);}
            IN exp=expression {$foreachstmnt.setList($exp.expr);}
            RPAR stm=statement{$foreachstmnt.setBody($stm.statemnt);}
            ;

ifStatement returns[ConditionalStmt ifelsestmnt]: IF 
            LPAR exp=expression {$ifelsestmnt.setCondition($exp.expr);}
            RPAR stm1=statement {$ifelsestmnt.setThenBody($stm1.statemnt);}
            (ELSE stm2=statement {$ifelsestmnt.setElseBody($stm2.statemnt);})?
            ;

expression returns[Expression expr]: ore1=orExpression {$expr = $ore1.expr;}
            (ASSIGN ore2=expression {$expr = new BinaryExpression($expr, $ore2.expr, BinaryOperator.or);})?;

orExpression returns[Expression expr]: ane1=andExpression {$expr = $ane1.expr;} 
            (OR ane2=andExpression {$expr = new BinaryExpression($expr, $ane2.expr, BinaryOperator.or);})*;

andExpression returns[Expression expr]: eqe1=equalityExpression {$expr = $eqe1.expr;}
            (AND eqe2=equalityExpression {$expr = new BinaryExpression($expr, $eqe2.expr, BinaryOperator.and);})*;

equalityExpression returns[Expression expr] locals[BinaryOperator op]: ree1=relationalExpression {$expr = $ree1.expr;}
            ((EQUAL {$op = BinaryOperator.eq;}| NOT_EQUAL {$op = BinaryOperator.neq;}) 
            ree2=relationalExpression {$expr = new BinaryExpression($expr, $ree2.expr, $op);})*;

relationalExpression returns[Expression expr] locals[BinaryOperator op]: ade1=additiveExpression  {$expr = $ade1.expr;}
            ((GREATER_THAN {$op = BinaryOperator.gt;}| LESS_THAN {$op = BinaryOperator.lt;}) 
            ade2=additiveExpression {$expr = new BinaryExpression($expr, $ade2.expr, $op);})*;

additiveExpression returns[Expression expr] locals[BinaryOperator op]: mce1=multiplicativeExpression {$expr = $mce1.expr;}
            ((PLUS {$op = BinaryOperator.add;} | MINUS {$op = BinaryOperator.sub;}) 
            mce2=multiplicativeExpression {$expr = new BinaryExpression($expr, $mce2.expr, $op);})*;

multiplicativeExpression returns[Expression expr] locals[BinaryOperator op]: pue1=preUnaryExpression{$expr = $pue1.expr;} 
            ((MULT {$op = BinaryOperator.mult;} | DIVIDE {$op = BinaryOperator.div;} | MOD {$op = BinaryOperator.mod;}) 
            pue2=preUnaryExpression {$expr = new BinaryExpression($expr, $pue2.expr, $op);})*;

preUnaryExpression returns[Expression expr] locals[UnaryOperator op]: ((NOT {$op = UnaryOperator.not;} 
            | MINUS {$op = UnaryOperator.minus;} 
            | INCREMENT {$op = UnaryOperator.preinc;}
            | DECREMENT {$op = UnaryOperator.predec;}) 
            preue=preUnaryExpression {$expr = new UnaryExpression($preue.expr, $op);}) 
            | postue=postUnaryExpression {$expr = $postue.expr;};

postUnaryExpression returns[Expression expr]: aex=accessExpression {$expr = $aex.exp;}
            (
            INCREMENT {$expr = new UnaryExpression($expr, UnaryOperator.postinc);} 
            | DECREMENT {$expr = new UnaryExpression($expr, UnaryOperator.postdec);}
            )?
            ;

accessExpression returns[Expression exp]: oex=otherExpression {$exp = $oex.otherExpr;} 
            ((LPAR mca=methodCallArguments {$exp = new MethodCall($exp, $mca.arguments);}RPAR)  
            | (DOT id=identifier {$exp = new ObjectOrListMemberAccess($exp, $id.identity);}) 
            | (LBRACK ex=expression {$exp = new ListAccessByIndex($exp, $ex.expr);} RBRACK))*;

otherExpression returns[Expression otherExpr]: THIS {$otherExpr = new ThisClass();} 
            | ne=newExpression {$otherExpr = $ne.classInstance} 
            | vs=values {$otherExpr = $vs.val;} 
            | identi=identifier {$otherExpr = $identi.identity} 
            | LPAR (expp=expression {$otherExpr = $expp.expr}) RPAR;

newExpression returns[NewClassInstance classInstance]: NEW 
            ct=classType {$classInstance = new NewClassInstance($ct.cType);} 
            LPAR mca=methodCallArguments {$classInstance.setArgs($mca.arguments);} 
            RPAR
            ;
//TODO: check getInt()
values returns[Value val]: bv=boolValue {$val = $bv.boolVal;} 
            | STRING_VALUE {$val = new StringValue($STRING_VALUE.getText());} 
            | INT_VALUE {$val = new IntValue($INT_VALUE.getInt());} 
            | NULL {$val = new NullValue();} 
            | lv=listValue {$val = $lv.listVal;}
            ;

boolValue returns[BoolValue boolVal]: TRUE {$boolVal.setConstant(true);} 
            | FALSE {$boolVal.setConstant(false);};

listValue returns[ListValue listVal]: LBRACK mca=methodCallArguments {$listVal.setElements($mca.arguments);} RBRACK;

identifier returns[Identifier identity]: IDENTIFIER {$identity.setName($IDENTIFIER.getText());};


DEF: 'def';
EXTENDS: 'extends';
CLASS: 'class';

PRINT: 'print';
FUNC: 'func';

NEW: 'new';

CONTINUE: 'continue';
BREAK: 'break';
RETURN: 'return';

FOREACH: 'foreach';
IN: 'in';
FOR: 'for';
IF: 'if';
ELSE: 'else';

BOOLEAN: 'bool';
STRING: 'string';
INT: 'int';
VOID: 'void';
NULL: 'null';
LIST: 'list';

TRUE: 'true';
FALSE: 'false';

THIS: 'this';

ARROW: '->';
GREATER_THAN: '>';
LESS_THAN: '<';
NOT_EQUAL: '!=';
EQUAL: '==';

MULT: '*';
DIVIDE: '/';
MOD: '%';
PLUS: '+';
MINUS: '-';
AND: '&&';
OR: '||';
NOT: '!';
QUESTION_MARK: '?';

ASSIGN: '=';

INCREMENT: '++';
DECREMENT: '--';

LPAR: '(';
RPAR: ')';
LBRACK: '[';
RBRACK: ']';
LBRACE: '{';
RBRACE: '}';

SHARP: '#';
COMMA: ',';
DOT: '.';
COLON: ':';
SEMICOLLON: ';';

INT_VALUE: '0' | [1-9][0-9]*;
IDENTIFIER: [a-zA-Z_][A-Za-z0-9_]*;
STRING_VALUE: '"'~["]*'"';
COMMENT: ('//' ~( '\r' | '\n')*) -> skip;
WS: ([ \t\n\r]) -> skip;