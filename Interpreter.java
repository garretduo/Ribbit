/* Garret Duo
AP Computer Science A
Period 4
Compiler Project */

import java.util.*;

class Interpreter implements Expression.Visitor<Object>, Statement.Visitor<Object> {

    // executes Abstract Syntax Tree statements/expressions

    private Storage vars = new Storage(); // for variables

    public void interpret(List<Statement> statements) {
        try {
            for (Statement statement : statements) {
                run(statement); // executes all statements in order they were added to AST
            }
        } catch (RuntimeError error) { // error checking stuff during runtime
            Ribbit.runtimeError(error);
        }
    }

    private void run(Statement statement) { 
        statement.accept(this); // fancy visitor pattern stuff (returns object)
    }

    public String toString(Object a) { // toString helper method
        if (a == null) {
            return "null";
        }
        return a.toString();
    }

    private void checkIfNum(Token operator, Object operand) { // helper method for unary expression checking
        if (operand.getClass() == Long.class) {
            return;
        }
        throw new RuntimeError(operator, "Cannot perform operation on non-integer!", operator.getLine());
    }

    private void checkBinaryIfNum(Token operator, Object left, Object right) { // helper method for binary expression checking
        if (left.getClass() == Long.class && right.getClass() == Long.class) {
            return;
        }
        throw new RuntimeError(operator, "Cannot perform operation between non-integers!", operator.getLine());
    }

    @Override
    public Object visitBinary(Expression.Binary binary) {
        Object left = eval(binary.getLeft());
        Object right = eval(binary.getRight());
        Token operator = binary.getOperator();
        if (operator.getType() == TokenType.PLUS) {
            if (left.getClass() == Long.class && right.getClass() == Long.class) { // sum two numbers
                return (long) left + (long) right;
            }
            else if (left.getClass() == String.class || right.getClass() == String.class) { // string concatenation stuff
                return String.valueOf(left) + String.valueOf(right);
            }
            else {
                throw new RuntimeError(operator, "Cannot add or concatenate two non-integers and/or non-strings!", operator.getLine());
            }
        }
        if (operator.getType() == TokenType.MINUS) { // subtract two numbers
            checkBinaryIfNum(operator, left, right);
            return (long) left - (long) right;
        } 
        if (operator.getType() == TokenType.STAR) { // multiply two numbers
            checkBinaryIfNum(operator, left, right);
            return (long) left * (long) right;
        }
        if (operator.getType() == TokenType.SLASH) { // divide two numbers
            checkBinaryIfNum(operator, left, right);
            if ((Long) right == 0) {
                throw new RuntimeError(operator, "Cannot divide by zero!", operator.getLine()); // arithmetic error checking
            }
            return (long) left / (long) right;
        }
        if (operator.getType() == TokenType.MOD) { // mod two numbers
            checkBinaryIfNum(operator, left, right);
            if ((Long) right == 0) {
                throw new RuntimeError(operator, "Cannot divide by zero!", operator.getLine()); // arithmetic error checking
            }
            return (long) left % (long) right;
        }
        // compare two numbers
        if (operator.getType() == TokenType.GREATER) {
            checkBinaryIfNum(operator, left, right);
            return (long) left > (long) right;
        }
        if (operator.getType() == TokenType.GREATER_EQUAL) {
            checkBinaryIfNum(operator, left, right);
            return (long) left >= (long) right;
        }
        if (operator.getType() == TokenType.LESS) {
            checkBinaryIfNum(operator, left, right);
            return (long) left < (long) right;
        }
        if (operator.getType() == TokenType.LESS_EQUAL) {
            checkBinaryIfNum(operator, left, right);
            return (long) left <= (long) right;
        }
        if (operator.getType() == TokenType.EQUAL_EQUAL) {
            if (left.getClass() != right.getClass()) {
                throw new RuntimeError(operator, "Cannot compare two objects of different types!", operator.getLine());
            }
            return isEqual(left, right);
        }
        if (operator.getType() == TokenType.EXCLAM_EQUAL) {
            if (left.getClass() != right.getClass()) {
                throw new RuntimeError(operator, "Cannot compare two objects of different types!", operator.getLine());
            }
            return !isEqual(left, right);
        }
        return null;
    }

    public boolean isEqual(Object a, Object b) { // helper method to compare objects
        return a.equals(b);
    }

    @Override
    public Object visitGrouping(Expression.Grouping grouping) { // returns expression within a parentheses group
        return eval(grouping.getExpression());
    }

    @Override
    public Object visitLiteral(Expression.Literal literal) { // returns value of literal
        return literal.getValue();
    }

    @Override
    public Object visitNegator(Expression.Negator negator) {
        Object right = eval(negator.getRight()); // gets object to be negated
        Token operator = negator.getOperator(); 
        if (operator.getType() == TokenType.MINUS) { // if '-' get negative value
            checkIfNum(operator, right);
            return -(long) right; // negative value
        }
        if (operator.getType() == TokenType.EXCLAM) { // if '!' get boolean opposite value
            return !(boolean) right;
        }
        return null;
    }

    @Override
    public Object visitVariable(Expression.Variable variable) { 
        Token var = variable.getName();
        return vars.grab(var.getLexeme(), var.getLine()); // calls 'Storage' object with all vars
    }

    public Object eval(Expression expression) { // fancy visitor pattern stuff
        return expression.accept(this);
    }

    @Override
    public Object visitAssign(Expression.Assign assign) {
        Object val = eval(assign.getVal()); // value to be assigned
        Expression name = assign.getName();
        if (name.getClass() == Expression.ArrayAccess.class) {
            int index = ((Long)eval(((Expression.ArrayAccess)name).getIndex())).intValue();
            Token var = ((Expression.ArrayAccess)(assign.getName())).getName();
            vars.assignArr(var.getLexeme(), val, index, var.getLine());
            return val;
        }
        Token var = ((Expression.Variable)(assign.getName())).getName(); // variable name to assign val to
        vars.assignVar(var.getLexeme(), val, var.getLine());
        return val;
    }

    @Override
    public Object visitLogic(Expression.Logic logic) {
        Object left = eval(logic.getLeft()); // left object
        Token operator = logic.getOperator();
        if (operator.getType() == TokenType.OR) {
            if ((Boolean) left) { // if left statement true OR evaluates true always
                return left;
            }
        }
        else {
            if (!(Boolean) left) { // if left statement false AND evalulates false always
                return left;
            }
        }
        return eval(logic.getRight()); // otherwise, must also evaluate right side
    }

    @Override
    public Object visitInput(Expression.Input input) {
        Scanner in = new Scanner(System.in);
        String temp = in.nextLine();
        boolean isNum = true;
        for (int i = 0; i < temp.length(); i++) { // see if string input needs to be converted to num
            if (Character.isLetter(temp.charAt(i))) { // if any letters in input, cannot be number
                isNum = false;
                i = temp.length();
            }
        }
        Object val = temp;
        if (isNum) {
            try {
                val = Long.valueOf(temp); // sets val to long
            } catch (NumberFormatException ex) {
                throw new RuntimeError(val.toString(), "Input must be either a number or string value!", input.getLine());
            }
        }
        input.setValue(val); // for error checking, strictly speaking not required
        return val;
    }

    @Override
    public Object visitArrayAccess(Expression.ArrayAccess arrAccess) {
        Expression accessID = arrAccess.getIndex();
        int index = ((Long)eval(accessID)).intValue();
        String arrName = (arrAccess.getName().getLexeme()); // name of identifier corresponding to array to access
        return vars.grabArr(arrName, index, arrAccess.getName().getLine());
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object visitLength(Expression.Length length) {
        Object right = eval(length.getRight());
        if (right == null) {
            return null;
        }
        if (right.getClass() != ArrayList.class) {
            return Long.valueOf(String.valueOf(right).length());
        }
        else {
            List<Object> list = (ArrayList<Object>) right;
            return Long.valueOf(list.size());
        }
    }

    @Override
    public Object visitExpressionStmt(Statement.ExprStmt stmt) {
        eval(stmt.getExpression()); // fancy visitor pattern stuff
        return null;
    }

    @Override
    public Object visitDispStmt(Statement.Disp stmt) {
        Object val = eval(stmt.getExpression()); // takes expression to be printed and stringifies it, then prints
        if (stmt.isNewLine()) { // if displn
            System.out.println(toString(val));
        }
        else { // if disp
            System.out.print(toString(val));
        }
        return null;
    }

    @Override
    public Object visitVarStmt(Statement.Var stmt) { // only handles variable declaration/init, NOT access (that is an expression)
        Object val = null;
        Expression init = stmt.getInit();
        if (init != null) { // var has been initialized
            val = eval(stmt.getInit()); // gets init value
            if (val.getClass() == ArrList.class) {
                Expression ex = ((ArrList)val).getSize();
                Object temp = eval(ex);
                if (temp.getClass() != Long.class) {
                    int line;
                    if (ex.getClass() == Expression.Literal.class) {
                        line = ((Expression.Literal)ex).getLine();
                    } else {
                        line = ((Expression.Variable)ex).getName().getLine();
                    }
                    throw new RuntimeError(temp.toString(), "Index must evaluate to a number!", line);
                }
                int size = ((Long)(temp)).intValue();
                List<Object> list = new ArrayList<Object>(size);
                for (int i = 0; i < size; i++) {
                    list.add(0);
                }
                vars.define(stmt.getName().getLexeme(), list);
                return null;
            }
        }
        vars.define(stmt.getName().getLexeme(), val); // adds var and value to storage
        return null;
    }

    @Override
    public Object visitBlockStmt(Statement.Block stmt) {
        runBlock(stmt.getStatements(), new Storage(vars)); // calls helper method to run block
        return null;
    }

    public void runBlock(List<Statement> statements, Storage vars) {
        Storage temp = this.vars;
        this.vars = vars; // handles local variables
        for (Statement statement : statements) {
            run(statement); // runs statements within block
        }
        this.vars = temp; // after, sets vars back to global scope
    }

    @Override
    public Object visitIfStmt(Statement.IfStmt stmt) {
        Object cond = eval(stmt.getExpression());
        if (cond.getClass() == Boolean.class) { // only works if cond is actually a cond
            if ((Boolean)cond) {
                run(stmt.getIfBranch()); // runs if branch if true
            } 
            else if (stmt.getElseBranch() != null ) {
                run(stmt.getElseBranch()); // if cond false and else branch exists, run else branch
            }
        }
        return null;
    }

    @Override
    public Object visitWhileStmt(Statement.WhileStmt stmt) { 
        while ((Boolean) eval(stmt.getCondition())) { // literally just Java's while loop
            run(stmt.getBody());
        }
        return null;
    }
}