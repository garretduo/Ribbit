/* Garret Duo
AP Computer Science A
Period 4
Compiler Project */

import java.util.*;

class Parser {

    // converts tokens into Abstract Syntax Tree

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
                statements.add(declare()); // keep adding new statements
            }
            return statements;
        }
        catch (ParseError | IndexOutOfBoundsException ex) { // deals with errors while parsing
            if (ex.getClass() == IndexOutOfBoundsException.class) {
                Token prev = tokens.get(cur - 1);
                Ribbit.error(prev, "Expected argument for '" + prev.getLexeme() + "'!");
            }
            return null;
        }
    }

    private Token consume(TokenType type, String message) { // helper method to check a token
        if (cur < tokens.size() && tokens.get(cur).getType() == type) {
            return tokens.get(cur++);
        }
        throw error(tokens.get(cur - 2), message);
    }

    private ParseError error(Token token, String message) { // reports errors to main
        Ribbit.error(token, message);
        return new ParseError();
    }

    private Expression expression() { // starts walking through the expression part of AST
        return assign(); // calls nodes with increasing precedence
    }

    private Expression assign() {
        Expression expression = or(); // going down the tree...
        if (match(TokenType.EQUAL)) { // means var assignment
            Expression val = assign();
            if (expression.getClass() == Expression.Variable.class) { // var assignment
                Expression name = (Expression.Variable)(expression);
                return new Expression.Assign(name, val);
            }
            if (expression.getClass() == Expression.ArrayAccess.class) { // array index value assignment
                Expression name = (Expression.ArrayAccess)(expression);
                return new Expression.Assign(name, val);
            }
            error(tokens.get(cur - 1), "Cannot assign value to target!"); // error if assigning to non-array/variable
        }
        if (match(TokenType.PLUS_EQUAL, TokenType.MINUS_EQUAL, TokenType.STAR_EQUAL, TokenType.SLASH_EQUAL, TokenType.MOD_EQUAL)) { // math assignment operators
            Token operator = tokens.get(cur - 1);
            Expression val = null;
            Expression name = null;
            if (expression.getClass() == Expression.Variable.class) { // if math assigning to var
                // changing binary operator based on math assignment operator
                Token op = null;
                if (operator.getType() == TokenType.PLUS_EQUAL) {
                    op = new Token(TokenType.PLUS, "+", null, operator.getLine());
                }
                if (operator.getType() == TokenType.MINUS_EQUAL) {
                    op = new Token(TokenType.MINUS, "-", null, operator.getLine());
                }
                if (operator.getType() == TokenType.STAR_EQUAL) {
                    op = new Token(TokenType.STAR, "*", null, operator.getLine());
                }
                if (operator.getType() == TokenType.SLASH_EQUAL) {
                    op = new Token(TokenType.SLASH, "/", null, operator.getLine());
                }
                if (operator.getType() == TokenType.MOD_EQUAL) {
                    op = new Token(TokenType.MOD, "%", null, operator.getLine());
                }
                // value to be operated on then assigned parsed from AST
                val = new Expression.Binary(expression, op, or());
                name = (Expression.Variable)(expression);
                return new Expression.Assign(name, val);
            }
            if (expression.getClass() == Expression.ArrayAccess.class) { // if math assigning to array
                // changing binary operator based on math assignment operator
                Token op = null;
                if (operator.getType() == TokenType.PLUS_EQUAL) {
                    op = new Token(TokenType.PLUS, "+", null, operator.getLine());
                }
                if (operator.getType() == TokenType.MINUS_EQUAL) {
                    op = new Token(TokenType.MINUS, "-", null, operator.getLine());
                }
                if (operator.getType() == TokenType.STAR_EQUAL) {
                    op = new Token(TokenType.STAR, "*", null, operator.getLine());
                }
                if (operator.getType() == TokenType.SLASH_EQUAL) {
                    op = new Token(TokenType.SLASH, "/", null, operator.getLine());
                }
                if (operator.getType() == TokenType.MOD_EQUAL) {
                    op = new Token(TokenType.MOD, "%", null, operator.getLine());
                }
                // value to be operated on then assigned parsed from AST
                val = new Expression.Binary(expression, op, or());
                name = (Expression.ArrayAccess)(expression);
                return new Expression.Assign(name, val);            
            }
            error(tokens.get(cur - 1), "Cannot assign value to target!");
        }
        return expression; // going back up the tree, from high to low precendence
    }

    private Expression or() {
        Expression expression = and(); // and further down...
        while(match(TokenType.OR)) {
            Token operator = tokens.get(cur - 1);
            Expression right = and(); // parses right-hand side for expression to evaluate with left
            expression = new Expression.Logic(expression, operator, right);
        }
        return expression; // going back up the tree, from high to low precendence
    }

    private Expression and() {
        Expression expression = equality(); // keep going.
        while(match(TokenType.AND)) {
            Token operator = tokens.get(cur - 1);
            Expression right = equality(); // parses right-hand side for expression to evaluate with left
            expression = new Expression.Logic(expression, operator, right);
        }
        return expression; // going back up the tree, from high to low precendence
    }

    private Expression equality() {
        Expression expression = comparison(); // still moving down...
        while (match(TokenType.EQUAL_EQUAL, TokenType.EXCLAM_EQUAL)) {
            Token operator = tokens.get(cur - 1);
            Expression right = comparison(); // parses right-hand side for expression to compare
            expression = new Expression.Binary(expression, operator, right);
        }
        return expression; // going back up the tree, from high to low precendence
    }

    private Expression comparison() {
        Expression expression = term(); // still moving.
        while (match(TokenType.GREATER, TokenType.GREATER_EQUAL, TokenType.LESS, TokenType.LESS_EQUAL)) {
            Token operator = tokens.get(cur - 1);
            Expression right = term(); // parses right-hand side for expression to compare
            expression = new Expression.Binary(expression, operator, right);
        }
        return expression; // going back up the tree, from high to low precendence
    }

    private Expression term() {
        Expression expression = factor(); // we're close!
        while (match(TokenType.PLUS, TokenType.MINUS)) {
            Token operator = tokens.get(cur - 1);
            Expression right = factor(); // parses right-hand side for expression to add/sub
            expression = new Expression.Binary(expression, operator, right);
        }
        return expression; // going back up the tree, from high to low precendence
    }

    private Expression factor() {
        Expression expression = negator(); // only a few more left...
        while (match(TokenType.STAR, TokenType.SLASH, TokenType.MOD)) {
            Token operator = tokens.get(cur - 1);
            Expression right = negator(); // parses right-hand side for expression to mult/div/mod
            expression = new Expression.Binary(expression, operator, right);
        }
        return expression; // going back up the tree, from high to low precendence
    }

    private Expression negator() {
        if (match(TokenType.EXCLAM, TokenType.MINUS)) { // slight difference; we negate before reaching the primary node
            Token operator = tokens.get(cur - 1);
            Expression right = negator(); // recusively parses right-hand side for any other negators and primary expressions
            Expression expression = new Expression.Negator(operator, right);
            return expression;
        }
        return primary(); // the last node!
    }

    private Expression primary() {
        if (match(TokenType.IDENTIFIER)) { // var names
            Token name = tokens.get(cur - 1);
            if (match(TokenType.L_BRACE)) { 
                if (tokens.get(cur).getType() == TokenType.R_BRACE) {
                    Ribbit.error(tokens.get(cur), "Expected index value!");
                    throw new ParseError();
                }
                Expression index = expression();
                consume(TokenType.R_BRACE, "Expected closing '}'!");
                return new Expression.ArrayAccess(index, name);
            }
            return new Expression.Variable(name);
        }
        // parses boolean, number, and string values
        if (match(TokenType.FALSE)) { 
            return new Expression.Literal(false, tokens.get(cur - 1).getLine());
        }
        if (match(TokenType.TRUE)) {
            return new Expression.Literal(true, tokens.get(cur - 1).getLine());
        }
        if (match(TokenType.NUMBER, TokenType.STRING)) {
            return new Expression.Literal(tokens.get(cur - 1).getLiteral(), tokens.get(cur - 1).getLine());
        }
        if (match(TokenType.INPUT)) { // inputs are treated as primary expressions (as they return a literal)
            return new Expression.Input(null, tokens.get(cur - 1).getLine());
        }
        if (match(TokenType.L_PAREN)) { // implements parentheses for grouping expressions
            Expression expression = expression(); // walks through expression tree again to parse enclosed expression
            consume(TokenType.R_PAREN, "Expected closing ')'!"); // checks for closing parentheses
            return new Expression.Grouping(expression);
        }
        if (match(TokenType.L_BRACKET)) { // means we are declaring/initializing array
            Expression size;
            if (tokens.get(cur).getType() == TokenType.R_BRACKET) {
                size = new Expression.Literal(0, tokens.get(cur - 1).getLine());
            }
            else {
                size = expression();
            }
            consume(TokenType.R_BRACKET, "Expected closing ']'!");
            return new Expression.Literal(new ArrList(size), tokens.get(cur - 1).getLine());
        }
        if (match(TokenType.DOLLAR)) {
            Expression right = primary();
            return new Expression.Length(right);
        }
        throw error(tokens.get(cur), "Unrecognized expression!"); // if reached here, expression is unrecognized
    }

    private boolean match(TokenType... types) { // helper method to match token types in list
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

    private Statement declare() { // beginning of AST tree! (statements)
        if (match(TokenType.LET)) {
            return varDeclare(); // if declaring var go down this path
        }
        return statement(); // otherwise keep going down tree to higher precedence
    }

    private Statement varDeclare() {
        Token name = null;
        if (match(TokenType.IDENTIFIER)) {
            name = tokens.get(cur - 1);
        }
        Expression init = null;
        if (match(TokenType.EQUAL)) {
            init = expression(); // go down expressions path in AST for initial val of var
        }
        return new Statement.Var(name, init);
    }

    private Statement statement() {
        // tree branches depending on what type of statement
        if (match(TokenType.FOR)) {
            return forStmt(); 
        }
        if (match(TokenType.IF)) {
            return ifStmt();
        }
        if (match(TokenType.DISP, TokenType.DISPLN)) {
            return dispStmt();
        }
        if (match(TokenType.WHILE)) {
            return whileStmt();
        }
        if (match(TokenType.COLON)) {
            return new Statement.Block(getBlock());
        }
        // if none matched, must be expression, so go down expression statement path
        return expressionStmt();
    }
    
    private Statement forStmt() {
        consume(TokenType.L_PAREN, "Expected '(' after for statement!"); // check for opening parentheses
        Statement init;
        if (match(TokenType.COMMA)) { // if no initializer
            init = null;
        }  
        else if (match(TokenType.LET)) { // if initializer is var declaration
            init = varDeclare();
        }
        else { // if initializer is expression
            init = expressionStmt();
        }
        consume(TokenType.COMMA, "Expected ',' after initializer statement!"); // check for dividing comma between init and cond
        Expression cond = null;
        if (tokens.get(cur).getType() != TokenType.COMMA) { // set cond to some expression
            cond = expression();
        }
        consume(TokenType.COMMA, "Expected ',' after loop condition!"); // check for dividing comma between cond and increment
        Expression increment = null;
        if (tokens.get(cur).getType() != TokenType.DO) { // set increment to some expression
            increment = expression();
        }
        consume(TokenType.R_PAREN, "Expected ')' after for statement!"); // check for ending parentheses
        consume(TokenType.DO, "Expected 'do' after for clauses!"); // check for 'do' to start loop block
        Statement body = statement(); // parses AST for statement to run in loop
        if (increment != null) { // if increment, add it in as a block at end of statement
            body = new Statement.Block(Arrays.asList(body, new Statement.ExprStmt(increment)));
        }
        if (cond == null) { // if no cond, set to always true
            cond = new Expression.Literal(true, tokens.get(cur - 1).getLine());
        }
        body = new Statement.WhileStmt(cond, body); // use while loop statement for cond
        if (init != null) { // if init exists, put it in block before body statement
            body = new Statement.Block(Arrays.asList(init, body));
        }
        return body;
    }

    private Statement ifStmt() {
        Expression condition = expression(); // parse AST for cond after "if" token
        consume(TokenType.THEN, "Expected 'then' after if condition!"); // check for 'then' to delimit condition
        Statement ifBranch = statement(); // parse AST for statement to run in branch
        Statement elseBranch = null; 
        if (match(TokenType.ELSE)) { // if else branch exists, parse AST for statement to run in branch
            elseBranch = statement();
        }
        return new Statement.IfStmt(condition, ifBranch, elseBranch);
    }

    private Statement dispStmt() {
        Token prev = tokens.get(cur - 1);
        boolean newLine = true;
        if (prev.getType() == TokenType.DISP) {
            newLine = false;
        }
        Expression val = expression(); // parse AST for expression to display
        return new Statement.Disp(val, newLine);
    }

    private Statement whileStmt() {
        Expression condition = expression(); // parse AST for cond for loop
        if (condition.getClass() == Expression.Literal.class && (Boolean)((Expression.Literal)condition).getValue() == true) { // check if cond = infinite loop
            Ribbit.error(tokens.get(cur - 1), "Infinite loop error!");
            throw new ParseError();
        }
        consume(TokenType.DO, "Expected 'do' after while condition!"); // check for 'do' to start loop block
        Statement body = statement(); // parse AST for statement to run in loop
        return new Statement.WhileStmt(condition, body);
    }

    private Statement expressionStmt() {
        Expression expression = expression(); // bridge between statement tree and expression tree
        return new Statement.ExprStmt(expression);
    }

    private List<Statement> getBlock() {
        List<Statement> statements = new ArrayList<Statement>(); // statements in a block stored as list
        while (cur < tokens.size() && tokens.get(cur).getType() != TokenType.SEMICOLON) { // while delimiting ';' not found, keep adding statements to block list
            statements.add(declare());
        }
        consume(TokenType.SEMICOLON, "Expected ';' to close block statement!"); // check if delimiting ';' exists
        return statements;
    }
}