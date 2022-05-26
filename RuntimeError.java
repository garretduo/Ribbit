/* Garret Duo
AP Computer Science A
Period 4
Compiler Project */

class RuntimeError extends RuntimeException {
    private final Token token;
    private final String tokenStr;
    private final int line;

    public RuntimeError(Token token, String message, int line) {
        super(message);
        this.token = token;
        tokenStr = null;
        this.line = line;
    }

    public RuntimeError(String tokenStr, String message, int line) {
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