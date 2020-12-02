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

sophia returns[Program sophiaProgram]: p=program {$sophiaProgram = $p.programRet; $sophiaProgram.setLine(1);} EOF;

program returns[Program programRet]: 
            {$programRet = new Program(); $programRet.setLine(1);} 
            (c=sophiaClass {$programRet.addClass($c.classDec);})*;

sophiaClass returns[ClassDeclaration classDec]: 
            CLASS
            cn=identifier{$classDec = new ClassDeclaration($cn.identity); $classDec.setLine($CLASS.getLine());}
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

varDeclaration returns[VarDeclaration varDec]: 
            idd=identifier COLON tp=type 
            {$varDec = new VarDeclaration($idd.identity, $tp.absType); $varDec.setLine($idd.identity.getLine());} 
            SEMICOLLON;

method returns[MethodDeclaration methodDec] locals[Type tp]: 
            DEF
            (abs=type {$tp = $abs.absType;} 
            | VOID {$tp = new NullType();})
            id=identifier {$methodDec = new MethodDeclaration($id.identity, $tp); $methodDec.setLine($DEF.getLine());} 
            LPAR ma=methodArguments {$methodDec.setArgs($ma.methodArgs);} RPAR
            LBRACE mb=methodBody {$methodDec.setLocalVars($mb.localVars); $methodDec.setBody($mb.body);} RBRACE;

constructor returns[ConstructorDeclaration constructorDec]: 
            DEF id=identifier {$constructorDec = new ConstructorDeclaration($id.identity); $constructorDec.setLine($DEF.getLine());}
            LPAR ma=methodArguments {$constructorDec.setArgs($ma.methodArgs);} RPAR
            LBRACE mb=methodBody {$constructorDec.setLocalVars($mb.localVars); $constructorDec.setBody($mb.body);} RBRACE;

methodArguments returns[ArrayList<VarDeclaration> methodArgs]: 
            {$methodArgs = new ArrayList<>();}
            (vwt1=variableWithType {$methodArgs.add($vwt1.varDecwType);} 
            (COMMA vwt2=variableWithType {$methodArgs.add($vwt2.varDecwType);})*)?;

variableWithType returns[VarDeclaration varDecwType]: 
            vid=identifier
            COLON vtp=type {$varDecwType = new VarDeclaration($vid.identity, $vtp.absType); $varDecwType.setLine($vid.identity.getLine());};

type returns[Type absType]: 
            pdt=primitiveDataType {$absType = $pdt.pType;} 
            | lt=listType {$absType = $lt.list_type;}
            | fpt=functionPointerType {$absType = $fpt.functionPointer;}
            | ct=classType {$absType = $ct.cType;}
            ;

classType returns[ClassType cType]: id=identifier{$cType = new ClassType($id.identity);};

listType returns [ListType list_type]: LIST LPAR 
            ((INT_VALUE SHARP tp=type {$list_type = new ListType($INT_VALUE.getInt(), $tp.absType);})
            | (lits=listItemsTypes {$list_type = new ListType($lits.elementTypes);})) RPAR;

listItemsTypes returns [ArrayList<ListNameType> elementTypes]: 
            {$elementTypes = new ArrayList<>();}
            lit1=listItemType {$elementTypes.add($lit1.listnametype);}
            (COMMA lit2=listItemType {$elementTypes.add($lit2.listnametype);})*;

listItemType returns [ListNameType listnametype]: 
            vwt=variableWithType {$listnametype = new ListNameType($vwt.varDecwType);}
            | tp=type {$listnametype = new ListNameType($tp.absType);};

functionPointerType returns[FptrType functionPointer]: 
            FUNC {$functionPointer = new FptrType();} 
            LESS_THAN 
            (VOID /*NOTE: nothing because list must be empty */| twc=typesWithComma {$functionPointer.setArgumentsTypes($twc.types);}) 
            ARROW (VOID {$functionPointer.setReturnType(new NullType());} | tp=type {$functionPointer.setReturnType($tp.absType);}) 
            GREATER_THAN;

typesWithComma returns[ArrayList<Type> types]:
            {$types = new ArrayList<>();}
            t1=type {$types.add($t1.absType);} 
            (COMMA t2=type {$types.add($t2.absType);} )*;

primitiveDataType returns[Type pType]: 
            INT {$pType = new IntType();} 
            | STRING {$pType = new StringType();}
            | BOOLEAN {$pType = new BoolType();};

methodBody returns[ArrayList<VarDeclaration> localVars, ArrayList<Statement> body]:
            {$localVars = new ArrayList<>(); $body = new ArrayList<>();}
            (varDecs=varDeclaration {$localVars.add($varDecs.varDec);})*
            (stats=statement {$body.add($stats.statemnt);})*;

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

block returns[BlockStmt blockstmnt]: 
            LBRACE {$blockstmnt = new BlockStmt(); $blockstmnt.setLine($LBRACE.getLine());} 
            (stm=statement {$blockstmnt.addStatement($stm.statemnt);})* 
            RBRACE;

assignmentStatement returns[AssignmentStmt assignmentstmnt]: ass=assignment {$assignmentstmnt = $ass.assignments;} SEMICOLLON;

assignment returns[AssignmentStmt assignments]: 
            ox=orExpression
            ASSIGN  ex=expression {$assignments = new AssignmentStmt($ox.expr, $ex.expr); $assignments.setLine($ASSIGN.getLine());};

printStatement returns[PrintStmt printstmnt]: PRINT LPAR 
            exp=expression {$printstmnt = new PrintStmt($exp.expr); $printstmnt.setLine($PRINT.getLine());} 
            RPAR SEMICOLLON;

returnStatement returns[ReturnStmt returnstmnt]: 
            RETURN {$returnstmnt = new ReturnStmt(); $returnstmnt.setLine($RETURN.getLine());} 
            (exp=expression {$returnstmnt.setReturnedExpr($exp.expr);})? 
            SEMICOLLON;

methodCallStatement returns[MethodCallStmt methodcallstmnt]: 
            mc=methodCall {$methodcallstmnt = new MethodCall($mc.mCall); $methodcallstmnt.setLine($mc.mCall.getLine());} SEMICOLLON;

methodCall returns[MethodCall mCall] locals[Expression exp]: 
            oex=otherExpression  {$exp = $oex.otherExpr;} 
            (
            (LPAR mca=methodCallArguments {$exp = new MethodCall($exp, $mca.arguments);} RPAR) 
            | (DOT id=identifier {$exp = new ObjectOrListMemberAccess($exp, $id.identity);}) 
            | (LBRACK ex=expression {$exp = new ListAccessByIndex($exp, $ex.expr);} RBRACK)
            )* 
            (lp=LPAR mca2=methodCallArguments {$mCall = new MethodCall($exp, $mca2.arguments); $mCall.setLine($lp.getLine());} RPAR);

methodCallArguments returns[ArrayList<Expression> arguments]: 
            {$arguments = new ArrayList<>();}
            (expr1=expression {$arguments.add($expr1.expr);} 
            (COMMA expr2=expression {$arguments.add($expr2.expr);})*)?;

continueBreakStatement returns[statement controlstmnt]: 
            (BREAK {$controlstmnt = new BreakStmt(); $controlstmnt.setLine($BREAK.getLine());}
            | CONTINUE {$controlstmnt = new ContinueStmt(); $controlstmnt.setLine($CONTINUE.getLine());}) 
            SEMICOLLON;

forStatement returns[ForStmt forstmnt]: 
            FOR {$forstmnt = new ForStmt(); $forstmnt.setLine($FOR.getLine());} 
            LPAR (ass1=assignment {$forstmnt.setInitialize($ass1.assignments);} )? SEMICOLLON 
            (xp=expression {$forstmnt.setCondition($xp.expr);})? SEMICOLLON 
            (ass2=assignment {$forstmnt.setUpdate($ass2.assignments);})? RPAR 
            stm=statement {$forstmnt.setBody($stm.statemnt);}
            ;

foreachStatement returns[ForeachStmt foreachstmnt]: 
            FOREACH 
            LPAR id=identifier 
            IN exp=expression {$foreachstmnt = new ForeachStmt($id.identity, $exp.expr); $foreachstmnt.setLine($FOREACH.getLine());}
            RPAR stm=statement{$foreachstmnt.setBody($stm.statemnt);}
            ;

ifStatement returns[ConditionalStmt ifelsestmnt]: 
            IF 
            LPAR exp=expression 
            RPAR stm1=statement {$ifelsestmnt = new ConditionalStmt($exp.expr, $stm1.statemnt); $ifelsestmnt.setLine($IF.getLine());}
            (ELSE stm2=statement {$ifelsestmnt.setElseBody($stm2.statemnt);})?
            ;

expression returns[Expression expr]: 
            ore1=orExpression {$expr = $ore1.expr;}
            (ASSIGN ore2=expression {$expr = new BinaryExpression($expr, $ore2.expr, BinaryOperator.assign); $expr.setLine($ASSIGN.getLine());})?;

orExpression returns[Expression expr]: 
            ane1=andExpression {$expr = $ane1.expr;} 
            (OR ane2=andExpression {$expr = new BinaryExpression($expr, $ane2.expr, BinaryOperator.or); $expr.setLine($OR.getLine());})*;

andExpression returns[Expression expr]: 
            eqe1=equalityExpression {$expr = $eqe1.expr;}
            (AND eqe2=equalityExpression {$expr = new BinaryExpression($expr, $eqe2.expr, BinaryOperator.and); $expr.setLine($AND.getLine());})*;

equalityExpression returns[Expression expr] locals[BinaryOperator op, int line]: 
            ree1=relationalExpression {$expr = $ree1.expr;}
            ((EQUAL {$op = BinaryOperator.eq; $line = $EQUAL.getLine();}
            | NOT_EQUAL {$op = BinaryOperator.neq; $line = $NOT_EQUAL.getLine();}) 
            ree2=relationalExpression {$expr = new BinaryExpression($expr, $ree2.expr, $op); $expr.setLine($line);})*;

relationalExpression returns[Expression expr] locals[BinaryOperator op, int line]: 
            ade1=additiveExpression  {$expr = $ade1.expr;}
            ((GREATER_THAN {$op = BinaryOperator.gt; $line= $GREATER_THAN.getLine();}
            | LESS_THAN {$op = BinaryOperator.lt; $line= $LESS_THAN.getLine();}) 
            ade2=additiveExpression {$expr = new BinaryExpression($expr, $ade2.expr, $op); $expr.setLine($line);})*;

additiveExpression returns[Expression expr] locals[BinaryOperator op, int line]: 
            mce1=multiplicativeExpression {$expr = $mce1.expr;}
            ((PLUS {$op = BinaryOperator.add; $line = $PLUS.getLine();} 
            | MINUS {$op = BinaryOperator.sub; $line = $MINUS.getLine();}) 
            mce2=multiplicativeExpression {$expr = new BinaryExpression($expr, $mce2.expr, $op); $expr.setLine($line);})*;

multiplicativeExpression returns[Expression expr] locals[BinaryOperator op, int line]: 
            pue1=preUnaryExpression{$expr = $pue1.expr;} 
            ((MULT {$op = BinaryOperator.mult; $line = $MULT.getLine();} 
            | DIVIDE {$op = BinaryOperator.div; $line = $DIVIDE.getLine();} 
            | MOD {$op = BinaryOperator.mod; $line = $MOD.getLine();}) 
            pue2=preUnaryExpression {$expr = new BinaryExpression($expr, $pue2.expr, $op); $expr.setLine($line);})*;

preUnaryExpression returns[Expression expr] locals[UnaryOperator op, int line]: 
            ((NOT {$op = UnaryOperator.not; $line = $NOT.getLine();} 
            | MINUS {$op = UnaryOperator.minus; $line = $MINUS.getLine();}
            | INCREMENT {$op = UnaryOperator.preinc; $line = $INCREMENT.getLine();}
            | DECREMENT {$op = UnaryOperator.predec; $line = $DECREMENT.getLine();}) 
            preue=preUnaryExpression {$expr = new UnaryExpression($preue.expr, $op); $expr.setLine($line);}) 
            | postue=postUnaryExpression {$expr = $postue.expr;};

postUnaryExpression returns[Expression expr]: aex=accessExpression {$expr = $aex.exp;}
            (
            INCREMENT {$expr = new UnaryExpression($expr, UnaryOperator.postinc); $expr.setLine($INCREMENT.getLine());} 
            | DECREMENT {$expr = new UnaryExpression($expr, UnaryOperator.postdec); $expr.setLine($DECREMENT.getLine());}
            )?
            ;

accessExpression returns[Expression exp]: 
            oex=otherExpression {$exp = $oex.otherExpr;} 
            ((LPAR mca=methodCallArguments {$exp = new MethodCall($exp, $mca.arguments); $exp.setLine($LPAR.getLine());}RPAR)  
            | (DOT id=identifier {$exp = new ObjectOrListMemberAccess($exp, $id.identity); $exp.setLine($id.identity.getLine());}) 
            | (LBRACK ex=expression {$exp = new ListAccessByIndex($exp, $ex.expr); $exp.setLine($LBRACK.getLine());} RBRACK))*;
//TODO: check if lpar expr rpar should get line from elsewhere
otherExpression returns[Expression otherExpr]: 
            THIS {$otherExpr = new ThisClass(); $otherExpr.setLine($THIS.getLine());} 
            | ne=newExpression {$otherExpr = $ne.classInstance;}
            | vs=values {$otherExpr = $vs.val;} 
            | identi=identifier {$otherExpr = $identi.identity;}
            | LPAR (expp=expression {$otherExpr = $expp.expr;}) RPAR;

newExpression returns[NewClassInstance classInstance]: NEW 
            ct=classType {$classInstance = new NewClassInstance($ct.cType); $classInstance.setLine($NEW.getLine());} 
            LPAR mca=methodCallArguments {$classInstance.setArgs($mca.arguments);} 
            RPAR
            ;

values returns[Value val]: bv=boolValue {$val = $bv.boolVal;} 
            | STRING_VALUE {$val = new StringValue($STRING_VALUE.getText()); $val.setLine($STRING_VALUE.getLine());} 
            | INT_VALUE {$val = new IntValue($INT_VALUE.getInt()); $val.setLine($INT_VALUE.getLine());} 
            | NULL {$val = new NullValue(); $val.setLine($NULL.getLine());} 
            | lv=listValue {$val = $lv.listVal;}
            ;

boolValue returns[BoolValue boolVal]: TRUE {$boolVal = new BoolValue(true); $boolVal.setLine($TRUE.getLine());} 
            | FALSE {$boolVal = new BoolValue(false); $boolVal.setLine($FALSE.getLine());} ;

listValue returns[ListValue listVal]: LBRACK {$listVal = new ListValue(); $listVal.setLine($LBRACK.getLine());} 
            mca=methodCallArguments {$listVal.setElements($mca.arguments);} RBRACK;

identifier returns[Identifier identity]: IDENTIFIER 
            {$identity = new Identifier($IDENTIFIER.getText()); $identity.setLine($IDENTIFIER.getLine());};

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