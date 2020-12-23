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

sophia returns[Program sophiaProgram]:
    p=program
    { $sophiaProgram = $p.programRet; }
    EOF
    ;

program returns[Program programRet]:
    {
        $programRet = new Program();
        $programRet.setLine(1);
    }
    (
        c=sophiaClass
        { $programRet.addClass($c.sophiaClassRet); }
    )*
    ;

sophiaClass returns[ClassDeclaration sophiaClassRet]:
    cl=CLASS name=identifier
    {
        $sophiaClassRet = new ClassDeclaration($name.idRet);
        $sophiaClassRet.setLine($cl.getLine());
    }
    (
        EXTENDS parentName=identifier
        { $sophiaClassRet.setParentClassName($parentName.idRet); }
    )?
    LBRACE (((v1=varDeclaration
    {
        FieldDeclaration f1 = new FieldDeclaration($v1.varDeclarationRet);
        f1.setLine($v1.line);
        $sophiaClassRet.addField(f1);
    }
    | m1=method
    { $sophiaClassRet.addMethod($m1.methodRet); }
    )*
    (c=constructor
    { $sophiaClassRet.setConstructor($c.constructorRet); }
    )
    (v2=varDeclaration
    {
        FieldDeclaration f2 = new FieldDeclaration($v2.varDeclarationRet);
        f2.setLine($v2.line);
        $sophiaClassRet.addField(f2);
    }
    | m2=method
    { $sophiaClassRet.addMethod($m2.methodRet); }
    )*)
    | (
    (v2=varDeclaration
    {
        FieldDeclaration f2 = new FieldDeclaration($v2.varDeclarationRet);
        f2.setLine($v2.line);
        $sophiaClassRet.addField(f2);
    }
    | m2=method
    { $sophiaClassRet.addMethod($m2.methodRet); }
    )*
    ))
    RBRACE
    ;

varDeclaration returns[VarDeclaration varDeclarationRet, int line]:
    id=identifier COLON t=type
    {
        $varDeclarationRet = new VarDeclaration($id.idRet, $t.typeRet);
        $varDeclarationRet.setLine($id.line);
        $line = $id.line;
    }
    SEMICOLLON
    ;

method returns[MethodDeclaration methodRet]
    locals[Type returnType]:
    d=DEF ((t=type
    { $returnType = $t.typeRet; }
    ) | (VOID
    { $returnType = new NullType(); }
    ))
    name=identifier
    {
        $methodRet = new MethodDeclaration($name.idRet, $returnType);
        $methodRet.setLine($d.getLine());
    }
    LPAR args=methodArguments
    { $methodRet.setArgs($args.argsRet); }
    RPAR LBRACE body=methodBody
    {
        $methodRet.setLocalVars($body.localVars);
        $methodRet.setBody($body.statements);
    }
    RBRACE
    ;

constructor returns[ConstructorDeclaration constructorRet]:
    d=DEF name=identifier
    {
        $constructorRet = new ConstructorDeclaration($name.idRet);
        $constructorRet.setLine($d.getLine());
    }
    LPAR args=methodArguments
    { $constructorRet.setArgs($args.argsRet); }
    RPAR LBRACE
    body=methodBody
    {
        $constructorRet.setLocalVars($body.localVars);
        $constructorRet.setBody($body.statements);
    }
    RBRACE
    ;

methodArguments returns[ArrayList<VarDeclaration> argsRet]:
    { $argsRet = new ArrayList<>(); }
    (v1=variableWithType
    { $argsRet.add($v1.varWithTypeRet); }
    (COMMA v2=variableWithType
    { $argsRet.add($v2.varWithTypeRet); }
    )*
    )?
    ;

variableWithType returns[VarDeclaration varWithTypeRet]:
    id=identifier COLON t=type
    {
        $varWithTypeRet = new VarDeclaration($id.idRet, $t.typeRet);
        $varWithTypeRet.setLine($id.line);
    }
    ;

type returns[Type typeRet]:
    p=primitiveDataType
    { $typeRet = $p.primitiveTypeRet; }
    | l=listType
    { $typeRet = $l.listTypeRet; }
    | f=functionPointerType
    { $typeRet = $f.fptrTypeRet; }
    | c=classType
    { $typeRet = $c.classTypeRet; }
    ;

classType returns[ClassType classTypeRet]:
    id=identifier
    { $classTypeRet = new ClassType($id.idRet); }
    ;

listType returns[ListType listTypeRet]:
    l=LIST LPAR (
    (num=INT_VALUE SHARP t=type
    { $listTypeRet = new ListType($num.int, new ListNameType($t.typeRet)); }
    )
    | (typesList=listItemsTypes
    { $listTypeRet = new ListType($typesList.listItemsTypesRet); }
    )
    ) RPAR
    ;

listItemsTypes returns[ArrayList<ListNameType> listItemsTypesRet]:
    { $listItemsTypesRet = new ArrayList<>(); }
    l1=listItemType
    { $listItemsTypesRet.add($l1.listItemTypeRet); }
    (COMMA l2=listItemType
    { $listItemsTypesRet.add($l2.listItemTypeRet); }
    )*
    ;

listItemType returns[ListNameType listItemTypeRet]:
    v=variableWithType
    { $listItemTypeRet = new ListNameType($v.varWithTypeRet); }
    | t=type
    { $listItemTypeRet = new ListNameType($t.typeRet); }
    ;

functionPointerType returns[FptrType fptrTypeRet]:
    FUNC
    { $fptrTypeRet = new FptrType(); }
    LESS_THAN
    (VOID
    { $fptrTypeRet.setArgumentsTypes(new ArrayList<Type>()); }
    | types=typesWithComma
    { $fptrTypeRet.setArgumentsTypes($types.typesWithCommaRet); }
    ) ARROW
    (VOID
    { $fptrTypeRet.setReturnType(new NullType()); }
    | t=type
    { $fptrTypeRet.setReturnType($t.typeRet); }
    ) GREATER_THAN
    ;

typesWithComma returns[ArrayList<Type> typesWithCommaRet]:
    { $typesWithCommaRet = new ArrayList<>(); }
    t1=type
    { $typesWithCommaRet.add($t1.typeRet); }
    (COMMA t2=type
    { $typesWithCommaRet.add($t2.typeRet); }
    )*
    ;

primitiveDataType returns[Type primitiveTypeRet]:
    INT
    { $primitiveTypeRet = new IntType(); }
    | STRING
    { $primitiveTypeRet = new StringType(); }
    | BOOLEAN
    { $primitiveTypeRet = new BoolType(); }
    ;

methodBody returns[ArrayList<VarDeclaration> localVars, ArrayList<Statement> statements]:
    {
        $localVars = new ArrayList<>();
        $statements = new ArrayList<>();
    }
    (v=varDeclaration
        { $localVars.add($v.varDeclarationRet); }
    )*
    (s=statement
        { $statements.add($s.sRet); }
    )*
    ;

statement returns[Statement sRet]:
    f1=forStatement
    { $sRet = $f1.forStmtRet; }
    | f2=foreachStatement
    { $sRet = $f2.foreachStmtRet; }
    | i=ifStatement
    { $sRet = $i.ifStmtRet; }
    | a=assignmentStatement
    { $sRet = $a.assignStmtRet; }
    | p=printStatement
    { $sRet = $p.printStmtRet; }
    | c=continueBreakStatement
    { $sRet = $c.continueBreakRet; }
    | m=methodCallStatement
    { $sRet = $m.methodCallStmtRet; }
    | r=returnStatement
    { $sRet = $r.returnStmtRet; }
    | b=block
    { $sRet = $b.blockRet; }
    ;

block returns[BlockStmt blockRet]:
    l=LBRACE
    {
        $blockRet = new BlockStmt();
        $blockRet.setLine($l.getLine());
    }
    (s=statement
        { $blockRet.addStatement($s.sRet); }
    )* RBRACE
    ;

assignmentStatement returns[AssignmentStmt assignStmtRet]:
    a=assignment
    { $assignStmtRet = $a.assignmentRet; }
    SEMICOLLON
    ;

assignment returns[AssignmentStmt assignmentRet]:
    left=orExpression a=ASSIGN right=expression
    {
        $assignmentRet = new AssignmentStmt($left.orExprRet, $right.exprRet);
        $assignmentRet.setLine($a.getLine());
    }
    ;

printStatement returns[PrintStmt printStmtRet]:
    p=PRINT LPAR
    e=expression
    {
        $printStmtRet = new PrintStmt($e.exprRet);
        $printStmtRet.setLine($p.getLine());
    }
    RPAR SEMICOLLON
    ;

returnStatement returns[ReturnStmt returnStmtRet]:
    r=RETURN
    {
        $returnStmtRet = new ReturnStmt();
        $returnStmtRet.setLine($r.getLine());
    }
    (e=expression
    { $returnStmtRet.setReturnedExpr($e.exprRet); }
    )?
    {
        if($returnStmtRet.getReturnedExpr() instanceof NullValue) {
            NullValue newNullValue = new NullValue();
            newNullValue.setLine($r.getLine());
            $returnStmtRet.setReturnedExpr(newNullValue);
        }
    }
    SEMICOLLON;

methodCallStatement returns[Statement methodCallStmtRet]:
    m=methodCall
    { $methodCallStmtRet = $m.methodCallRet; }
    SEMICOLLON
    ;

methodCall returns[Statement methodCallRet]:
    ae=accessExpression
    (l=LPAR m2=methodCallArguments
    {
        MethodCall methodCall = new MethodCall($ae.accessExprRet, $m2.methodCallArgsRet);
        methodCall.setLine($l.line);
        $methodCallRet = new MethodCallStmt(methodCall);
        $methodCallRet.setLine($l.line);
    }
    RPAR)
    ;

methodCallArguments returns[ArrayList<Expression> methodCallArgsRet]:
    { $methodCallArgsRet = new ArrayList<>(); }
    (e1=expression
    { $methodCallArgsRet.add($e1.exprRet); }
    (COMMA e2=expression
    { $methodCallArgsRet.add($e2.exprRet); }
    )*
    )?
    ;

continueBreakStatement returns[Statement continueBreakRet]:
    (b=BREAK
    {
        $continueBreakRet = new BreakStmt();
        $continueBreakRet.setLine($b.getLine());
    }
    | c=CONTINUE
    {
        $continueBreakRet = new ContinueStmt();
        $continueBreakRet.setLine($c.getLine());
    }
    ) SEMICOLLON;

forStatement returns[ForStmt forStmtRet]:
    f=FOR
    {
        $forStmtRet = new ForStmt();
        $forStmtRet.setLine($f.getLine());
    }
    LPAR (init=assignment
    { $forStmtRet.setInitialize($init.assignmentRet); }
    )? SEMICOLLON
    (cond=expression
    { $forStmtRet.setCondition($cond.exprRet); }
    )? SEMICOLLON
    (update=assignment
    { $forStmtRet.setUpdate($update.assignmentRet); }
    )? RPAR body=statement
    { $forStmtRet.setBody($body.sRet); }
    ;

foreachStatement returns[ForeachStmt foreachStmtRet]:
    f=FOREACH LPAR id=identifier IN list=expression
    {
        $foreachStmtRet = new ForeachStmt($id.idRet, $list.exprRet);
        $foreachStmtRet.setLine($f.getLine());
    }
    RPAR body=statement
    { $foreachStmtRet.setBody($body.sRet); }
    ;

ifStatement returns[ConditionalStmt ifStmtRet]:
    i=IF LPAR e=expression RPAR thenBody=statement
    {
        $ifStmtRet = new ConditionalStmt($e.exprRet, $thenBody.sRet);
        $ifStmtRet.setLine($i.getLine());
    }
    (ELSE elseBody=statement
    { $ifStmtRet.setElseBody($elseBody.sRet); }
    )?
    ;

expression returns[Expression exprRet]:
    oe=orExpression
    { $exprRet = $oe.orExprRet; }
    (a=ASSIGN e=expression
    {
        BinaryOperator op = BinaryOperator.assign;
        $exprRet = new BinaryExpression($oe.orExprRet, $e.exprRet, op);
        $exprRet.setLine($a.getLine());
    }
    )?
    ;

orExpression returns[Expression orExprRet]:
    ael=andExpression
    { $orExprRet = $ael.andExprRet; }
    (o=OR aer=andExpression
    {
        BinaryOperator op = BinaryOperator.or;
        $orExprRet = new BinaryExpression($orExprRet, $aer.andExprRet, op);
        $orExprRet.setLine($o.getLine());
    }
    )*
    ;

andExpression returns[Expression andExprRet]:
    eel=equalityExpression
    { $andExprRet = $eel.eqExprRet; }
    (a=AND ee2=equalityExpression
    {
        BinaryOperator op = BinaryOperator.and;
        $andExprRet = new BinaryExpression($andExprRet, $ee2.eqExprRet, op);
        $andExprRet.setLine($a.getLine());
    }
    )*
    ;

equalityExpression returns[Expression eqExprRet]
    locals[BinaryOperator op, int line]:
    rel=relationalExpression
    { $eqExprRet = $rel.relExprRet; }
    ((eq=EQUAL
    {
        $op = BinaryOperator.eq;
        $line = $eq.getLine();
    }
    | neq=NOT_EQUAL
    {
        $op = BinaryOperator.neq;
        $line = $neq.getLine();
    }
    ) rer=relationalExpression
    {
        $eqExprRet = new BinaryExpression($eqExprRet, $rer.relExprRet, $op);
        $eqExprRet.setLine($line);
    }
    )*
    ;

relationalExpression returns[Expression relExprRet]
    locals[BinaryOperator op, int line]:
    ael=additiveExpression
    { $relExprRet = $ael.addExprRet; }
    ((gt=GREATER_THAN
    {
        $op = BinaryOperator.gt;
        $line = $gt.getLine();
    }
    | lt=LESS_THAN
    {
        $op = BinaryOperator.lt;
        $line = $lt.getLine();
    }
    ) aer=additiveExpression
    {
        $relExprRet = new BinaryExpression($relExprRet, $aer.addExprRet, $op);
        $relExprRet.setLine($line);
    }
    )*
    ;

additiveExpression returns[Expression addExprRet]
    locals[BinaryOperator op, int line]:
    mel=multiplicativeExpression
    { $addExprRet = $mel.multExprRet; }
    ((add=PLUS
    {
        $op = BinaryOperator.add;
        $line = $add.getLine();
    }
    | sub=MINUS
    {
        $op = BinaryOperator.sub;
        $line = $sub.getLine();
    }
    ) mer=multiplicativeExpression
    {
        $addExprRet = new BinaryExpression($addExprRet, $mer.multExprRet, $op);
        $addExprRet.setLine($line);
    }
    )*
    ;

multiplicativeExpression returns[Expression multExprRet]
    locals[BinaryOperator op, int line]:
    pel=preUnaryExpression
    { $multExprRet = $pel.preUnaryExprRet; }
    ((mult=MULT
    {
        $op = BinaryOperator.mult;
        $line = $mult.getLine();
    }
    | div=DIVIDE
    {
        $op = BinaryOperator.div;
        $line = $div.getLine();
    }
    | mod=MOD
    {
        $op = BinaryOperator.mod;
        $line = $mod.getLine();
    }
    ) per=preUnaryExpression
    {
        $multExprRet = new BinaryExpression($multExprRet, $per.preUnaryExprRet, $op);
        $multExprRet.setLine($line);
    }
    )*
    ;

preUnaryExpression returns[Expression preUnaryExprRet]
    locals[UnaryOperator op, int line]:
    (not=NOT
    {
        $op = UnaryOperator.not;
        $line = $not.getLine();
    }
    | minus=MINUS
    {
        $op = UnaryOperator.minus;
        $line = $minus.getLine();
    }
    | preinc=INCREMENT
    {
        $op = UnaryOperator.preinc;
        $line = $preinc.getLine();
    }
    | predec=DECREMENT
    {
        $op = UnaryOperator.predec;
        $line = $predec.getLine();
    }
    ) pre=preUnaryExpression
    {
        $preUnaryExprRet = new UnaryExpression($pre.preUnaryExprRet, $op);
        $preUnaryExprRet.setLine($line);
    }
    | post=postUnaryExpression
    { $preUnaryExprRet = $post.postUnaryExprRet; }
    ;

postUnaryExpression returns[Expression postUnaryExprRet]:
    ae=accessExpression
    { $postUnaryExprRet = $ae.accessExprRet; }
    (postinc=INCREMENT
    {
        UnaryOperator op = UnaryOperator.postinc;
        $postUnaryExprRet = new UnaryExpression($postUnaryExprRet, op);
        $postUnaryExprRet.setLine($postinc.getLine());
    }
    | postdec=DECREMENT
    {
        UnaryOperator op = UnaryOperator.postdec;
        $postUnaryExprRet = new UnaryExpression($postUnaryExprRet, op);
        $postUnaryExprRet.setLine($postdec.getLine());
    }
    )?
    ;

accessExpression returns[Expression accessExprRet]:
    oe=otherExpression
    { $accessExprRet = $oe.otherExprRet; }
    (
    (l=LPAR m=methodCallArguments
    {
        $accessExprRet = new MethodCall($accessExprRet, $m.methodCallArgsRet);
        $accessExprRet.setLine($l.line);
    }
    RPAR)
    | (DOT i=identifier)
    {
        $accessExprRet = new ObjectOrListMemberAccess($accessExprRet, $i.idRet);
        $accessExprRet.setLine($i.line);
    }
    | (l=LBRACK index=expression RBRACK)
    {
        $accessExprRet = new ListAccessByIndex($accessExprRet, $index.exprRet);
        $accessExprRet.setLine($l.getLine());
    }
    )*
    ;

otherExpression returns[Expression otherExprRet]:
    t=THIS
    {
        $otherExprRet = new ThisClass();
        $otherExprRet.setLine($t.getLine());
    }
    | n=newExpression
    { $otherExprRet = $n.newExprRet; }
    | v=values
    { $otherExprRet = $v.valuesRet; }
    | id=identifier
    { $otherExprRet = $id.idRet; }
    | LPAR (e=expression) RPAR
    { $otherExprRet = $e.exprRet; }
    ;

newExpression returns[NewClassInstance newExprRet]:
    n=NEW c=classType LPAR m=methodCallArguments RPAR
    {
        $newExprRet = new NewClassInstance($c.classTypeRet, $m.methodCallArgsRet);
        $newExprRet.setLine($n.getLine());
    }
    ;

values returns[Value valuesRet]:
    b=boolValue
    {
        $valuesRet = new BoolValue($b.boolValueRet);
        $valuesRet.setLine($b.line);
    }
    | s=STRING_VALUE
    {
        $valuesRet = new StringValue(($s.text).substring(1, ($s.text).length()-1));
        $valuesRet.setLine($s.getLine());
    }
    | i=INT_VALUE
    {
        $valuesRet = new IntValue($i.int);
        $valuesRet.setLine($i.getLine());
    }
    | n=NULL
    {
        $valuesRet = new NullValue();
        $valuesRet.setLine($n.getLine());
    }
    | l=listValue
    { $valuesRet = $l.listValueRet; }
    ;

boolValue returns[boolean boolValueRet, int line]:
    t=TRUE
    {
        $boolValueRet = true;
        $line = $t.getLine();
    }
    | f=FALSE
    {
        $boolValueRet = false;
        $line = $f.getLine();
    }
    ;

listValue returns[ListValue listValueRet]:
    l=LBRACK m=methodCallArguments
    {
        $listValueRet = new ListValue($m.methodCallArgsRet);
        $listValueRet.setLine($l.getLine());
    }
    RBRACK
    ;

identifier returns[Identifier idRet, int line]:
    id=IDENTIFIER
    {
        $idRet = new Identifier($id.text);
        $idRet.setLine($id.getLine());
        $line = $id.getLine();
    }
    ;


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