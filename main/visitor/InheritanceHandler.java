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
import main.symbolTable.items.ClassSymbolTableItem;
import main.symbolTable.items.FieldSymbolTableItem;
import main.symbolTable.items.LocalVariableSymbolTableItem;
import main.symbolTable.items.MethodSymbolTableItem;
import main.symbolTable.utils.Stack;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class InheritanceHandler extends Visitor<Void> {

    private Stack<SymbolTable> stack = new Stack<>();
    private SymbolTable top;
    private SymbolTable root;
    private int numberErrors = 0;
    private Set<String> errors = new HashSet<>();

    public void setInfo(NameCollector nameCollector) {
        this.setStack(nameCollector.getStack());
        this.setTop(nameCollector.getTop());
        this.setRoot(nameCollector.getRoot());
        this.setNumberErrors(nameCollector.getNumberErrors());
        this.setErrors(nameCollector.getErrors());
    }

    public void setStack(Stack<SymbolTable> stack) {
        this.stack = stack;
    }

    public void setTop(SymbolTable top) {
        this.top = top;
    }

    public void setRoot(SymbolTable root) {
        this.root = root;
    }

    public void setNumberErrors(int numberErrors) {
        this.numberErrors = numberErrors;
    }

    public int getNumberErrors() {
        return this.numberErrors;
    }

    public void setErrors(Set<String> errors) { this.errors = errors; }

    private void checkInheritanceCycle(ClassDeclaration classDeclaration) {
        Set<SymbolTable> visitedSymbolTables = new HashSet<>();
        try {
            // Find classSymbolTableItem
            ClassSymbolTableItem classSymbolTableItem = (ClassSymbolTableItem) this.root.getItem(
                    "Class_"+classDeclaration.getClassName().getName(), true);
            // Find parent
            SymbolTable currentSymbolTable = classSymbolTableItem.getClassSymbolTable().pre;
            while(currentSymbolTable != null && (!visitedSymbolTables.contains(currentSymbolTable))) {
                visitedSymbolTables.add(currentSymbolTable);
                if (currentSymbolTable == classSymbolTableItem.getClassSymbolTable()) {
                    //ErrorItemMessage: Class <ClassName> is in an inheritance cycle
                    //
                    if (!this.errors.contains("Line:"+classDeclaration.getLine()+
                            ":Class "+classDeclaration.getClassName().getName()+" is in an inheritance cycle")) {
                        this.errors.add("Line:"+classDeclaration.getLine()+
                                ":Class "+classDeclaration.getClassName().getName()+" is in an inheritance cycle");
                        this.numberErrors += 1;
                        {System.out.println("Line:"+classDeclaration.getLine()+
                                ":Class "+classDeclaration.getClassName().getName()+" is in an inheritance cycle");}
                    }
                    //
                    break;
                }
                currentSymbolTable = currentSymbolTable.pre;
            }
        } catch (ItemNotFoundException e) {
            //
        }
    }

    //Line:<LineNumber>:<ErrorItemMessage>

    @Override
    public Void visit(Program program) {
        for (ClassDeclaration programClass : program.getClasses()) {
            programClass.accept(this);
        }
        for (ClassDeclaration classDeclaration : program.getClasses()) {
            checkInheritanceCycle(classDeclaration);
        }
        return null;
    }

    @Override
    public Void visit(ClassDeclaration classDeclaration) {
        try {
            // Find classSymbolTableItem
            ClassSymbolTableItem classSymbolTableItem = (ClassSymbolTableItem) this.root.getItem(
                    "Class_"+classDeclaration.getClassName().getName(), true);
            try {
                // Find parentClassSymbolTableItem
                if (classDeclaration.getParentClassName() != null) {
                    ClassSymbolTableItem parentClassSymbolTable = (ClassSymbolTableItem) this.root.getItem(
                            "Class_"+classDeclaration.getParentClassName().getName(), true);
                    classSymbolTableItem.getClassSymbolTable().pre = parentClassSymbolTable.getClassSymbolTable();

                    // updating stack
                    this.stack.push(classSymbolTableItem.getClassSymbolTable());
                    SymbolTable prev = this.top;
                    this.top = classSymbolTableItem.getClassSymbolTable();
                    //
                    classDeclaration.getClassName().accept(this);
                    classDeclaration.getParentClassName().accept(this);
                    for (FieldDeclaration field : classDeclaration.getFields()) {
                        field.accept(this);
                    }
                    if (classDeclaration.getConstructor() != null) {
                        classDeclaration.getConstructor().accept(this);
                    }
                    for (MethodDeclaration method : classDeclaration.getMethods()) {
                        method.accept(this);
                    }
                    // updating stack
                    this.stack.pop();
                    this.top = prev;
                }

            } catch (ItemNotFoundException e) {
                //
            }
        } catch (ItemNotFoundException e) {
            //
        }
        return null;
    }

    @Override
    public Void visit(ConstructorDeclaration constructorDeclaration) {
        try {
            // Find methodSymbolTableItem
            MethodSymbolTableItem methodSymbolTableItem = (MethodSymbolTableItem) this.top.getItem(
                    "Method_"+constructorDeclaration.getMethodName().getName(),true);
            try {
                // Find methodSymbolTableItem name in parents
                this.top.getItem(methodSymbolTableItem.getKey(), false);
                // ErrorItemMessage: Redefinition of method <MethodName>
                //
                if (!this.errors.contains("Line:"+constructorDeclaration.getLine()+
                        ":Redefinition of method "+methodSymbolTableItem.getName())) {
                    this.errors.add("Line:"+constructorDeclaration.getLine()+
                            ":Redefinition of method "+methodSymbolTableItem.getName());
                    this.numberErrors += 1;
                    {System.out.println("Line:"+constructorDeclaration.getLine()+
                            ":Redefinition of method "+methodSymbolTableItem.getName());}
                }
                //
                this.stack.push(methodSymbolTableItem.getMethodSymbolTable());
                SymbolTable prev = this.top;
                this.top = methodSymbolTableItem.getMethodSymbolTable();
                //
                constructorDeclaration.getMethodName().accept(this);
                for (Statement body : constructorDeclaration.getBody()) {
                    body.accept(this);
                }
                // updating stack
                this.stack.pop();
                this.top = prev;
            } catch (ItemNotFoundException e) {
                //
            }
            if (!methodSymbolTableItem.getFieldConflict()) {
                try {
                    this.top.getItem(
                            "Field_"+constructorDeclaration.getMethodName().getName(), false);
                    // ErrorItemMessage: Redefinition of method <MethodName>
                    //
                    if (!this.errors.contains("Line:"+constructorDeclaration.getLine()+
                            ":Name of method "+methodSymbolTableItem.getName()+" conflicts with a field's name")) {
                        this.errors.add("Line:"+constructorDeclaration.getLine()+
                                ":Name of method "+methodSymbolTableItem.getName()+" conflicts with a field's name");
                        this.numberErrors += 1;
                        methodSymbolTableItem.setFieldConflict();
                        {System.out.println("Line:"+constructorDeclaration.getLine()+
                                ":Name of method "+methodSymbolTableItem.getName()+" conflicts with a field's name");}
                    }
                    //
                } catch (ItemNotFoundException er) {
                    //
                }
            }
            // they were here before
        } catch (ItemNotFoundException e) {
            //
        }
        return null;
    }

    @Override
    public Void visit(MethodDeclaration methodDeclaration) {
        try {
            // Find methodSymbolTableItem
            MethodSymbolTableItem methodSymbolTableItem = (MethodSymbolTableItem) this.top.getItem(
                    "Method_"+methodDeclaration.getMethodName().getName() ,true);
            try {
                // Find methodSymbolTableItem name in parents
                this.top.getItem(methodSymbolTableItem.getKey(), false);
                // ErrorItemMessage: Redefinition of method <MethodName>
                //
                if (!this.errors.contains("Line:"+methodDeclaration.getLine()+
                        ":Redefinition of method "+methodSymbolTableItem.getName())) {
                    this.errors.add("Line:"+methodDeclaration.getLine()+
                            ":Redefinition of method "+methodSymbolTableItem.getName());
                    this.numberErrors += 1;
                    {System.out.println("Line:"+methodDeclaration.getLine()+
                            ":Redefinition of method "+methodSymbolTableItem.getName());}
                }
                //
                // updating stack
                this.stack.push(methodSymbolTableItem.getMethodSymbolTable());
                SymbolTable prev = this.top;
                this.top = methodSymbolTableItem.getMethodSymbolTable();
                //
                methodDeclaration.getMethodName().accept(this);
                for (Statement body : methodDeclaration.getBody()) {
                    body.accept(this);
                }
                // updating stack
                this.stack.pop();
                this.top = prev;
                //
            } catch (ItemNotFoundException e) {
                //
            }
            if (!methodSymbolTableItem.getFieldConflict()) {
                try {
                    this.top.getItem(
                            "Field_"+methodDeclaration.getMethodName().getName(), false);
                    // ErrorItemMessage: Redefinition of method <MethodName>
                    //
                    if (!this.errors.contains("Line:"+methodDeclaration.getLine()+
                            ":Name of method "+methodSymbolTableItem.getName()+" conflicts with a field's name")) {
                        this.errors.add("Line:"+methodDeclaration.getLine()+
                                ":Name of method "+methodSymbolTableItem.getName()+" conflicts with a field's name");
                        this.numberErrors += 1;
                        methodSymbolTableItem.setFieldConflict();
                        {System.out.println("Line:"+methodDeclaration.getLine()+
                                ":Name of method "+methodSymbolTableItem.getName()+" conflicts with a field's name");}
                    }
                    //
                } catch (ItemNotFoundException er) {
                    //
                }
            }
            // they were here before
        } catch (ItemNotFoundException e) {
            //
        }
        return null;
    }

    @Override
    public Void visit(FieldDeclaration fieldDeclaration) {
        try {
            // Find fieldSymbolTableItem
            FieldSymbolTableItem fieldSymbolTableItem = (FieldSymbolTableItem) this.top.getItem(
                    "Field_"+fieldDeclaration.getVarDeclaration().getVarName().getName(), true);
            try {
                // Find fieldSymbolTableItem in parents
                this.top.getItem(fieldSymbolTableItem.getKey(), false);
                //
                if (!this.errors.contains("Line:"+fieldDeclaration.getLine()+
                        ":Redefinition of field "+fieldSymbolTableItem.getName())) {
                    this.errors.add("Line:"+fieldDeclaration.getLine()+
                            ":Redefinition of field "+fieldSymbolTableItem.getName());
                    this.numberErrors += 1;
                    {System.out.println("Line:"+fieldDeclaration.getLine()+
                            ":Redefinition of field "+fieldSymbolTableItem.getName());}
                }
                //
            } catch (ItemNotFoundException e) {
                // Find classSymbolTableItem
                SymbolTable currentSymbolTable = this.top.pre;
                Set<SymbolTable> visitedSymbolTables = new HashSet<>();
                while((currentSymbolTable != null) && (!visitedSymbolTables.contains(currentSymbolTable))) {
                    visitedSymbolTables.add(currentSymbolTable);
                    try {
                        MethodSymbolTableItem methodSymbolTableItem = (MethodSymbolTableItem) currentSymbolTable.getItem("Method_"+fieldDeclaration.getVarDeclaration().getVarName().getName(), true);
                        if (!methodSymbolTableItem.getFieldConflict()) {
                            //ErrorItemMessage: Class <ClassName> is in an inheritance cycle
                            for (Integer line : methodSymbolTableItem.getLines()) {
                                //
                                if (!this.errors.contains("Line:"+line+
                                        ":Name of method "+methodSymbolTableItem.getName()+" conflicts with a field's name")) {
                                    this.errors.add("Line:"+line+
                                            ":Name of method "+methodSymbolTableItem.getName()+" conflicts with a field's name");
                                    this.numberErrors += 1;
                                    {System.out.println("Line:"+line+
                                            ":Name of method "+methodSymbolTableItem.getName()+" conflicts with a field's name");}
                                }
                                //
                            }
                        }
                    } catch (ItemNotFoundException itemNotFoundException) {
                        //
                    }
                    currentSymbolTable = currentSymbolTable.pre;
                }
                fieldDeclaration.getVarDeclaration().accept(this);
            }
        } catch (ItemNotFoundException e) {

        }
        return null;
    }

    @Override
    public Void visit(VarDeclaration varDeclaration) {
        try {
            // Find localVariableSymbolTableItem
            LocalVariableSymbolTableItem localVariableSymbolTableItem =
                    (LocalVariableSymbolTableItem) this.top.getItem("Var_"+varDeclaration.getVarName().getName()
                            , true);
            try {
                // Find localVariableSymbolTableItem in parents
                this.top.getItem(localVariableSymbolTableItem.getKey(), false);
                // ErrorItemMessage: Redefinition of local variable <VariableName>
                //
                if (!this.errors.contains("Line:"+varDeclaration.getLine()+
                        ":Redefinition of local variable "+localVariableSymbolTableItem.getName())) {
                    this.errors.add("Line:"+varDeclaration.getLine()+
                            ":Redefinition of local variable "+localVariableSymbolTableItem.getName());
                    this.numberErrors += 1;
                    {System.out.println("Line:"+varDeclaration.getLine()+
                            ":Redefinition of local variable "+localVariableSymbolTableItem.getName());}
                }
                //
            } catch (ItemNotFoundException e) {
                varDeclaration.getVarName().accept(this);
            }
        } catch (ItemNotFoundException e) {
            //
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
