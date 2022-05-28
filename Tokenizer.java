/* Garret Duo
AP Computer Science A
Period 4
Compiler Project */

import java.util.*;

class Tokenizer {

    // converts source code string into tokens

    private List<Token> tokenList;
    private String source;
    private int line; // keeps track of line for error reporting purposes

    public Tokenizer(String source) {
        tokenList = new ArrayList<Token>();
        this.source = source;
        line = 1;
    }

    public void scan() {
        int srcLen = source.length();
        for (int i = 0; i < srcLen; i++) {
            char cur = source.charAt(i);
            if (cur == '#') { // single-line comments; ignore text until next line
                while (i < srcLen && source.charAt(i) != '\n') {
                    i++;
                }
                line++;
            }
            else if (cur == '@') { // multi-line comments; ignore text until next occurrence of '@'
                i++;
                while (i < srcLen && source.charAt(i) != '@') {
                    if (source.charAt(i) == '\n') {
                        line++;
                    }
                    i++;
                }
                if (i >= srcLen) {
                    Ribbit.error(line, "Expected '@' to end comment!"); // reached end of code but no closing '@'
                    return;
                }
            }
            // next two branches deal with ignoring whitespaces
            else if (cur == ' ' || cur == '\r' || cur == '\t') {
                continue;
            }
            else if (cur == '\n') {
                line++; // tracking our lines accurately
                continue;
            }
            // everything below is single-character token scanning
            else if (cur == ':') {
                tokenList.add(new Token(TokenType.COLON, ":", null, line));
            }
            else if (cur == ';') {
                tokenList.add(new Token(TokenType.SEMICOLON, ";", null, line));
            }
            else if (cur == '(') {
                tokenList.add(new Token(TokenType.L_PAREN, "(", null, line));
            }
            else if (cur == ')') {
                tokenList.add(new Token(TokenType.R_PAREN, ")", null, line));
            }
            else if (cur == ',') {
                tokenList.add(new Token(TokenType.COMMA, ",", null, line));
            }
            else if (cur == '[') {
                tokenList.add(new Token(TokenType.L_BRACKET, "[", null, line));
            }
            else if (cur == ']') {
                tokenList.add(new Token(TokenType.R_BRACKET, "]", null, line));
            }
            else if (cur == '{') {
                tokenList.add(new Token(TokenType.L_BRACE, "{", null, line));
            }
            else if (cur == '}') {
                tokenList.add(new Token(TokenType.R_BRACE, "}", null, line));
            }
            else if (cur == '$') {
                tokenList.add(new Token(TokenType.DOLLAR, "$", null, line));
            }
            else if (cur == '+' && i + 1 != srcLen && source.charAt(i + 1) != '=') {
                tokenList.add(new Token(TokenType.PLUS, "+", null, line));
            }
            else if (cur == '-' && i + 1 != srcLen && source.charAt(i + 1) != '=') {
                tokenList.add(new Token(TokenType.MINUS, "-", null, line));
            }
            else if (cur == '*' && i + 1 != srcLen && source.charAt(i + 1) != '=') {
                tokenList.add(new Token(TokenType.STAR, "*", null, line));
            }
            else if (cur == '/' && i + 1 != srcLen && source.charAt(i + 1) != '=') {
                tokenList.add(new Token(TokenType.SLASH, "/", null, line));
            }
            else if (cur == '%' && i + 1 != srcLen && source.charAt(i + 1) != '=') {
                tokenList.add(new Token(TokenType.MOD, "%", null, line));
            }
            // everything below is double-character token scanning
            else if (cur == '+' && i + 1 != srcLen && source.charAt(i + 1) == '=') {
                tokenList.add(new Token(TokenType.PLUS_EQUAL, "+=", null, line));
                i++;
            }
            else if (cur == '-' && i + 1 != srcLen && source.charAt(i + 1) == '=') {
                tokenList.add(new Token(TokenType.MINUS_EQUAL, "-=", null, line));
                i++;
            }
            else if (cur == '*' && i + 1 != srcLen && source.charAt(i + 1) == '=') {
                tokenList.add(new Token(TokenType.STAR_EQUAL, "*=", null, line));
                i++;
            }
            else if (cur == '/' && i + 1 != srcLen && source.charAt(i + 1) == '=') {
                tokenList.add(new Token(TokenType.SLASH_EQUAL, "/=", null, line));
                i++;
            }
            else if (cur == '%' && i + 1 != srcLen && source.charAt(i + 1) == '=') {
                tokenList.add(new Token(TokenType.MOD_EQUAL, "%=", null, line));
                i++;
            }
            // nvm, some more single-character token scanning
            else if (cur == '!' && i + 1 != srcLen && source.charAt(i + 1) != '=') {
                tokenList.add(new Token(TokenType.EXCLAM, "!", null, line));
            }
            else if (cur == '=' && i + 1 != srcLen && source.charAt(i + 1) != '=') {
                tokenList.add(new Token(TokenType.EQUAL, "=", null, line));
            }
            else if (cur == '>' && i + 1 != srcLen && source.charAt(i + 1) != '=') {
                tokenList.add(new Token(TokenType.GREATER, ">", null, line));
            }
            else if (cur == '<' && i + 1 != srcLen && source.charAt(i + 1) != '=') {
                tokenList.add(new Token(TokenType.LESS, "<", null, line));
            }
            // back to double-character token scanning
            else if (cur == '!' && i + 1 != srcLen && source.charAt(i + 1) == '=') {
                tokenList.add(new Token(TokenType.EXCLAM_EQUAL, "!=", null, line));
                i++;
            }
            else if (cur == '=' && i + 1 != srcLen && source.charAt(i + 1) == '=') {
                tokenList.add(new Token(TokenType.EQUAL_EQUAL, "==", null, line));
                i++;
            }
            else if (cur == '>' && i + 1 != srcLen && source.charAt(i + 1) == '=') {
                tokenList.add(new Token(TokenType.GREATER_EQUAL, ">=", null, line));
                i++;
            }
            else if (cur == '<' && i + 1 != srcLen && source.charAt(i + 1) == '=') {
                tokenList.add(new Token(TokenType.LESS_EQUAL, ">=", null, line));
                i++;
            }
            else if (cur == '"') { // '"' signifies the beginning of a string
                i = makeString(i); // call string helper method
                if (i == -1) { // if makeString returns -1, string never ended
                    return;
                }
            }
            else if (isDigit(cur)) { // any digit signifies the beginning of a number (unless enclosed by quotes)
                long x = makeNumber(i); // call number helper method
                if (x != -1) { // if makeNumber returns -1, error encountered
                    int y = String.valueOf(x).length() - 1;
                    i += y; // lets us know length of number to advance pointer by
                }
                if (i + 1 < source.length() && Character.isLetter(source.charAt(i + 1))) { // variables like '1a' are disallowed
                    Ribbit.error(line, "Cannot declare or reference variable with non-letter starting character!");
                }
            }
            else {
                i = scanKeyword(i) - 1; // call keyword/identifier helper method
            }
        }
    }

    public int makeString(int start) {
        int end = -1;
        int newLines = 0;
        for (int i = start + 1; i < source.length(); i++) { // keeps iterating until another '"' encountered
            if (source.charAt(i) == '\n') {
                newLines++;
            }
            if (source.charAt(i) == '"') {
                end = i;
                i = source.length();
            }
        }
        if (end == -1) { // never found a closing '"'
            Ribbit.error(line, "Unterminated string.");
            return -1;
        }
        String val = source.substring(start + 1, end); // creates the string between opening and ending '"'
        line += newLines;
        tokenList.add(new Token(TokenType.STRING, val, val, line));
        return end;
    }

    public boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    public long makeNumber(int start) {
        int end = -1;
        int newLines = 0;
        for (int i = start + 1; i <= source.length(); i++) {
            if (source.charAt(i) == '\n') {
                newLines++;
            }
            if (i == source.length()) { // if reached end, terminate number
                i = source.length() + 1;
                end = i - 1;
            }
            else if (!isDigit(source.charAt(i))) { // if found non-digit, terminate number
                end = i;
                break;
            }
        }
        long longVal = -1; // opted for long to circumvent IntegerOutOfBoundsExceptions
        if (end > start) {
            String val = source.substring(start, end).trim();
            try {
                longVal = Long.valueOf(val);
                line += newLines;
                tokenList.add(new Token(TokenType.NUMBER, val, longVal, line));
            } catch (NumberFormatException ex) {
                Ribbit.error(line, "Integer out of bounds!");
            }
        }
        return longVal;
    }

    public int scanKeyword(int start) {
        int newLines = 0;
        String cur = "";
        int i = start;
        for (; i < source.length() && !isWhitespace(source.charAt(i)) && Character.isLetterOrDigit(source.charAt(i)); i++) { // assume all are identifiers first
            if (source.charAt(i) == '\n') {
                newLines++;
            }
            cur += source.substring(i, i + 1);
        }
        line += newLines;
        Token temp = new Token(TokenType.IDENTIFIER, cur, null, line);
        // if identifier is any of reserved, switch type to keyword instead
        String[] reserved = {"if", "else", "and", "or", "true", "false", "disp", "displn", "input", "return", "let", "for", "while", "then", "do"};
        TokenType[] resTokenType = {TokenType.IF, TokenType.ELSE, TokenType.AND, TokenType.OR, TokenType.TRUE, TokenType.FALSE, TokenType.DISP, TokenType.DISPLN, TokenType.INPUT, TokenType.RETURN, TokenType.LET, TokenType.FOR, TokenType.WHILE, TokenType.THEN, TokenType.DO};
        String lexeme = temp.getLexeme();
        for (int j = 0; j < reserved.length; j++) {
            if (reserved[j].equals(lexeme)) {
                temp.setType(resTokenType[j]);
                j = reserved.length;
            }
        }
        tokenList.add(temp);
        return i;
    }

    public boolean isWhitespace(char c) {
        return c == ' ' || c == '\r' || c == '\t' || c == '\n';
    }

    public List<Token> getTokens() {
        return tokenList;
    }
}