/* Garret Duo
AP Computer Science A
Period 4
Compiler Project */

enum TokenType {
    // single char
    COLON, SEMICOLON, L_PAREN, R_PAREN, COMMA, PLUS, MINUS, STAR, SLASH, MOD, QUOTE, EXCLAM, EQUAL, GREATER, LESS, L_BRACKET, R_BRACKET, L_BRACE, R_BRACE, DOLLAR,

    // two char
    EXCLAM_EQUAL, EQUAL_EQUAL, GREATER_EQUAL, LESS_EQUAL,

    // literals
    STRING, NUMBER,

    // reserved keywords
    IF, ELSE, AND, OR, TRUE, FALSE, DISP, INPUT, RETURN, LET, FOR, WHILE, THEN, DO,

    // identifier
    IDENTIFIER
}