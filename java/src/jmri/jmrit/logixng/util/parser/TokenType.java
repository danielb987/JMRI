package jmri.jmrit.logixng.util.parser;

/**
 * Types of tokens.
 * 
 * 
 * condition = "!" expression | expression { ("="|"#"|"<"|"<="|">"|">=") expression } .
 * 
 * expression = ["+"|"-"] term {("+"|"-") term} .
 * 
 * term = factor {("*"|"/") factor} .
 * 
 * factor = identifier | number | string | "(" condition ")" .
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * intervall = expression ".." expression
 * 
 * subset_expression_part = expression { ".." expression }
 * subset_expression = subset_expression_part { "," subset_expression_part }
 * subset = "[" subset_expression "]"
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * subset = "[" expression { ".." expression } { "," (expression|intervall) } "]"
 * 
 * condition = "!" expression | expression ("="|"#"|"<"|"<="|">"|">=") expression .
 * 
 * expression = ["+"|"-"] term {("+"|"-") term} .
 * 
 * term = factor {("*"|"/") factor} .
 * 
 * factor = identifier | number | string | "(" expression ")" .
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * factor = identifier | number | string | "(" expression ")" .
 * 
 * parameter_list = a ({"," parameter_list} | ".." 
 * 
    COMMA(-2),              // , , used for parameter lists
    DOT_DOT(-1),            // .. , used for intervalls
    BOOLEAN_OR(3),  // ||
    BOOLEAN_AND(4), // &&
    BINARY_OR(5),   // |
    BINARY_XOR(6),  // ^
    BINARY_AND(7),  // &
    EQUAL(8),       // ==
    NOT_EQUAL(8),   // !=
    LESS_THAN(9),       // <
    LESS_OR_EQUAL(9),   // <=
    GREATER_THAN(9),    // >
    GREATER_OR_EQUAL(9),// >=
    SHIFT_LEFT(10),     // <<
    SHIFT_RIGHT(10),    // >>
    ADD(11),            // +
    SUBTRACKT(11),      // -
    MULTIPLY(12),       // *
    DIVIDE(12),         // /
    MODULO(12),         // %
    BOOLEAN_NOT(14),    // !
    BINARY_NOT(14),     // ~
    LEFT_PARENTHESIS(16),       // (
    RIGHT_PARENTHESIS(16),      // )
    LEFT_SQUARE_BRACKET(16),    // [
    RIGHT_SQUARE_BRACKET(16),   // ]
    LEFT_CURLY_BRACKET(16),     // {
    RIGHT_CURLY_BRACKET(16),    // }
    IDENTIFIER(Integer.MAX_VALUE),
    NUMBER(Integer.MAX_VALUE),
    STRING(Integer.MAX_VALUE);
 * 
 * 
 * 
 * 
 * -------------------------------------------------------------
 * 
 * 
 * 
 * 
 * 
 * condition = "!" expression | expression ("="|"#"|"<"|"<="|">"|">=") expression .
 * 
 * expression = ["+"|"-"] term {("+"|"-") term} .
 * 
 * term = factor {("*"|"/") factor} .
 * 
 * factor = identifier | number | string | "(" expression ")" .
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * logical_or = 
 * 
 * 
 * 
 * 
 * factor = ident | number | string
 * 
 * 
 * 
 * 
 *     COMMA(-2),              // , , used for parameter lists
    DOT_DOT(-1),            // .. , used for intervalls
    BOOLEAN_OR(3),  // ||
    BOOLEAN_AND(4), // &&
    BINARY_OR(5),   // |
    BINARY_XOR(6),  // ^
    BINARY_AND(7),  // &
    EQUAL(8),       // ==
    NOT_EQUAL(8),   // !=
    LESS_THAN(9),       // <
    LESS_OR_EQUAL(9),   // <=
    GREATER_THAN(9),    // >
    GREATER_OR_EQUAL(9),// >=
    SHIFT_LEFT(10),     // <<
    SHIFT_RIGHT(10),    // >>
    ADD(11),            // +
    SUBTRACKT(11),      // -
    MULTIPLY(12),       // *
    DIVIDE(12),         // /
    MODULO(12),         // %
    BOOLEAN_NOT(14),    // !
    BINARY_NOT(14),     // ~
    LEFT_PARENTHESIS(16),       // (
    RIGHT_PARENTHESIS(16),      // )
    LEFT_SQUARE_BRACKET(16),    // [
    RIGHT_SQUARE_BRACKET(16),   // ]
    LEFT_CURLY_BRACKET(16),     // {
    RIGHT_CURLY_BRACKET(16),    // }
    IDENTIFIER(Integer.MAX_VALUE),
    NUMBER(Integer.MAX_VALUE),
    STRING(Integer.MAX_VALUE);

 * 
 * 
 */
public enum TokenType {
    // For precedence, see: https://introcs.cs.princeton.edu/java/11precedence/
    ERROR(Integer.MIN_VALUE),           // Invalid token, for example an identifier starting with a digit
    SAME_AS_LAST(Integer.MIN_VALUE),    // The same token as last time
    NONE(Integer.MIN_VALUE),
    SPACE(Integer.MIN_VALUE),           // Any space character outside of a string, like space, newline, ...
    COMMA(-2),              // , , used for parameter lists
    DOT_DOT(-1),            // .. , used for intervalls
    BOOLEAN_OR(3),  // ||
    BOOLEAN_AND(4), // &&
    BINARY_OR(5),   // |
    BINARY_XOR(6),  // ^
    BINARY_AND(7),  // &
    EQUAL(8),       // ==
    NOT_EQUAL(8),   // !=
    LESS_THAN(9),       // <
    LESS_OR_EQUAL(9),   // <=
    GREATER_THAN(9),    // >
    GREATER_OR_EQUAL(9),// >=
    SHIFT_LEFT(10),     // <<
    SHIFT_RIGHT(10),    // >>
    ADD(11),            // +
    SUBTRACKT(11),      // -
    MULTIPLY(12),       // *
    DIVIDE(12),         // /
    MODULO(12),         // %
    BOOLEAN_NOT(14),    // !
    BINARY_NOT(14),     // ~
    LEFT_PARENTHESIS(16),       // (
    RIGHT_PARENTHESIS(16),      // )
    LEFT_SQUARE_BRACKET(16),    // [
    RIGHT_SQUARE_BRACKET(16),   // ]
    LEFT_CURLY_BRACKET(16),     // {
    RIGHT_CURLY_BRACKET(16),    // }
    IDENTIFIER(Integer.MAX_VALUE),
    NUMBER(Integer.MAX_VALUE),
    STRING(Integer.MAX_VALUE);
    
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
    
    public boolean isLeaf() {
        return ((this == TokenType.IDENTIFIER)
                || (this == TokenType.NUMBER)
                || (this == TokenType.STRING));
    }
    
    /**
     * Checks if this token type can follow an other token type.
     * 
     * @param previousTokenType the previous token type or null if this is
     * the first token
     * @return true if this token type can follow the previous token type, false otherwise.
     */
    public boolean canFollow(TokenType previousTokenType) {
        
        if (previousTokenType == null) {
            return true;
        }
        
        // These tokens are only used by the Tokenizer and should never be
        // visible outside of the Tokenizer.
        if ((this == ERROR)
                || (this == SAME_AS_LAST)
                || (this == NONE)
                || (this == SPACE)) {
            return false;
        }
    
        if (this.isLeaf()) {
            return !previousTokenType.isLeaf();
        }
        
        if (this == TokenType.SUBTRACKT) {
            // The minus sign may follow any operator
            return true;
        }
        
        if ((this == TokenType.LEFT_PARENTHESIS)
                || (this == TokenType.LEFT_CURLY_BRACKET)) {
            // The left parenthesis and curly bracket may follow any token type
            // and may be the first token.
            return true;
        }
        
        if ((this == TokenType.LEFT_SQUARE_BRACKET)
                && ((previousTokenType == TokenType.RIGHT_PARENTHESIS)
                        || (previousTokenType == TokenType.RIGHT_CURLY_BRACKET)
                        || (previousTokenType.isLeaf()))) {
            // The left parenthesis or bracket may follow any operator
            return true;
        }
        
        if ((this == TokenType.RIGHT_PARENTHESIS)
                && ((previousTokenType == TokenType.LEFT_PARENTHESIS)
                        || (previousTokenType == TokenType.RIGHT_PARENTHESIS)
                        || (previousTokenType == TokenType.RIGHT_SQUARE_BRACKET)
                        || (previousTokenType == TokenType.RIGHT_CURLY_BRACKET)
                        || (previousTokenType.isLeaf()))) {
            // The left parenthesis or bracket may follow any operator
            return true;
        }
        
        if ((this == TokenType.RIGHT_SQUARE_BRACKET)
                && ((previousTokenType == TokenType.LEFT_SQUARE_BRACKET)
                        || (previousTokenType == TokenType.RIGHT_PARENTHESIS)
                        || (previousTokenType == TokenType.RIGHT_SQUARE_BRACKET)
                        || (previousTokenType == TokenType.RIGHT_CURLY_BRACKET)
                        || (previousTokenType.isLeaf()))) {
            // The left parenthesis or bracket may follow any operator
            return true;
        }
        
        if ((this == TokenType.RIGHT_CURLY_BRACKET)
                && ((previousTokenType == TokenType.LEFT_CURLY_BRACKET)
                        || (previousTokenType == TokenType.RIGHT_PARENTHESIS)
                        || (previousTokenType == TokenType.RIGHT_SQUARE_BRACKET)
                        || (previousTokenType == TokenType.RIGHT_CURLY_BRACKET)
                        || (previousTokenType.isLeaf()))) {
            // The left parenthesis or bracket may follow any operator
            return true;
        }
        
        return previousTokenType.isLeaf();
    }
    
}
