package jmri.jmrit.logixng.util.parser;

/**
 * 
 */
public enum TokenType {
    // For precedence, see: https://introcs.cs.princeton.edu/java/11precedence/
    ERROR(-1), // Invalid token, for example an identifier starting with a digit
    SAME_AS_LAST(-1), // The same token as last time
    NONE(-1), SPACE(-1), // Any space character outside of a string, like space, newline, ...
    BOOLEAN_OR(3), // ||
    BOOLEAN_AND(4), // &&
    BINARY_OR(5), // |
    BINARY_XOR(6), // ^
    BINARY_AND(7), // &
    EQUAL(8), // ==
    NOT_EQUAL(8), // !=
    LESS(9), // <
    LESS_THAN(9), // <=
    GREATER(9), // >
    GREATER_THAN(9), // >=
    SHIFT_LEFT(10), // <<
    SHIFT_RIGHT(10), // >>
    ADD(11), // +
    SUBTRACKT(11), // -
    MULTIPLY(12), // *
    DIVIDE(12), // /
    MODULO(12), // %
    BOOLEAN_NOT(14), // !
    BINARY_NOT(14), // ~
    LEFT_PARENTHESIS(16), // (
    RIGHT_PARENTHESIS(16), // )
    LEFT_SQUARE_BRACKET(16), // [
    RIGHT_SQUARE_BRACKET(16), // ]
    LEFT_CURLY_BRACKET(16), // {
    RIGHT_CURLY_BRACKET(16), // }
    COMMA(20), // , , used for parameter lists
    DOT_DOT(20), // .. , used for intervalls
    IDENTIFIER(Integer.MAX_VALUE), NUMBER(Integer.MAX_VALUE), STRING(Integer.MAX_VALUE);
    private final int _priority;

    private TokenType(int priority) {
        _priority = priority;
    }

    public boolean hasLowerPrecedence(TokenType tokenType) {
        return _priority < tokenType._priority;
    }

    public boolean hasSamePrecedence(TokenType tokenType) {
        return _priority == tokenType._priority;
    }

    public boolean hasHigherPrecedence(TokenType tokenType) {
        return _priority > tokenType._priority;
    }

}
