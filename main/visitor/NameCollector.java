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
import main.symbolTable.SymbolTable;
import main.symbolTable.exceptions.ItemAlreadyExistsException;
import main.symbolTable.exceptions.ItemNotFoundException;
import main.symbolTable.items.*;
import main.symbolTable.utils.Stack;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class NameCollector extends Visitor<Void> {

    private Stack<SymbolTable> stack = new Stack<>();
    private SymbolTable top;
    private SymbolTable root;
    private int numberErrors = 0;
    private Set<String> errors = new HashSet<>();

    public Stack<SymbolTable> getStack() {
        return  this.stack;
    }

    public SymbolTable getTop() {
        return this.top;
    }

    public SymbolTable getRoot() {
        return this.root;
    }

    public int getNumberErrors() {
        return numberErrors;
    }

    public Set<String> getErrors() { return this.errors; }

    //Line:<LineNumber>:<ErrorItemMessage>

    @Override
    public Void visit(Program program) {
        SymbolTable symbolTable = new SymbolTable();
        // Updating stack
        this.stack.push(symbolTable);
        this.top = symbolTable;
        this.root = symbolTable;
        //
        for (ClassDeclaration programClass : program.getClasses()) {
            programClass.accept(this);
        }
        return null;
    }

    @Override
    public Void visit(ClassDeclaration classDeclaration) {
        // Creating item
        ClassSymbolTableItem classSymbolTableItem = new ClassSymbolTableItem(classDeclaration);
        // Putting item into symbol table
        do {
            try {
                this.top.put(classSymbolTableItem);
                break;
            } catch (ItemAlreadyExistsException e) {
                // ErrorItemMessage: Redefinition of class <ClassName>
                if(classSymbolTableItem.getName().indexOf('`') == -1) {
                    this.numberErrors += 1;
                    //
                    this.errors.add("Line:"+classDeclaration.getLine()+
                            ":Redefinition of class "+classSymbolTableItem.getName());
                    //
                    {System.out.println("Line:"+classDeclaration.getLine()+
                            ":Redefinition of class "+classSymbolTableItem.getName());}
                }
                classSymbolTableItem.setName(classSymbolTableItem.getName()+"`");
            }
        } while (true);
        // Adding symbol table to the item
        SymbolTable symbolTable = new SymbolTable();   // pre -> null
        classSymbolTableItem.setClassSymbolTable(symbolTable);
        // Updating stack
        this.stack.push(symbolTable);
        SymbolTable prev = this.top;
        this.top = symbolTable;
        // Visiting other nodes
        classDeclaration.getClassName().accept(this);
        if(classDeclaration.getParentClassName() != null) {
            classDeclaration.getParentClassName().accept(this);
        }
        for (FieldDeclaration field : classDeclaration.getFields()) {
            field.accept(this);
        }
        if(classDeclaration.getConstructor() != null) {
            classDeclaration.getConstructor().accept(this);
        }
        for (MethodDeclaration method : classDeclaration.getMethods()) {
            method.accept(this);
        }
        // updating stack
        this.stack.pop();
        this.top = prev;
        //
        return null;
    }

    @Override
    public Void visit(ConstructorDeclaration constructorDeclaration) {
        // creating item
        MethodSymbolTableItem methodSymbolTableItem = new MethodSymbolTableItem(constructorDeclaration);
        methodSymbolTableItem.addLine(constructorDeclaration.getLine());
        // putting item into symbol table
        try {
            this.top.put(methodSymbolTableItem);
        } catch (ItemAlreadyExistsException e) {
            // ErrorItemMessage: Redefinition of method <MethodName>
            this.numberErrors += 1;
            try {
                MethodSymbolTableItem sameMethodSymbolTableItem = (MethodSymbolTableItem) this.top.getItem(methodSymbolTableItem.getKey(), true);
                sameMethodSymbolTableItem.addLine(constructorDeclaration.getLine());
            } catch (ItemNotFoundException itemNotFoundException) {

            }
            //
            this.errors.add("Line:"+constructorDeclaration.getLine()+
                    ":Redefinition of method "+methodSymbolTableItem.getName());
            //
            {System.out.println("Line:"+constructorDeclaration.getLine()+
                    ":Redefinition of method "+methodSymbolTableItem.getName());}
        }
        // Check whether name of the constructor is same as the name of field
        try {
            this.top.getItem("Field_"+constructorDeclaration.getMethodName().getName(), true);
            // ErrorItemMessage: Redefinition of method <MethodName>
            this.numberErrors += 1;
            methodSymbolTableItem.setFieldConflict();
            //
            this.errors.add("Line:"+constructorDeclaration.getLine()+
                    ":Name of method "+methodSymbolTableItem.getName()+" conflicts with a field's name");
            //
            {System.out.println("Line:"+constructorDeclaration.getLine()+
                    ":Name of method "+methodSymbolTableItem.getName()+" conflicts with a field's name");}
        } catch (ItemNotFoundException er) {
            //
        }
        // adding symbol table to the item
        SymbolTable symbolTable = new SymbolTable(this.top);   //pre -> class
        methodSymbolTableItem.setMethodSymbolTable(symbolTable);
        // updating stack
        this.stack.push(symbolTable);
        SymbolTable prev = this.top;
        this.top = symbolTable;
        //
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
        // updating stack
        this.stack.pop();
        this.top = prev;
        //
        return null;
    }

    @Override
    public Void visit(MethodDeclaration methodDeclaration) {
        // Creating item
        MethodSymbolTableItem methodSymbolTableItem = new MethodSymbolTableItem(methodDeclaration);
        methodSymbolTableItem.addLine(methodDeclaration.getLine());
        // Putting item into symbol table
        try {
            this.top.put(methodSymbolTableItem);
        } catch (ItemAlreadyExistsException e) {
            // ErrorItemMessage: Redefinition of method <MethodName>
            this.numberErrors += 1;
            try {
                MethodSymbolTableItem sameMethodSymbolTableItem = (MethodSymbolTableItem) this.top.getItem(methodSymbolTableItem.getKey(), true);
                sameMethodSymbolTableItem.addLine(methodDeclaration.getLine());
            } catch (ItemNotFoundException itemNotFoundException) {

            }
            methodSymbolTableItem.addLine(methodDeclaration.getLine());
            //
            this.errors.add("Line:"+methodDeclaration.getLine()+
                    ":Redefinition of method "+methodSymbolTableItem.getName());
            //
            {System.out.println("Line:"+methodDeclaration.getLine()+
                    ":Redefinition of method "+methodSymbolTableItem.getName());}
        }
        // Check whether name of the method is same as the name of field
        try {
            this.top.getItem("Field_"+methodDeclaration.getMethodName().getName(), true);
            // ErrorItemMessage: Redefinition of method <MethodName>
            this.numberErrors += 1;
            methodSymbolTableItem.setFieldConflict();
            //
            this.errors.add("Line:"+methodDeclaration.getLine()+
                    ":Name of method "+methodSymbolTableItem.getName()+" conflicts with a field's name");
            //
            {System.out.println("Line:"+methodDeclaration.getLine()+
                    ":Name of method "+methodSymbolTableItem.getName()+" conflicts with a field's name");}
        } catch (ItemNotFoundException er) {
            //
        }
        // adding symbol table to the item
        SymbolTable symbolTable = new SymbolTable(this.top);   //pre -> class
        methodSymbolTableItem.setMethodSymbolTable(symbolTable);
        // updating stack
        this.stack.push(symbolTable);
        SymbolTable prev = this.top;
        this.top = symbolTable;
        //
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
        // updating stack
        this.stack.pop();
        this.top = prev;
        //
        return null;
    }

    @Override
    public Void visit(FieldDeclaration fieldDeclaration) {
        // Creating item
        FieldSymbolTableItem fieldSymbolTableItem = new FieldSymbolTableItem(fieldDeclaration);
        // Putting item into symbol table
        try {
            this.top.put(fieldSymbolTableItem);
            fieldDeclaration.getVarDeclaration().accept(this);
        } catch (ItemAlreadyExistsException e) {
            // ErrorItemMessage: Redefinition of field <FieldName>
            this.numberErrors += 1;
            //
            this.errors.add("Line:"+fieldDeclaration.getLine()+
                    ":Redefinition of field "+fieldSymbolTableItem.getName());
            //
            {System.out.println("Line:"+fieldDeclaration.getLine()+
                    ":Redefinition of field "+fieldSymbolTableItem.getName());}
        }
        return null;
    }

    @Override
    public Void visit(VarDeclaration varDeclaration) {
        // Creating item
        LocalVariableSymbolTableItem localVariableSymbolTableItem = new LocalVariableSymbolTableItem(varDeclaration);
        // Putting item into symbol table
        try {
            this.top.put(localVariableSymbolTableItem);
            varDeclaration.getVarName().accept(this);
        } catch (ItemAlreadyExistsException e) {
            // ErrorItemMessage: Redefinition of local variable <VariableName>
            this.numberErrors += 1;
            //
            this.errors.add("Line:"+varDeclaration.getLine()+
                    ":Redefinition of local variable "+localVariableSymbolTableItem.getName());
            //
            {System.out.println("Line:"+varDeclaration.getLine()+
                    ":Redefinition of local variable "+localVariableSymbolTableItem.getName());}
        }
        return null;
    }

    @Override
    public Void visit(AssignmentStmt assignmentStmt) {
        assignmentStmt.getlValue().accept(this);
        assignmentStmt.getrValue().accept(this);
        return null;
    }

    @Override
    public Void visit(BlockStmt blockStmt) {
        for (Statement statement: blockStmt.getStatements()) {
            statement.accept(this);
        }
        return null;
    }

    @Override
    public Void visit(ConditionalStmt conditionalStmt) {
        conditionalStmt.getCondition().accept(this);
        conditionalStmt.getThenBody().accept(this);
        if (conditionalStmt.getElseBody() != null) {
            conditionalStmt.getElseBody().accept(this);
        }
        return null;
    }

    @Override
    public Void visit(MethodCallStmt methodCallStmt) {
        methodCallStmt.getMethodCall().accept(this);
        return null;
    }

    @Override
    public Void visit(PrintStmt print) {
        print.getArg().accept(this);
        return null;
    }

    @Override
    public Void visit(ReturnStmt returnStmt) {
        returnStmt.getReturnedExpr().accept(this);
        return null;
    }

    @Override
    public Void visit(BreakStmt breakStmt) {
        return null;
    }

    @Override
    public Void visit(ContinueStmt continueStmt) {
        return null;
    }

    @Override
    public Void visit(ForeachStmt foreachStmt) {
        foreachStmt.getVariable().accept(this);
        foreachStmt.getList().accept(this);
        foreachStmt.getBody().accept(this);
        return null;
    }

    @Override
    public Void visit(ForStmt forStmt) {
        forStmt.getInitialize().accept(this);
        forStmt.getCondition().accept(this);
        forStmt.getUpdate().accept(this);
        forStmt.getBody().accept(this);
        return null;
    }

    @Override
    public Void visit(BinaryExpression binaryExpression) {
        binaryExpression.getFirstOperand().accept(this);
        binaryExpression.getSecondOperand().accept(this);
        return null;
    }

    @Override
    public Void visit(UnaryExpression unaryExpression) {
        unaryExpression.getOperand().accept(this);
        return null;
    }

    @Override
    public Void visit(ObjectOrListMemberAccess objectOrListMemberAccess) {
        objectOrListMemberAccess.getInstance().accept(this);
        objectOrListMemberAccess.getMemberName().accept(this);
        return null;
    }

    @Override
    public Void visit(Identifier identifier) {
        return null;
    }

    @Override
    public Void visit(ListAccessByIndex listAccessByIndex) {
        listAccessByIndex.getInstance().accept(this);
        listAccessByIndex.getIndex().accept(this);
        return null;
    }

    @Override
    public Void visit(MethodCall methodCall) {
        methodCall.getInstance().accept(this);
        for(Expression arg : methodCall.getArgs()) {
            arg.accept(this);
        }
        return null;
    }

    @Override
    public Void visit(NewClassInstance newClassInstance) {
        for(Expression arg : newClassInstance.getArgs()) {
            arg.accept(this);
        }
        return null;
    }

    @Override
    public Void visit(ThisClass thisClass) {
        return null;
    }

    @Override
    public Void visit(ListValue listValue) {
        for(Expression element : listValue.getElements()) {
            element.accept(this);
        }
        return null;
    }

    @Override
    public Void visit(NullValue nullValue) {
        return null;
    }

    @Override
    public Void visit(IntValue intValue) {
        return null;
    }

    @Override
    public Void visit(BoolValue boolValue) {
        return null;
    }

    @Override
    public Void visit(StringValue stringValue) {
        return null;
    }

}
