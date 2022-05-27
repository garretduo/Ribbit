/* Garret Duo
AP Computer Science A
Period 4
Compiler Project */

import java.util.*;

class Tokenizer {
    private List<Token> tokenList;
    private String source;
    private int line;

    public Tokenizer(String source) {
        tokenList = new ArrayList<Token>();
        this.source = source;
        line = 1;
    }

    public void scan() {
        int srcLen = source.length();
        for (int i = 0; i < srcLen; i++) {
            char cur = source.charAt(i);
            if (cur == '#') {
                while (source.charAt(i) != '\n') {
                    i++;
                }
            }
            else if (cur == '@') {
                while (i < srcLen - 1 && source.charAt(i + 1) != '@') {
                    if (source.charAt(i) == '\n') {
                        line++;
                    }
                    i++;
                }
                i++;
                if (i >= srcLen) {
                    Ribbit.error(line, "Expected '@' to end comment!");
                    return;
                }
            }
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
            else if (cur == ' ' || cur == '\r' || cur == '\t') {
                continue;
            }
            else if (cur == '\n') {
                line++;
                continue;
            }
            else if (cur == '"') {
                i = makeString(i);
                if (i == -1) {
                    return;
                }
            }
            else if (isDigit(cur)) {
                long x = makeNumber(i);
                if (x != -1) {
                    int y = String.valueOf(x).length() - 1;
                    i += y;
                }
                if (i + 1 < source.length() && Character.isLetter(source.charAt(i + 1))) {
                    Ribbit.error(line, "Cannot declare or reference variable with non-letter starting character!");
                }
            }
            else {
                i = scanKeyword(i) - 1;
            }
        }
    }

    public int makeString(int start) {
        int end = -1;
        for (int i = start + 1; i < source.length(); i++) {
            if (source.charAt(i) == '"') {
                end = i;
                i = source.length();
            }
        }
        if (end == -1) {
            Ribbit.error(line, "Unterminated string.");
            return -1;
        }
        String val = source.substring(start + 1, end);
        tokenList.add(new Token(TokenType.STRING, val, val, line));
        return end;
    }

    public boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    public long makeNumber(int start) {
        int end = -1;
        for (int i = start + 1; i <= source.length(); i++) {
            if (i == source.length()) {
                i = source.length() + 1;
                end = i - 1;
            }
            else if (!isDigit(source.charAt(i))) {
                end = i;
                break;
            }
        }
        long longVal = -1;
        if (end > start) {
            String val = source.substring(start, end).trim();
            try {
                longVal = Long.valueOf(val);
                tokenList.add(new Token(TokenType.NUMBER, val, longVal, line));
            } catch (NumberFormatException ex) {
                Ribbit.error(line, "Integer out of bounds!");
            }
        }
        return longVal;
    }

    public int scanKeyword(int start) {
        String cur = "";
        int i = start;
        for (; i < source.length() && !isWhitespace(source.charAt(i)) && Character.isLetterOrDigit(source.charAt(i)); i++) {
            cur += source.substring(i, i + 1);
        }
        Token temp = new Token(TokenType.IDENTIFIER, cur, null, line);
        String[] reserved = {"if", "else", "and", "or", "true", "false", "disp", "input", "return", "let", "for", "while", "then", "do"};
        TokenType[] resTokenType = {TokenType.IF, TokenType.ELSE, TokenType.AND, TokenType.OR, TokenType.TRUE, TokenType.FALSE, TokenType.DISP, TokenType.INPUT, TokenType.RETURN, TokenType.LET, TokenType.FOR, TokenType.WHILE, TokenType.THEN, TokenType.DO};
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