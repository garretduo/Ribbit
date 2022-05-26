/* Garret Duo
AP Computer Science A
Period 4
Compiler Project */

abstract class Expression {
    public abstract <R> R accept(Visitor<R> visitor);

    public static class Binary extends Expression {
        private final Expression left;
        private final Token operator;
        private final Expression right;

        public Binary(Expression left, Token operator, Expression right) { // expr == expr
            this.left = left;
            this.operator = operator;
            this.right = right;
        }

        public <Binary> Binary accept(Visitor<Binary> visitor) {
            return visitor.visitBinary(this);
        }

        public Expression getLeft() {
            return left;
        }

        public Token getOperator() {
            return operator;
        }
        
        public Expression getRight() {
            return right;
        }
    }

    public static class Grouping extends Expression {
        private final Expression expression;

        public Grouping(Expression expression) {
            this.expression = expression;
        }

        public <Grouping> Grouping accept(Visitor<Grouping> visitor) {
            return visitor.visitGrouping(this);
        }

        public Expression getExpression() {
            return expression;
        }
    }

    public static class Literal extends Expression {
        private final Object value;

        public Literal(Object value) {
            this.value = value;
        }

        public <Literal> Literal accept(Visitor<Literal> visitor) {
            return visitor.visitLiteral(this);
        }

        public Object getValue() {
            return value;
        }
    }

    public static class Negator extends Expression {
        private final Token operator;
        private final Expression right;

        public Negator(Token operator, Expression right) {
            this.operator = operator;
            this.right = right;
        }

        public <Negator> Negator accept(Visitor<Negator> visitor) {
            return visitor.visitNegator(this);
        }

        public Token getOperator() {
            return operator;
        }

        public Expression getRight() {
            return right;
        }
    }

    public static class Variable extends Expression {
        private final Token name;

        public Variable(Token name) {
            this.name = name;
        }

        public <Variable> Variable accept(Visitor<Variable> visitor) {
            return visitor.visitVariable(this);
        }

        public Token getName() {
            return name;
        }
    }

    public static class Assign extends Expression {
        private Token name;
        private Expression val;
        private Token index;

        public Assign(Token name, Expression val) {
            this.name = name;
            this.val = val;
            this.index = null;
        }

        public Assign(Token name, Expression val, Token index) {
            this.name = name;
            this.val = val;
            this.index = index;
        }

        @Override
        public <R> R accept(Expression.Visitor<R> visitor) {
            return visitor.visitAssign(this);
        }

        public Token getName() {
            return name;
        }

        public Expression getVal() {
            return val;
        }

        public Token getIndex() {
            return index;
        }
    }

    public static class Logic extends Expression {
        private final Expression left;
        private final Token operator;
        private final Expression right;

        public Logic(Expression left, Token operator, Expression right) {
            this.left = left;
            this.operator = operator;
            this.right = right;
        }

        @Override
        public <R> R accept(Expression.Visitor<R> visitor) {
            return visitor.visitLogic(this);
        }

        public Expression getLeft() {
            return left;
        }

        public Token getOperator() {
            return operator;
        }

        public Expression getRight() {
            return right;
        }
    }

    public static class Input extends Expression {
        private Object value;

        public Input(Object value) {
            this.value = value;
        }

        public <Input> Input accept(Visitor<Input> visitor) {
            return visitor.visitInput(this);
        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }
    }

    public static class ArrayAccess extends Expression {
        private final Token index;
        private final Token name;

        public ArrayAccess(Token index, Token name) {
            this.index = index;
            this.name = name;
        }

        @Override
        public <R> R accept(Expression.Visitor<R> visitor) {
            return visitor.visitArrayAccess(this);
        }

        public Token getIndex() {
            return index;
        }

        public Token getName() {
            return name;
        }
    }

    public static class ArrayLength extends Expression {
        private final Token name;

        public ArrayLength(Token name) {
            this.name = name;
        }

        @Override
        public <R> R accept(Expression.Visitor<R> visitor) {
            return visitor.visitArrayLength(this);
        }

        public Token getName() {
            return name;
        }
    }

    public interface Visitor<R> {
        public R visitBinary(Binary binary);
        public R visitGrouping(Grouping grouping);
        public R visitLiteral(Literal literal);
        public R visitNegator(Negator negator);
        public R visitVariable(Variable variable);
        public R visitAssign(Assign assign);
        public R visitLogic(Logic logic);
        public R visitInput(Input input);
        public R visitArrayAccess(ArrayAccess arrayAcc);
        public R visitArrayLength(ArrayLength arrayLen);
    }   
}