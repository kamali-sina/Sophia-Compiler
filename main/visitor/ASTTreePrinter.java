package main.visitor;

import main.ast.nodes.Program;
import main.ast.nodes.declaration.classDec.ClassDeclaration;
import main.ast.nodes.declaration.classDec.classMembersDec.ConstructorDeclaration;
import main.ast.nodes.declaration.classDec.classMembersDec.FieldDeclaration;
import main.ast.nodes.declaration.classDec.classMembersDec.MethodDeclaration;
import main.ast.nodes.declaration.variableDec.VarDeclaration;
import main.ast.nodes.expression.*;
import main.ast.nodes.expression.values.ListValue;
import main.ast.nodes.expression.values.NullValue;
import main.ast.nodes.expression.values.primitive.BoolValue;
import main.ast.nodes.expression.values.primitive.IntValue;
import main.ast.nodes.expression.values.primitive.StringValue;
import main.ast.nodes.statement.*;
import main.ast.nodes.statement.loop.BreakStmt;
import main.ast.nodes.statement.loop.ContinueStmt;
import main.ast.nodes.statement.loop.ForStmt;
import main.ast.nodes.statement.loop.ForeachStmt;

public class ASTTreePrinter extends Visitor<Void> {

    @Override
    public Void visit(Program program) {
        {System.out.println("Line:"+program.getLine()+":"+program.toString());}
        for (ClassDeclaration programClass : program.getClasses()) {
            programClass.accept(this);
        }
        return null;
    }

    @Override
    public Void visit(ClassDeclaration classDeclaration) {
        {System.out.println("Line:"+classDeclaration.getLine()+":"+classDeclaration.toString());}
        classDeclaration.getClassName().accept(this);
        if(classDeclaration.getParentClassName() != null) {
            classDeclaration.getParentClassName().accept(this);
        }
        for (FieldDeclaration field : classDeclaration.getFields()) {
            field.accept(this);
        }
        if (classDeclaration.getConstructor() != null) {
            classDeclaration.getConstructor().accept(this);
        }
        for (MethodDeclaration method : classDeclaration.getMethods()) {
            method.accept(this);
        }
        return null;
    }

    @Override
    public Void visit(ConstructorDeclaration constructorDeclaration) {
        {System.out.println("Line:"+constructorDeclaration.getLine()+":"+constructorDeclaration.toString());}
        constructorDeclaration.getMethodName().accept(this);
        for (VarDeclaration arg : constructorDeclaration.getArgs()) {
            arg.accept(this);
        }
        for (VarDeclaration localVar : constructorDeclaration.getLocalVars()) {
            localVar.accept(this);
        }
        for (Statement body : constructorDeclaration.getBody()) {
            body.accept(this);
        }
        return null;
    }

    @Override
    public Void visit(MethodDeclaration methodDeclaration) {
        {System.out.println("Line:"+methodDeclaration.getLine()+":"+methodDeclaration.toString());}
        methodDeclaration.getMethodName().accept(this);
        for (VarDeclaration arg : methodDeclaration.getArgs()) {
            arg.accept(this);
        }
        for (VarDeclaration localVar : methodDeclaration.getLocalVars()) {
            localVar.accept(this);
        }
        for (Statement body : methodDeclaration.getBody()) {
            body.accept(this);
        }
        return null;
    }

    @Override
    public Void visit(FieldDeclaration fieldDeclaration) {
        {System.out.println("Line:"+fieldDeclaration.getLine()+":"+fieldDeclaration.toString());}
        fieldDeclaration.getVarDeclaration().accept(this);
        return null;
    }

    @Override
    public Void visit(VarDeclaration varDeclaration) {
        {System.out.println("Line:"+varDeclaration.getLine()+":"+varDeclaration.toString());}
        varDeclaration.getVarName().accept(this);
        return null;
    }

    @Override
    public Void visit(AssignmentStmt assignmentStmt) {
        {System.out.println("Line:"+assignmentStmt.getLine()+":"+assignmentStmt.toString());}
        assignmentStmt.getlValue().accept(this);
        assignmentStmt.getrValue().accept(this);
        return null;
    }

    @Override
    public Void visit(BlockStmt blockStmt) {
        {System.out.println("Line:"+blockStmt.getLine()+":"+blockStmt.toString());}
        for (Statement statement: blockStmt.getStatements()) {
            statement.accept(this);
        }
        return null;
    }

    @Override
    public Void visit(ConditionalStmt conditionalStmt) {
        {System.out.println("Line:"+conditionalStmt.getLine()+":"+conditionalStmt.toString());}
        conditionalStmt.getCondition().accept(this);
        conditionalStmt.getThenBody().accept(this);
        if (conditionalStmt.getElseBody() != null) {
            conditionalStmt.getElseBody().accept(this);
        }
        return null;
    }

    @Override
    public Void visit(MethodCallStmt methodCallStmt) {
        {System.out.println("Line:"+methodCallStmt.getLine()+":"+methodCallStmt.toString());}
        methodCallStmt.getMethodCall().accept(this);
        return null;
    }

    @Override
    public Void visit(PrintStmt print) {
        {System.out.println("Line:"+print.getLine()+":"+print.toString());}
        print.getArg().accept(this);
        return null;
    }

    @Override
    public Void visit(ReturnStmt returnStmt) {
        {System.out.println("Line:"+returnStmt.getLine()+":"+returnStmt.toString());}
        returnStmt.getReturnedExpr().accept(this);
        return null;
    }

    @Override
    public Void visit(BreakStmt breakStmt) {
        {System.out.println("Line:"+breakStmt.getLine()+":"+breakStmt.toString());}
        return null;
    }

    @Override
    public Void visit(ContinueStmt continueStmt) {
        {System.out.println("Line:"+continueStmt.getLine()+":"+continueStmt.toString());}
        return null;
    }

    @Override
    public Void visit(ForeachStmt foreachStmt) {
        {System.out.println("Line:"+foreachStmt.getLine()+":"+foreachStmt.toString());}
        foreachStmt.getVariable().accept(this);
        foreachStmt.getList().accept(this);
        foreachStmt.getBody().accept(this);
        return null;
    }

    @Override
    public Void visit(ForStmt forStmt) {
        {System.out.println("Line:"+forStmt.getLine()+":"+forStmt.toString());}
        forStmt.getInitialize().accept(this);
        forStmt.getCondition().accept(this);
        forStmt.getUpdate().accept(this);
        forStmt.getBody().accept(this);
        return null;
    }

    @Override
    public Void visit(BinaryExpression binaryExpression) {
        {System.out.println("Line:"+binaryExpression.getLine()+":"+binaryExpression.toString());}
        binaryExpression.getFirstOperand().accept(this);
        binaryExpression.getSecondOperand().accept(this);
        return null;
    }

    @Override
    public Void visit(UnaryExpression unaryExpression) {
        {System.out.println("Line:"+unaryExpression.getLine()+":"+unaryExpression.toString());}
        unaryExpression.getOperand().accept(this);
        return null;
    }

    @Override
    public Void visit(ObjectOrListMemberAccess objectOrListMemberAccess) {
        {System.out.println("Line:"+objectOrListMemberAccess.getLine()+":"+objectOrListMemberAccess.toString());}
        objectOrListMemberAccess.getInstance().accept(this);
        objectOrListMemberAccess.getMemberName().accept(this);
        return null;
    }

    @Override
    public Void visit(Identifier identifier) {
        {System.out.println("Line:"+identifier.getLine()+":"+identifier.toString());}
        return null;
    }

    @Override
    public Void visit(ListAccessByIndex listAccessByIndex) {
        {System.out.println("Line:"+listAccessByIndex.getLine()+":"+listAccessByIndex.toString());}
        listAccessByIndex.getInstance().accept(this);
        listAccessByIndex.getIndex().accept(this);
        return null;
    }

    @Override
    public Void visit(MethodCall methodCall) {
        {System.out.println("Line:"+methodCall.getLine()+":"+methodCall.toString());}
        methodCall.getInstance().accept(this);
        for(Expression arg : methodCall.getArgs()) {
            arg.accept(this);
        }
        return null;
    }

    @Override
    public Void visit(NewClassInstance newClassInstance) {
        {System.out.println("Line:"+newClassInstance.getLine()+":"+newClassInstance.toString());}
        for(Expression arg : newClassInstance.getArgs()) {
            arg.accept(this);
        }
        return null;
    }

    @Override
    public Void visit(ThisClass thisClass) {
        {System.out.println("Line:"+thisClass.getLine()+":"+thisClass.toString());}
        return null;
    }

    @Override
    public Void visit(ListValue listValue) {
        {System.out.println("Line:"+listValue.getLine()+":"+listValue.toString());}
        for(Expression element : listValue.getElements()) {
            element.accept(this);
        }
        return null;
    }

    @Override
    public Void visit(NullValue nullValue) {
        {System.out.println("Line:"+nullValue.getLine()+":"+nullValue.toString());}
        return null;
    }

    @Override
    public Void visit(IntValue intValue) {
        {System.out.println("Line:"+intValue.getLine()+":"+intValue.toString());}
        return null;
    }

    @Override
    public Void visit(BoolValue boolValue) {
        {System.out.println("Line:"+boolValue.getLine()+":"+boolValue.toString());}
        return null;
    }

    @Override
    public Void visit(StringValue stringValue) {
        stringValue.setConstant(stringValue.getConstant().substring(1, stringValue.getConstant().length() - 1));
        {System.out.println("Line:"+stringValue.getLine()+":"+stringValue.toString());}
        return null;
    }

}
