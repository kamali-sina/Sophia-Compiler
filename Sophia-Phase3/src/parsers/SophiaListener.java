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

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link SophiaParser}.
 */
public interface SophiaListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link SophiaParser#sophia}.
	 * @param ctx the parse tree
	 */
	void enterSophia(SophiaParser.SophiaContext ctx);
	/**
	 * Exit a parse tree produced by {@link SophiaParser#sophia}.
	 * @param ctx the parse tree
	 */
	void exitSophia(SophiaParser.SophiaContext ctx);
	/**
	 * Enter a parse tree produced by {@link SophiaParser#program}.
	 * @param ctx the parse tree
	 */
	void enterProgram(SophiaParser.ProgramContext ctx);
	/**
	 * Exit a parse tree produced by {@link SophiaParser#program}.
	 * @param ctx the parse tree
	 */
	void exitProgram(SophiaParser.ProgramContext ctx);
	/**
	 * Enter a parse tree produced by {@link SophiaParser#sophiaClass}.
	 * @param ctx the parse tree
	 */
	void enterSophiaClass(SophiaParser.SophiaClassContext ctx);
	/**
	 * Exit a parse tree produced by {@link SophiaParser#sophiaClass}.
	 * @param ctx the parse tree
	 */
	void exitSophiaClass(SophiaParser.SophiaClassContext ctx);
	/**
	 * Enter a parse tree produced by {@link SophiaParser#varDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterVarDeclaration(SophiaParser.VarDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link SophiaParser#varDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitVarDeclaration(SophiaParser.VarDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link SophiaParser#method}.
	 * @param ctx the parse tree
	 */
	void enterMethod(SophiaParser.MethodContext ctx);
	/**
	 * Exit a parse tree produced by {@link SophiaParser#method}.
	 * @param ctx the parse tree
	 */
	void exitMethod(SophiaParser.MethodContext ctx);
	/**
	 * Enter a parse tree produced by {@link SophiaParser#constructor}.
	 * @param ctx the parse tree
	 */
	void enterConstructor(SophiaParser.ConstructorContext ctx);
	/**
	 * Exit a parse tree produced by {@link SophiaParser#constructor}.
	 * @param ctx the parse tree
	 */
	void exitConstructor(SophiaParser.ConstructorContext ctx);
	/**
	 * Enter a parse tree produced by {@link SophiaParser#methodArguments}.
	 * @param ctx the parse tree
	 */
	void enterMethodArguments(SophiaParser.MethodArgumentsContext ctx);
	/**
	 * Exit a parse tree produced by {@link SophiaParser#methodArguments}.
	 * @param ctx the parse tree
	 */
	void exitMethodArguments(SophiaParser.MethodArgumentsContext ctx);
	/**
	 * Enter a parse tree produced by {@link SophiaParser#variableWithType}.
	 * @param ctx the parse tree
	 */
	void enterVariableWithType(SophiaParser.VariableWithTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link SophiaParser#variableWithType}.
	 * @param ctx the parse tree
	 */
	void exitVariableWithType(SophiaParser.VariableWithTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link SophiaParser#type}.
	 * @param ctx the parse tree
	 */
	void enterType(SophiaParser.TypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link SophiaParser#type}.
	 * @param ctx the parse tree
	 */
	void exitType(SophiaParser.TypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link SophiaParser#classType}.
	 * @param ctx the parse tree
	 */
	void enterClassType(SophiaParser.ClassTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link SophiaParser#classType}.
	 * @param ctx the parse tree
	 */
	void exitClassType(SophiaParser.ClassTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link SophiaParser#listType}.
	 * @param ctx the parse tree
	 */
	void enterListType(SophiaParser.ListTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link SophiaParser#listType}.
	 * @param ctx the parse tree
	 */
	void exitListType(SophiaParser.ListTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link SophiaParser#listItemsTypes}.
	 * @param ctx the parse tree
	 */
	void enterListItemsTypes(SophiaParser.ListItemsTypesContext ctx);
	/**
	 * Exit a parse tree produced by {@link SophiaParser#listItemsTypes}.
	 * @param ctx the parse tree
	 */
	void exitListItemsTypes(SophiaParser.ListItemsTypesContext ctx);
	/**
	 * Enter a parse tree produced by {@link SophiaParser#listItemType}.
	 * @param ctx the parse tree
	 */
	void enterListItemType(SophiaParser.ListItemTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link SophiaParser#listItemType}.
	 * @param ctx the parse tree
	 */
	void exitListItemType(SophiaParser.ListItemTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link SophiaParser#functionPointerType}.
	 * @param ctx the parse tree
	 */
	void enterFunctionPointerType(SophiaParser.FunctionPointerTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link SophiaParser#functionPointerType}.
	 * @param ctx the parse tree
	 */
	void exitFunctionPointerType(SophiaParser.FunctionPointerTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link SophiaParser#typesWithComma}.
	 * @param ctx the parse tree
	 */
	void enterTypesWithComma(SophiaParser.TypesWithCommaContext ctx);
	/**
	 * Exit a parse tree produced by {@link SophiaParser#typesWithComma}.
	 * @param ctx the parse tree
	 */
	void exitTypesWithComma(SophiaParser.TypesWithCommaContext ctx);
	/**
	 * Enter a parse tree produced by {@link SophiaParser#primitiveDataType}.
	 * @param ctx the parse tree
	 */
	void enterPrimitiveDataType(SophiaParser.PrimitiveDataTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link SophiaParser#primitiveDataType}.
	 * @param ctx the parse tree
	 */
	void exitPrimitiveDataType(SophiaParser.PrimitiveDataTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link SophiaParser#methodBody}.
	 * @param ctx the parse tree
	 */
	void enterMethodBody(SophiaParser.MethodBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link SophiaParser#methodBody}.
	 * @param ctx the parse tree
	 */
	void exitMethodBody(SophiaParser.MethodBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link SophiaParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatement(SophiaParser.StatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SophiaParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatement(SophiaParser.StatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SophiaParser#block}.
	 * @param ctx the parse tree
	 */
	void enterBlock(SophiaParser.BlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link SophiaParser#block}.
	 * @param ctx the parse tree
	 */
	void exitBlock(SophiaParser.BlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link SophiaParser#assignmentStatement}.
	 * @param ctx the parse tree
	 */
	void enterAssignmentStatement(SophiaParser.AssignmentStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SophiaParser#assignmentStatement}.
	 * @param ctx the parse tree
	 */
	void exitAssignmentStatement(SophiaParser.AssignmentStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SophiaParser#assignment}.
	 * @param ctx the parse tree
	 */
	void enterAssignment(SophiaParser.AssignmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link SophiaParser#assignment}.
	 * @param ctx the parse tree
	 */
	void exitAssignment(SophiaParser.AssignmentContext ctx);
	/**
	 * Enter a parse tree produced by {@link SophiaParser#printStatement}.
	 * @param ctx the parse tree
	 */
	void enterPrintStatement(SophiaParser.PrintStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SophiaParser#printStatement}.
	 * @param ctx the parse tree
	 */
	void exitPrintStatement(SophiaParser.PrintStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SophiaParser#returnStatement}.
	 * @param ctx the parse tree
	 */
	void enterReturnStatement(SophiaParser.ReturnStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SophiaParser#returnStatement}.
	 * @param ctx the parse tree
	 */
	void exitReturnStatement(SophiaParser.ReturnStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SophiaParser#methodCallStatement}.
	 * @param ctx the parse tree
	 */
	void enterMethodCallStatement(SophiaParser.MethodCallStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SophiaParser#methodCallStatement}.
	 * @param ctx the parse tree
	 */
	void exitMethodCallStatement(SophiaParser.MethodCallStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SophiaParser#methodCall}.
	 * @param ctx the parse tree
	 */
	void enterMethodCall(SophiaParser.MethodCallContext ctx);
	/**
	 * Exit a parse tree produced by {@link SophiaParser#methodCall}.
	 * @param ctx the parse tree
	 */
	void exitMethodCall(SophiaParser.MethodCallContext ctx);
	/**
	 * Enter a parse tree produced by {@link SophiaParser#methodCallArguments}.
	 * @param ctx the parse tree
	 */
	void enterMethodCallArguments(SophiaParser.MethodCallArgumentsContext ctx);
	/**
	 * Exit a parse tree produced by {@link SophiaParser#methodCallArguments}.
	 * @param ctx the parse tree
	 */
	void exitMethodCallArguments(SophiaParser.MethodCallArgumentsContext ctx);
	/**
	 * Enter a parse tree produced by {@link SophiaParser#continueBreakStatement}.
	 * @param ctx the parse tree
	 */
	void enterContinueBreakStatement(SophiaParser.ContinueBreakStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SophiaParser#continueBreakStatement}.
	 * @param ctx the parse tree
	 */
	void exitContinueBreakStatement(SophiaParser.ContinueBreakStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SophiaParser#forStatement}.
	 * @param ctx the parse tree
	 */
	void enterForStatement(SophiaParser.ForStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SophiaParser#forStatement}.
	 * @param ctx the parse tree
	 */
	void exitForStatement(SophiaParser.ForStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SophiaParser#foreachStatement}.
	 * @param ctx the parse tree
	 */
	void enterForeachStatement(SophiaParser.ForeachStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SophiaParser#foreachStatement}.
	 * @param ctx the parse tree
	 */
	void exitForeachStatement(SophiaParser.ForeachStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SophiaParser#ifStatement}.
	 * @param ctx the parse tree
	 */
	void enterIfStatement(SophiaParser.IfStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SophiaParser#ifStatement}.
	 * @param ctx the parse tree
	 */
	void exitIfStatement(SophiaParser.IfStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SophiaParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression(SophiaParser.ExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SophiaParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression(SophiaParser.ExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SophiaParser#orExpression}.
	 * @param ctx the parse tree
	 */
	void enterOrExpression(SophiaParser.OrExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SophiaParser#orExpression}.
	 * @param ctx the parse tree
	 */
	void exitOrExpression(SophiaParser.OrExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SophiaParser#andExpression}.
	 * @param ctx the parse tree
	 */
	void enterAndExpression(SophiaParser.AndExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SophiaParser#andExpression}.
	 * @param ctx the parse tree
	 */
	void exitAndExpression(SophiaParser.AndExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SophiaParser#equalityExpression}.
	 * @param ctx the parse tree
	 */
	void enterEqualityExpression(SophiaParser.EqualityExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SophiaParser#equalityExpression}.
	 * @param ctx the parse tree
	 */
	void exitEqualityExpression(SophiaParser.EqualityExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SophiaParser#relationalExpression}.
	 * @param ctx the parse tree
	 */
	void enterRelationalExpression(SophiaParser.RelationalExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SophiaParser#relationalExpression}.
	 * @param ctx the parse tree
	 */
	void exitRelationalExpression(SophiaParser.RelationalExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SophiaParser#additiveExpression}.
	 * @param ctx the parse tree
	 */
	void enterAdditiveExpression(SophiaParser.AdditiveExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SophiaParser#additiveExpression}.
	 * @param ctx the parse tree
	 */
	void exitAdditiveExpression(SophiaParser.AdditiveExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SophiaParser#multiplicativeExpression}.
	 * @param ctx the parse tree
	 */
	void enterMultiplicativeExpression(SophiaParser.MultiplicativeExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SophiaParser#multiplicativeExpression}.
	 * @param ctx the parse tree
	 */
	void exitMultiplicativeExpression(SophiaParser.MultiplicativeExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SophiaParser#preUnaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterPreUnaryExpression(SophiaParser.PreUnaryExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SophiaParser#preUnaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitPreUnaryExpression(SophiaParser.PreUnaryExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SophiaParser#postUnaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterPostUnaryExpression(SophiaParser.PostUnaryExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SophiaParser#postUnaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitPostUnaryExpression(SophiaParser.PostUnaryExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SophiaParser#accessExpression}.
	 * @param ctx the parse tree
	 */
	void enterAccessExpression(SophiaParser.AccessExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SophiaParser#accessExpression}.
	 * @param ctx the parse tree
	 */
	void exitAccessExpression(SophiaParser.AccessExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SophiaParser#otherExpression}.
	 * @param ctx the parse tree
	 */
	void enterOtherExpression(SophiaParser.OtherExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SophiaParser#otherExpression}.
	 * @param ctx the parse tree
	 */
	void exitOtherExpression(SophiaParser.OtherExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SophiaParser#newExpression}.
	 * @param ctx the parse tree
	 */
	void enterNewExpression(SophiaParser.NewExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SophiaParser#newExpression}.
	 * @param ctx the parse tree
	 */
	void exitNewExpression(SophiaParser.NewExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SophiaParser#values}.
	 * @param ctx the parse tree
	 */
	void enterValues(SophiaParser.ValuesContext ctx);
	/**
	 * Exit a parse tree produced by {@link SophiaParser#values}.
	 * @param ctx the parse tree
	 */
	void exitValues(SophiaParser.ValuesContext ctx);
	/**
	 * Enter a parse tree produced by {@link SophiaParser#boolValue}.
	 * @param ctx the parse tree
	 */
	void enterBoolValue(SophiaParser.BoolValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link SophiaParser#boolValue}.
	 * @param ctx the parse tree
	 */
	void exitBoolValue(SophiaParser.BoolValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link SophiaParser#listValue}.
	 * @param ctx the parse tree
	 */
	void enterListValue(SophiaParser.ListValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link SophiaParser#listValue}.
	 * @param ctx the parse tree
	 */
	void exitListValue(SophiaParser.ListValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link SophiaParser#identifier}.
	 * @param ctx the parse tree
	 */
	void enterIdentifier(SophiaParser.IdentifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link SophiaParser#identifier}.
	 * @param ctx the parse tree
	 */
	void exitIdentifier(SophiaParser.IdentifierContext ctx);
}