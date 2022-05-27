/* Garret Duo
AP Computer Science A
Period 4
Compiler Project */

import java.util.*;

class Parser {
    private static class ParseError extends RuntimeException {}
    private final List<Token> tokens;
    private int cur = 0;

    Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public List<Statement> parse() {
        try {
            List<Statement> statements = new ArrayList<Statement>();
            while (cur < tokens.size()) {
                statements.add(declare());
            }
            return statements;
        }
        catch (ParseError ex) {
            return null;
        }
    }

    private Token consume(TokenType type, String message) {
        if (cur < tokens.size() && tokens.get(cur).getType() == type) {
            return tokens.get(cur++);
        }
        throw error(tokens.get(cur - 2), message);
    }

    private ParseError error(Token token, String message) {
        Ribbit.error(token, message);
        return new ParseError();
    }

    private Expression expression() {
        return assign();
    }

    private Expression assign() {
        Expression expression = or();
        if (match(TokenType.EQUAL)) {
            Expression val = assign();
            if (expression.getClass() == Expression.Variable.class) {
                Token name = ((Expression.Variable)(expression)).getName();
                return new Expression.Assign(name, val);
            }
            if (expression.getClass() == Expression.ArrayAccess.class) {
                Token name = ((Expression.ArrayAccess)(expression)).getName();
                return new Expression.Assign(name, val, ((Expression.ArrayAccess)(expression)).getIndex());
            }
            error(tokens.get(cur - 1), "Cannot assign value to target!");
        }
        if (match(TokenType.PLUS_EQUAL, TokenType.MINUS_EQUAL, TokenType.STAR_EQUAL, TokenType.SLASH_EQUAL, TokenType.MOD_EQUAL)) {
            Token operator = tokens.get(cur - 1);
            Expression val = null;
            Token name = null;
            if (expression.getClass() == Expression.Variable.class) {
                if (operator.getType() == TokenType.PLUS_EQUAL) {
                    Token op = new Token(TokenType.PLUS, "+", null, operator.getLine());
                    val = new Expression.Binary(expression, op, primary());
                    name = ((Expression.Variable)(expression)).getName();
                }
                if (operator.getType() == TokenType.MINUS_EQUAL) {
                    Token op = new Token(TokenType.MINUS, "-", null, operator.getLine());
                    val = new Expression.Binary(expression, op, primary());
                    name = ((Expression.Variable)(expression)).getName();
                }
                if (operator.getType() == TokenType.STAR_EQUAL) {
                    Token op = new Token(TokenType.STAR, "*", null, operator.getLine());
                    val = new Expression.Binary(expression, op, primary());
                    name = ((Expression.Variable)(expression)).getName();
                }
                if (operator.getType() == TokenType.SLASH_EQUAL) {
                    Token op = new Token(TokenType.SLASH, "/", null, operator.getLine());
                    val = new Expression.Binary(expression, op, primary());
                    name = ((Expression.Variable)(expression)).getName();
                }
                if (operator.getType() == TokenType.MOD_EQUAL) {
                    Token op = new Token(TokenType.MOD, "%", null, operator.getLine());
                    val = new Expression.Binary(expression, op, primary());
                    name = ((Expression.Variable)(expression)).getName();
                }
                return new Expression.Assign(name, val);
            }
            if (expression.getClass() == Expression.ArrayAccess.class) {
                if (operator.getType() == TokenType.PLUS_EQUAL) {
                    Token op = new Token(TokenType.PLUS, "+", null, operator.getLine());
                    val = new Expression.Binary(expression, op, primary());
                    name = ((Expression.ArrayAccess)(expression)).getName();
                }
                if (operator.getType() == TokenType.MINUS_EQUAL) {
                    Token op = new Token(TokenType.MINUS, "-", null, operator.getLine());
                    val = new Expression.Binary(expression, op, primary());
                    name = ((Expression.ArrayAccess)(expression)).getName();
                }
                if (operator.getType() == TokenType.STAR_EQUAL) {
                    Token op = new Token(TokenType.STAR, "*", null, operator.getLine());
                    val = new Expression.Binary(expression, op, primary());
                    name = ((Expression.ArrayAccess)(expression)).getName();
                }
                if (operator.getType() == TokenType.SLASH_EQUAL) {
                    Token op = new Token(TokenType.SLASH, "/", null, operator.getLine());
                    val = new Expression.Binary(expression, op, primary());
                    name = ((Expression.ArrayAccess)(expression)).getName();
                }
                if (operator.getType() == TokenType.MOD_EQUAL) {
                    Token op = new Token(TokenType.MOD, "%", null, operator.getLine());
                    val = new Expression.Binary(expression, op, primary());
                    name = ((Expression.ArrayAccess)(expression)).getName();
                }
                return new Expression.Assign(name, val, ((Expression.ArrayAccess)(expression)).getIndex());            
            }
            error(tokens.get(cur - 1), "Cannot assign value to target!");
        }
        return expression;
    }

    private Expression or() {
        Expression expression = and();
        while(match(TokenType.OR)) {
            Token operator = tokens.get(cur - 1);
            Expression right = and();
            expression = new Expression.Logic(expression, operator, right);
        }
        return expression;
    }

    private Expression and() {
        Expression expression = equality();
        while(match(TokenType.AND)) {
            Token operator = tokens.get(cur - 1);
            Expression right = equality();
            expression = new Expression.Logic(expression, operator, right);
        }
        return expression;
    }

    private Expression equality() {
        Expression expression = comparison();
        while (match(TokenType.EQUAL_EQUAL, TokenType.EXCLAM_EQUAL)) {
            Token operator = tokens.get(cur - 1);
            Expression right = comparison();
            expression = new Expression.Binary(expression, operator, right);
        }
        return expression;
    }

    private Expression comparison() {
        Expression expression = term();
        while (match(TokenType.GREATER, TokenType.GREATER_EQUAL, TokenType.LESS, TokenType.LESS_EQUAL)) {
            Token operator = tokens.get(cur - 1);
            Expression right = term();
            expression = new Expression.Binary(expression, operator, right);
        }
        return expression;
    }

    private Expression term() {
        Expression expression = factor();
        while (match(TokenType.PLUS, TokenType.MINUS)) {
            Token operator = tokens.get(cur - 1);
            Expression right = factor();
            expression = new Expression.Binary(expression, operator, right);
        }
        return expression;
    }

    private Expression factor() {
        Expression expression = negator();
        while (match(TokenType.STAR, TokenType.SLASH, TokenType.MOD)) {
            Token operator = tokens.get(cur - 1);
            Expression right = negator();
            expression = new Expression.Binary(expression, operator, right);
        }
        return expression;
    }

    private Expression negator() {
        if (match(TokenType.EXCLAM, TokenType.MINUS)) {
            Token operator = tokens.get(cur - 1);
            Expression right = negator();
            Expression expression = new Expression.Negator(operator, right);
            return expression;
        }
        return primary();
    }

    private Expression primary() {
        if (match(TokenType.IDENTIFIER)) {
            if (match(TokenType.L_BRACE)) {
                if (match(TokenType.NUMBER, TokenType.IDENTIFIER, TokenType.PLUS, TokenType.MINUS)) {
                    Token index = tokens.get(cur - 1);
                    Token name = tokens.get(cur - 3);
                    consume(TokenType.R_BRACE, "Expected closing '}'!");
                    return new Expression.ArrayAccess(index, name);
                }
                else if (match(TokenType.DOLLAR)) {
                    Token name = tokens.get(cur - 3);
                    consume(TokenType.R_BRACE, "Expected closing '}'!");
                    return new Expression.ArrayLength(name);
                }
                consume(TokenType.NUMBER, "Expected valid index for list!");
                consume(TokenType.R_BRACE, "Expected closing '}'!");
            }
            return new Expression.Variable(tokens.get(cur - 1));
        }
        if (match(TokenType.FALSE)) {
            return new Expression.Literal(false);
        }
        if (match(TokenType.TRUE)) {
            return new Expression.Literal(true);
        }
        if (match(TokenType.NUMBER, TokenType.STRING)) {
            return new Expression.Literal(tokens.get(cur - 1).getLiteral());
        }
        if (match(TokenType.INPUT)) {
            return new Expression.Input(null);
        }
        if (match(TokenType.L_PAREN)) {
            Expression expression = expression();
            consume(TokenType.R_PAREN, "Expected closing ')'!");
            return new Expression.Grouping(expression);
        }
        if (match(TokenType.L_BRACKET)) {
            int size;
            if (match(TokenType.R_BRACKET)) {
                size = 0;
            }
            else {
                size = ((Long)(consume(TokenType.NUMBER, "Expected size declaration for list!").getLiteral())).intValue();
                consume(TokenType.R_BRACKET, "Expected closing ']'!");
            }
            ArrayList<Object> list = new ArrayList<Object>(size);
            for (int i = 0; i < size; i++) {
                list.add(0);
            }
            return new Expression.Literal(list);
        }
        throw error(tokens.get(cur), "Unrecognized expression!");
    }

    private boolean match(TokenType... types) {
        for (TokenType type : types) {
            if (cur < tokens.size()) {
                if (tokens.get(cur).getType() == type) {
                    cur++;
                    return true;
                }
            } 
        }
        return false;
    }

    private Statement declare() {
        if (match(TokenType.LET)) {
            return varDeclare();
        }
        return statement();
    }

    private Statement varDeclare() {
        Token name = null;
        if (match(TokenType.IDENTIFIER)) {
            name = tokens.get(cur - 1);
        }
        Expression init = null;
        if (match(TokenType.EQUAL)) {
            init = expression();
        }
        return new Statement.Var(name, init);
    }

    private Statement statement() {
        if (match(TokenType.FOR)) {
            return forStmt();
        }
        if (match(TokenType.IF)) {
            return ifStmt();
        }
        if (match(TokenType.DISP)) {
            return dispStmt();
        }
        if (match(TokenType.WHILE)) {
            return whileStmt();
        }
        if (match(TokenType.COLON)) {
            return new Statement.Block(getBlock());
        }
        return expressionStmt();
    }
    
    private Statement forStmt() { // loop initializer, condition, incremenent do
        consume(TokenType.L_PAREN, "Expected '(' after for statement!");
        Statement init;
        if (match(TokenType.COMMA)) {
            init = null;
        }  
        else if (match(TokenType.LET)) {
            init = varDeclare();
        }
        else {
            init = expressionStmt();
        }
        consume(TokenType.COMMA, "Expected ',' after initializer statement!");
        Expression cond = null;
        if (tokens.get(cur).getType() != TokenType.COMMA) {
            cond = expression();
        }
        consume(TokenType.COMMA, "Expected ',' after loop condition!");
        Expression increment = null;
        if (tokens.get(cur).getType() != TokenType.DO) {
            increment = expression();
        }
        consume(TokenType.R_PAREN, "Expected ')' after for statement!");
        consume(TokenType.DO, "Expected 'do' after for clauses!");
        Statement body = statement();
        if (increment != null) {
            body = new Statement.Block(Arrays.asList(body, new Statement.ExprStmt(increment)));
        }
        if (cond == null) {
            cond = new Expression.Literal(true);
        }
        body = new Statement.WhileStmt(cond, body);
        if (init != null) {
            body = new Statement.Block(Arrays.asList(init, body));
        }
        return body;
    }

    private Statement ifStmt() {
        Expression condition = expression();
        consume(TokenType.THEN, "Expected 'then' after if condition!");
        Statement ifBranch = statement();
        Statement elseBranch = null;
        if (match(TokenType.ELSE)) {
            elseBranch = statement();
        }
        return new Statement.IfStmt(condition, ifBranch, elseBranch);
    }

    private Statement dispStmt() {
        Expression val = expression();
        return new Statement.Disp(val);
    }

    private Statement whileStmt() {
        Expression condition = expression();
        if (condition.getClass() == Expression.Literal.class && (Boolean)((Expression.Literal)condition).getValue() == true) {
            Ribbit.error(tokens.get(cur - 1), "Infinite loop error!");
            throw new ParseError();
        }
        consume(TokenType.DO, "Expected 'do' after while condition!");
        Statement body = statement();
        return new Statement.WhileStmt(condition, body);
    }

    private Statement expressionStmt() {
        Expression expression = expression();
        return new Statement.ExprStmt(expression);
    }

    private List<Statement> getBlock() {
        List<Statement> statements = new ArrayList<Statement>();
        while (cur < tokens.size() && tokens.get(cur).getType() != TokenType.SEMICOLON) {
            statements.add(declare());
        }
        consume(TokenType.SEMICOLON, "Expected ';' to close block statement!");
        return statements;
    }
}