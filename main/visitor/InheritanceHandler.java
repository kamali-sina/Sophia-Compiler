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

public class InheritanceHandler extends Visitor<Void> {

    private Stack<SymbolTable> stack = new Stack<>();
    private SymbolTable top;
    private SymbolTable root;
    private int numberErrors = 0;

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

    //Line:<LineNumber>:<ErrorItemMessage>

    @Override
    public Void visit(Program program) {
        for (ClassDeclaration programClass : program.getClasses()) {
            programClass.accept(this);
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
                if(classDeclaration.getParentClassName() != null) {
                    ClassSymbolTableItem parentClassSymbolTable = (ClassSymbolTableItem) this.root.getItem(
                            "Class_"+classDeclaration.getParentClassName().getName(), true);
                    classSymbolTableItem.getClassSymbolTable().pre = parentClassSymbolTable.getClassSymbolTable();
                    do {
                        try {
                            // Check class name in parents
                            classSymbolTableItem.getClassSymbolTable().getItem(
                                    classSymbolTableItem.getKey(), false);
                            // ErrorItemMessage: Redefinition of class <ClassName>
                            if(classSymbolTableItem.getName().indexOf('`') == -1) {
                                this.numberErrors += 1;
                                {System.out.println("Line:"+classDeclaration.getLine()+
                                        ":Redefinition of class "+classSymbolTableItem.getName());}
                            }
                            Identifier newName = new Identifier(classSymbolTableItem.getName()+"`");
                            classDeclaration.setClassName(newName);
                            classSymbolTableItem.setClassDeclaration(classDeclaration);
                        } catch (ItemNotFoundException e) {
                            break;
                        }
                    } while (true);
                }
                // updating stack
                this.stack.push(classSymbolTableItem.getClassSymbolTable());
                SymbolTable prev = this.top;
                this.top = classSymbolTableItem.getClassSymbolTable();
                //
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
                // updating stack
                this.stack.pop();
                this.top = prev;
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
                    "Method_"+constructorDeclaration.getMethodName().getName(),false);
            try {
                // Find methodSymbolTableItem name in parents
                methodSymbolTableItem.getMethodSymbolTable().getItem(methodSymbolTableItem.getKey(), false);
                // ErrorItemMessage: Redefinition of method <MethodName>
                this.numberErrors += 1;
                {System.out.println("Line:"+constructorDeclaration.getLine()+
                        ":Redefinition of method "+methodSymbolTableItem.getName());}
            } catch (ItemNotFoundException e) {
                try {
                    methodSymbolTableItem.getMethodSymbolTable().getItem(
                            "Field_"+constructorDeclaration.getMethodName().getName(), false);
                    // ErrorItemMessage: Redefinition of method <MethodName>
                    this.numberErrors += 1;
                    {System.out.println("Line:"+constructorDeclaration.getLine()+
                            ":Name of method "+methodSymbolTableItem.getName()+" conflicts with a field's name");}
                } catch (ItemNotFoundException er) {
                    //
                    this.stack.push(methodSymbolTableItem.getMethodSymbolTable());
                    SymbolTable prev = this.top;
                    this.top = methodSymbolTableItem.getMethodSymbolTable();
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
                }
            }
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
                    "Method_"+methodDeclaration.getMethodName().getName() ,false);
            try {
                // Find methodSymbolTableItem name in parents
                methodSymbolTableItem.getMethodSymbolTable().getItem(methodSymbolTableItem.getKey(), false);
                // ErrorItemMessage: Redefinition of method <MethodName>
                this.numberErrors += 1;
                {System.out.println("Line:"+methodDeclaration.getLine()+
                        ":Redefinition of method "+methodSymbolTableItem.getName());}
            } catch (ItemNotFoundException e) {
                try {
                    methodSymbolTableItem.getMethodSymbolTable().getItem(
                            "Field_"+methodDeclaration.getMethodName().getName(), false);
                    // ErrorItemMessage: Redefinition of method <MethodName>
                    this.numberErrors += 1;
                    {System.out.println("Line:"+methodDeclaration.getLine()+
                            ":Name of method "+methodSymbolTableItem.getName()+" conflicts with a field's name");}
                } catch (ItemNotFoundException er) {
                    // updating stack
                    this.stack.push(methodSymbolTableItem.getMethodSymbolTable());
                    SymbolTable prev = this.top;
                    this.top = methodSymbolTableItem.getMethodSymbolTable();
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
                }
                //
            }
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
                    "Field_"+fieldDeclaration.getVarDeclaration().getVarName().getName(), false);
            try {
                // Find fieldSymbolTableItem in parents
                this.top.getItem(fieldSymbolTableItem.getKey(), false);
                this.numberErrors += 1;
                {System.out.println("Line:"+fieldDeclaration.getLine()+
                        ":Redefinition of field "+fieldSymbolTableItem.getName());}
            } catch (ItemNotFoundException e) {
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
                            , false);
            try {
                // Find localVariableSymbolTableItem in parents
                this.top.getItem(localVariableSymbolTableItem.getKey(), false);
                // ErrorItemMessage: Redefinition of local variable <VariableName>
                this.numberErrors += 1;
                {System.out.println("Line:"+varDeclaration.getLine()+
                        ":Redefinition of local variable "+localVariableSymbolTableItem.getName());}
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
