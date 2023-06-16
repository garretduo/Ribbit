/* Garret Duo
AP Computer Science A
Period 4
Compiler Project */

class RuntimeError extends RuntimeException {

    // custom runtime error class

    private final Token token;
    private final String tokenStr;
    private final int line;

    public RuntimeError(Token token, String message, int line) { // for normal runtime error reporting
        super(message);
        this.token = token;
        tokenStr = null;
        this.line = line;
    }

    public RuntimeError(String tokenStr, String message, int line) { // for storage error reporting
        super(message);
        token = null;
        this.tokenStr = tokenStr;
        this.line = line;
    }

    public Token getToken() {
        return token;
    }

    public String getTokenStr() {
        return tokenStr;
    }

    public int getLine() {
        return line;
    }
}