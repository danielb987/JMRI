package jmri.jmrit.logixng.util.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Parses and calculates an expression, for example "sin(2*pi*x)/3"
 * 
 * @author Daniel Bergqvist 2019
 */
public class Tokenizer {
    
    public static List<Token> getTokens(String expression) throws InvalidSyntaxException {
        
        List<Token> tokens = new ArrayList<>();
        Token currentToken = new Token();
        
        System.out.format("%n%n%n");
        System.out.format("getTokens(): %s%n", expression);
        
        AtomicBoolean eatNextChar = new AtomicBoolean(false);
        
        for (int i=0; i < expression.length(); i++) {
            char ch = expression.charAt(i);
            char nextChar = ' ';    // An extra space at the end of the _string doesn't matter
            if (i+1 < expression.length()) {
                nextChar = expression.charAt(i+1);
            }
            
            System.out.format("index %d: %s, %s, %c, %c%n", i, currentToken._tokenType.name(), currentToken._string, ch, nextChar);
            
            // Handle escaped characters
            if ((currentToken._tokenType == TokenType.STRING)
                    && (ch == '\\')
                    && ((nextChar == '\\') || (nextChar == '"'))) {
                
                currentToken._string += nextChar;
                i++;
                continue;
            }
            
            TokenType nextToken = getTokenType(currentToken, ch, nextChar, eatNextChar);
            System.out.format("index %d: %s, %c%n", i, nextToken.name(), ch);
            
            if (nextToken == TokenType.SAME_AS_LAST) {
                currentToken._string += ch;
                continue;
            }
            
            switch (nextToken) {
                case ERROR:
                    throw new InvalidSyntaxException("invalid syntax at index "+Integer.toString(i), i);
                    
                case LEFT_PARENTHESIS:
                case RIGHT_PARENTHESIS:
                case LEFT_SQUARE_BRACKET:
                case RIGHT_SQUARE_BRACKET:
                case LEFT_CURLY_BRACKET:
                case RIGHT_CURLY_BRACKET:
                case DOT_DOT:
                case COMMA:
                case EQUAL:
                case NOT_EQUAL:
                case LESS:
                case LESS_THAN:
                case GREATER:
                case GREATER_THAN:
                case ADD:
                case SUBTRACKT:
                case MULTIPLY:
                case DIVIDE:
                case MODULO:
                case BOOLEAN_AND:
                case BOOLEAN_OR:
                case BOOLEAN_NOT:
                case BINARY_AND:
                case BINARY_OR:
                case BINARY_NOT:
                case IDENTIFIER:
                    if ((currentToken._tokenType != TokenType.NONE) && (currentToken._tokenType != TokenType.SPACE)) {
                        System.out.format("Add: index %d: %s, %s, %c, %c%n", i, currentToken._tokenType.name(), currentToken._string, ch, nextChar);
                        tokens.add(currentToken);
                        currentToken = new Token();
                    }
                    currentToken._tokenType = nextToken;
                    break;
                    
                case NUMBER:
                    if ((currentToken._tokenType == TokenType.NUMBER) && !currentToken._string.isEmpty() && !isNumber(currentToken._string)) {
                        System.out.format("Not a number: '%s'%n", currentToken._string);
                        throw new InvalidSyntaxException("invalid syntax at index "+Integer.toString(i), i);
                    }
                    if ((currentToken._tokenType != TokenType.NONE) && (currentToken._tokenType != TokenType.SPACE)) {
                        tokens.add(currentToken);
                        currentToken = new Token();
                    }
                    currentToken._tokenType = nextToken;
                    break;
                    
                case STRING:
                    if (!currentToken._string.endsWith("\"")) {
                        throw new InvalidSyntaxException("invalid syntax at index "+Integer.toString(i), i);
                    }
                    if ((currentToken._tokenType != TokenType.NONE) && (currentToken._tokenType != TokenType.SPACE)) {
                        tokens.add(currentToken);
                        currentToken = new Token();
                    }
                    currentToken._tokenType = nextToken;
                    break;
                    
                case SPACE:
//                    if (currentToken._tokenType == TokenType.SPACE) {
//                        continue;
//                    }
                    // Fall through
                    
                case NONE:
                    if ((currentToken._tokenType == TokenType.STRING) && !currentToken._string.endsWith("\"")) {
                        throw new InvalidSyntaxException("invalid syntax at index "+Integer.toString(i), i);
                    }
                    if ((currentToken._tokenType != TokenType.NONE) && (currentToken._tokenType != TokenType.SPACE)) {
                        tokens.add(currentToken);
                        currentToken = new Token();
                    }
                    currentToken._tokenType = nextToken;
                    break;
                    
                default:
                    throw new RuntimeException("unknown token type: "+nextToken.name());
            }
            
            if (currentToken._tokenType != TokenType.SPACE) {
                currentToken._string += ch;
            }
            
            if (eatNextChar.get()) {
                i++;
            }
            System.out.format("New string: '%s'%n", currentToken._string);
        }
        
        if (currentToken._tokenType != TokenType.NONE) {
            tokens.add(currentToken);
        }
        
        return tokens;
    }
    
    private static TokenType getTokenType(Token currentToken, char ch, char nextChar, AtomicBoolean eatNextChar) {
        
        eatNextChar.set(false);
        
        if (ch == '"') {
            return TokenType.STRING;
        }
        
        if (Character.isSpaceChar(ch)) {
            return TokenType.SPACE;
        }
        
        if ((currentToken._tokenType == TokenType.STRING) && (ch != '"')) {
            return TokenType.SAME_AS_LAST;
        }
        
        if ((ch == '.') && (nextChar == '.')) {
            if ((currentToken._tokenType != TokenType.DOT_DOT)) {
                eatNextChar.set(true);
                return TokenType.DOT_DOT;
            } else {
                // Three dots in a row is an error
                return TokenType.ERROR;
            }
        }
        
        if (ch == '<') {
            if (nextChar == '=') {
                eatNextChar.set(true);
                return TokenType.LESS_THAN;
            } else if (nextChar == '<') {
                eatNextChar.set(true);
                return TokenType.SHIFT_LEFT;
            } else {
                return TokenType.LESS;
            }
        }
        
        if (ch == '>') {
            if (nextChar == '=') {
                eatNextChar.set(true);
                return TokenType.GREATER_THAN;
            } else if (nextChar == '>') {
                eatNextChar.set(true);
                return TokenType.SHIFT_RIGHT;
            } else {
                return TokenType.GREATER;
            }
        }
        
        if (ch == '=') {
            if (nextChar == '=') {
                eatNextChar.set(true);
                return TokenType.EQUAL;
            } else {
                return TokenType.ERROR;
            }
        }
        
        if (ch == '!') {
            if (nextChar == '=') {
                eatNextChar.set(true);
                return TokenType.NOT_EQUAL;
            } else {
                return TokenType.BOOLEAN_NOT;
            }
        }
        
        if (ch == '&') {
            if (nextChar == '&') {
                eatNextChar.set(true);
                return TokenType.BOOLEAN_AND;
            } else {
                return TokenType.BINARY_AND;
            }
        }
        
        if (ch == '~') {
            return TokenType.BINARY_NOT;
        }
        
        if (ch == ',') {
            return TokenType.COMMA;
        }
        
        if (ch == '+') {
            return TokenType.ADD;
        }
        
        if (ch == '-') {
            return TokenType.SUBTRACKT;
        }
        
        if (ch == '*') {
            return TokenType.MULTIPLY;
        }
        
        if (ch == '/') {
            return TokenType.DIVIDE;
        }
        
        if (ch == '%') {
            return TokenType.MODULO;
        }
        
        if (ch == '^') {
            return TokenType.BINARY_XOR;
        }
        
        if (ch == '(') {
            return TokenType.LEFT_PARENTHESIS;
        }
        
        if (ch == ')') {
            return TokenType.RIGHT_PARENTHESIS;
        }
        
        if (ch == '[') {
            return TokenType.LEFT_SQUARE_BRACKET;
        }
        
        if (ch == ']') {
            return TokenType.RIGHT_SQUARE_BRACKET;
        }
        
        if (ch == '{') {
            return TokenType.LEFT_CURLY_BRACKET;
        }
        
        if (ch == '}') {
            return TokenType.RIGHT_CURLY_BRACKET;
        }
        
        if ((currentToken._tokenType == TokenType.NUMBER) &&
                (isNumber(currentToken._string+ch) || isNumber(currentToken._string+ch+nextChar))) {
            return TokenType.SAME_AS_LAST;
        }
        
        if ((currentToken._tokenType == TokenType.IDENTIFIER) && (Character.isLetterOrDigit(ch))) {
            return TokenType.SAME_AS_LAST;
        }
        
        if (Character.isDigit(ch)) {
            return TokenType.NUMBER;
        }
        
        if ((currentToken._tokenType == TokenType.NUMBER) &&
                (Character.isLetterOrDigit(ch))) {
            return TokenType.ERROR;
        }
        
        if (Character.isDigit(ch)) {
            return TokenType.NUMBER;
        }
        
        if (Character.isLetter(ch)) {
            return TokenType.IDENTIFIER;
        }
        
        return TokenType.ERROR;
    }
    
    private static boolean isNumber(String str) {
        return str.matches("\\d+") || str.matches("\\d+\\.\\d+");
    }
    
}
