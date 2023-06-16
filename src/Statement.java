/* Garret Duo
AP Computer Science A
Period 4
Compiler Project */

import java.util.*;

abstract class Statement {
    public abstract <R> R accept(Visitor<R> visitor);

    // fancy visitor pattern stuff that allows interpreter to access statements

    public interface Visitor<R> {
        R visitExpressionStmt(ExprStmt stmt);
        R visitDispStmt(Disp stmt);
        R visitVarStmt(Var stmt);
        R visitBlockStmt(Block stmt);
        R visitIfStmt(IfStmt stmt);
        R visitWhileStmt(WhileStmt stmt);
    }

    // a bunch of statement classes; each one contains enough info for the interpreter to execute it

    public static class ExprStmt extends Statement {
        private final Expression expression;
        
        public ExprStmt(Expression expression) {
            this.expression = expression;
        }

        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitExpressionStmt(this);
        }

        public Expression getExpression() {
            return expression;
        }
    }

    public static class Disp extends Statement {
        private final Expression expression;
        private final boolean newLine;

        public Disp(Expression expression, boolean newLine) {
            this.expression = expression;
            this.newLine = newLine;
        }

        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitDispStmt(this);
        }

        public Expression getExpression() {
            return expression;
        }

        public boolean isNewLine() {
            return newLine;
        }
    }

    public static class Var extends Statement {
        private final Token name;
        private final Expression init;

        public Var(Token name, Expression init) {
            this.name = name;
            this.init = init;
        }

        @Override
        public <R> R accept(Statement.Visitor<R> visitor) {
            return visitor.visitVarStmt(this);
        }

        public Token getName() {
            return name;
        }

        public Expression getInit() {
            return init;
        }
    }

    public static class Block extends Statement {
        private List<Statement> statements;

        public Block(List<Statement> statements) {
            this.statements = statements;
        }

        @Override
        public <R> R accept(Statement.Visitor<R> visitor) {
            return visitor.visitBlockStmt(this);
        }

        public List<Statement> getStatements() {
            return statements;
        }
    }

    public static class IfStmt extends Statement {
        private Expression expression;
        private Statement ifBranch;
        private Statement elseBranch;

        public IfStmt(Expression expression, Statement ifBranch, Statement elseBranch) {
            this.expression = expression;
            this.ifBranch = ifBranch;
            this.elseBranch = elseBranch;
        }

        @Override
        public <R> R accept(Statement.Visitor<R> visitor) {
            return visitor.visitIfStmt(this);
        }

        public Expression getExpression() {
            return expression;
        }

        public Statement getIfBranch() {
            return ifBranch;
        }

        public Statement getElseBranch() {
            return elseBranch;
        }
    }

    public static class WhileStmt extends Statement {
        private Expression condition;
        private Statement body;

        public WhileStmt(Expression condition, Statement body) {
            this.condition = condition;
            this.body = body;
        }

        @Override
        public <R> R accept(Statement.Visitor<R> visitor) {
            return visitor.visitWhileStmt(this);
        }

        public Expression getCondition() {
            return condition;
        }

        public Statement getBody() {
            return body;
        }
    }
}