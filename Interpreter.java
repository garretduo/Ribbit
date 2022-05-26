/* Garret Duo
AP Computer Science A
Period 4
Compiler Project */

import java.util.*;

class Interpreter implements Expression.Visitor<Object>, Statement.Visitor<Object> {
    private Storage vars = new Storage();

    public void interpret(List<Statement> statements) {
        try {
            for (Statement statement : statements) {
                run(statement);
            }
        } catch (RuntimeError error) {
            Ribbit.runtimeError(error);
        }
    }

    private void run(Statement statement) {
        statement.accept(this);
    }

    public String toString(Object a) {
        if (a == null) {
            return "null";
        }
        return a.toString();
    }

    private void checkIfNum(Token operator, Object operand) {
        if (operand.getClass() == Long.class) {
            return;
        }
        throw new RuntimeError(operator, "Cannot perform operation on non-integer!", operator.getLine());
    }

    private void checkBinaryIfNum(Token operator, Object left, Object right) {
        if (left.getClass() == Long.class && right.getClass() == Long.class) {
            return;
        }
        throw new RuntimeError(operator, "Cannot perform operation between non-integers!", operator.getLine());
    }

    @Override
    public Object visitBinary(Expression.Binary binary) {
        Object left = eval(binary.getLeft());
        Object right = eval(binary.getRight());
        if (binary.getOperator().getType() == TokenType.PLUS) {
            if (left.getClass() == Long.class && right.getClass() == Long.class) {
                return (long) left + (long) right;
            }
            else if (left.getClass() == String.class || right.getClass() == String.class) {
                if (left.getClass() == Long.class) {
                    return String.valueOf(left) + (String) right;
                }
                else {
                    return (String) left + String.valueOf(right);
                }
            }
            else {
                throw new RuntimeError(binary.getOperator(), "Cannot add or concatenate two non-integers and/or non-strings!", binary.getOperator().getLine());
            }
        }
        if (binary.getOperator().getType() == TokenType.MINUS) {
            checkBinaryIfNum(binary.getOperator(), left, right);
            return (long) left - (long) right;
        } 
        if (binary.getOperator().getType() == TokenType.STAR) {
            checkBinaryIfNum(binary.getOperator(), left, right);
            return (long) left * (long) right;
        }
        if (binary.getOperator().getType() == TokenType.SLASH) {
            checkBinaryIfNum(binary.getOperator(), left, right);
            if ((Long) right == 0) {
                throw new RuntimeError(binary.getOperator(), "Cannot divide by zero!", binary.getOperator().getLine());
            }
            return (long) left / (long) right;
        }
        if (binary.getOperator().getType() == TokenType.MOD) {
            checkBinaryIfNum(binary.getOperator(), left, right);
            if ((Long) right == 0) {
                throw new RuntimeError(binary.getOperator(), "Cannot divide by zero!", binary.getOperator().getLine());
            }
            return (long) left % (long) right;
        }
        if (binary.getOperator().getType() == TokenType.GREATER) {
            checkBinaryIfNum(binary.getOperator(), left, right);
            return (long) left > (long) right;
        }
        if (binary.getOperator().getType() == TokenType.GREATER_EQUAL) {
            checkBinaryIfNum(binary.getOperator(), left, right);
            return (long) left >= (long) right;
        }
        if (binary.getOperator().getType() == TokenType.LESS) {
            checkBinaryIfNum(binary.getOperator(), left, right);
            return (long) left < (long) right;
        }
        if (binary.getOperator().getType() == TokenType.LESS_EQUAL) {
            checkBinaryIfNum(binary.getOperator(), left, right);
            return (long) left <= (long) right;
        }
        if (binary.getOperator().getType() == TokenType.EQUAL_EQUAL) {
            if (left.getClass() != right.getClass()) {
                throw new RuntimeError(binary.getOperator(), "Cannot compare two objects of different types!", binary.getOperator().getLine());
            }
            return isEqual(left, right);
        }
        if (binary.getOperator().getType() == TokenType.EXCLAM_EQUAL) {
            if (left.getClass() != right.getClass()) {
                throw new RuntimeError(binary.getOperator(), "Cannot compare two objects of different types!", binary.getOperator().getLine());
            }
            return !isEqual(left, right);
        }
        return null;
    }

    public boolean isEqual(Object a, Object b) {
        return a.equals(b);
    }

    @Override
    public Object visitGrouping(Expression.Grouping grouping) {
        return eval(grouping.getExpression());
    }

    @Override
    public Object visitLiteral(Expression.Literal literal) {
        return literal.getValue();
    }

    @Override
    public Object visitNegator(Expression.Negator negator) {
        Object right = eval(negator.getRight());
        if (negator.getOperator().getType() == TokenType.MINUS) {
            checkIfNum(negator.getOperator(), right);
            return -(long) right; // negative value
        }
        if (negator.getOperator().getType() == TokenType.EXCLAM) {
            return !(boolean) right;
        }
        return null;
    }

    @Override
    public Object visitVariable(Expression.Variable variable) {
        return vars.grab(variable.getName().getLexeme(), variable.getName().getLine());
    }

    public Object eval(Expression expression) {
        return expression.accept(this);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object visitAssign(Expression.Assign assign) {
        Object val = eval(assign.getVal());
        int index;
        if (assign.getIndex() != null) {
            if (assign.getIndex().getType() == TokenType.IDENTIFIER) {
                index = ((Long)vars.grab(assign.getIndex().getLexeme(), assign.getIndex().getLine())).intValue();
                vars.assignArr(assign.getName().getLexeme(), val, index, assign.getName().getLine());
            }
            else if (assign.getIndex().getType() == TokenType.PLUS) {
                Token arr = assign.getName();
                List<Object> list = (List<Object>)vars.grab(arr.getLexeme(), arr.getLine());
                index = list.size();
                vars.assignArr(assign.getName().getLexeme(), val, index, assign.getName().getLine());
            }
            else if (assign.getIndex().getType() == TokenType.MINUS) {
                Token arr = assign.getName();
                List<Object> list = (List<Object>)vars.grab(arr.getLexeme(), arr.getLine());
                try {
                    list.remove(((Long)val).intValue());
                } catch (IndexOutOfBoundsException ex) {
                    throw new RuntimeError(assign.getName(), "Index " + val + " out of bounds!", assign.getName().getLine());
                }
            }
            else if (assign.getIndex().getType() == TokenType.NUMBER) {
                index = ((Long)assign.getIndex().getLiteral()).intValue();
                vars.assignArr(assign.getName().getLexeme(), val, index, assign.getName().getLine());
            }
        }
        else {
            vars.assignVar(assign.getName().getLexeme(), val, assign.getName().getLine());
        }
        return val;
    }

    @Override
    public Object visitLogic(Expression.Logic logic) {
        Object left = eval(logic.getLeft());
        if (logic.getOperator().getType() == TokenType.OR) {
            if ((Boolean) left) {
                return left;
            }
        }
        else {
            if (!(Boolean) left) {
                return left;
            }
        }
        return eval(logic.getRight());
    }

    @Override
    public Object visitInput(Expression.Input input) {
        Scanner in = new Scanner(System.in);
        String temp = in.nextLine();
        boolean isNum = true;
        for (int i = 0; i < temp.length(); i++) {
            if (Character.isLetter(temp.charAt(i))) {
                isNum = false;
                i = temp.length();
            }
        }
        Object val = temp;
        if (isNum) {
            val = Long.valueOf(temp);
        }
        input.setValue(val);
        return val;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object visitArrayAccess(Expression.ArrayAccess arrAccess) {
        int index;
        if (arrAccess.getIndex().getType() == TokenType.IDENTIFIER) {
            index = ((Long)vars.grab(arrAccess.getIndex().getLexeme(), arrAccess.getIndex().getLine())).intValue();
            return vars.grabArr(arrAccess.getName().getLexeme(), index, arrAccess.getName().getLine());
        }
        else if (arrAccess.getIndex().getType() == TokenType.PLUS) {
            throw new RuntimeError(arrAccess.getIndex(), "Cannot invoke '+' operator to access arrays!", arrAccess.getIndex().getLine());
        }
        else if (arrAccess.getIndex().getType() == TokenType.MINUS) {
            Token arr = arrAccess.getName();
            List<Object> list = (List<Object>)vars.grab(arr.getLexeme(), arr.getLine());
            return list.remove(list.size() - 1);
        }
        else {
            index = ((Long)arrAccess.getIndex().getLiteral()).intValue();
            return vars.grabArr(arrAccess.getName().getLexeme(), index, arrAccess.getName().getLine());
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object visitArrayLength(Expression.ArrayLength arrayLen) {
        Token arr = arrayLen.getName();
        List<Object> list = (List<Object>)vars.grab(arr.getLexeme(), arr.getLine());
        return Long.valueOf(list.size());
    }

    @Override
    public Object visitExpressionStmt(Statement.ExprStmt stmt) {
        eval(stmt.getExpression());
        return null;
    }

    @Override
    public Object visitDispStmt(Statement.Disp stmt) {
        Object val = eval(stmt.getExpression());
        System.out.println(toString(val));
        return null;
    }

    @Override
    public Object visitVarStmt(Statement.Var stmt) {
        Object val = null;
        if (stmt.getInit() != null) { // var has been initialized
            val = eval(stmt.getInit());
        }
        vars.define(stmt.getName().getLexeme(), val);
        return null;
    }

    @Override
    public Object visitBlockStmt(Statement.Block stmt) {
        runBlock(stmt.getStatements(), new Storage(vars));
        return null;
    }

    public void runBlock(List<Statement> statements, Storage vars) {
        Storage temp = this.vars;
        this.vars = vars;
        for (Statement statement : statements) {
            run(statement);
        }
        this.vars = temp;
    }

    @Override
    public Object visitIfStmt(Statement.IfStmt stmt) {
        if (eval(stmt.getExpression()).getClass() == Boolean.class) {
            if ((Boolean) eval(stmt.getExpression())) {
                run(stmt.getIfBranch());
            } 
            else if (stmt.getElseBranch() != null ) {
                run(stmt.getElseBranch());
            }
        }
        return null;
    }

    @Override
    public Object visitWhileStmt(Statement.WhileStmt stmt) {
        while ((Boolean) eval(stmt.getCondition())) {
            run(stmt.getBody());
        }
        return null;
    }
}