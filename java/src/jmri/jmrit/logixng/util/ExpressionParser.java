package jmri.jmrit.logixng.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Parses and calculates an expression, for example "sin(2*pi*x)/3"
 */
public class ExpressionParser<E> {
    
    private final Map<String, OperatorInfo<E>> unaryOperators = new HashMap<>();
    private final Map<String, OperatorInfo<E>> binaryOperators = new HashMap<>();
    private final Map<String, OperatorInfo<E>> functions = new HashMap<>();
    
    
    public void addUnaryOperator(String operator, OperatorInfo<E> operatorInfo) {
        unaryOperators.put(operator, operatorInfo);
    }
    
    public void addBinaryOperator(String operator, OperatorInfo<E> operatorInfo) {
        unaryOperators.put(operator, operatorInfo);
    }
    
    public void addFunction(String operator, OperatorInfo<E> operatorInfo) {
        unaryOperators.put(operator, operatorInfo);
    }
    
    public ParsedExpression<E> parseExpression(String expression) throws InvalidSyntaxException {
        List<Token> tokens = getTokens(expression);
        return null;
    }
    
    protected List<Token> getTokens(String expression) throws InvalidSyntaxException {
        
        List<Token> tokens = new ArrayList<>();
        Token currentToken = new Token();
        
        System.out.format("%n%n%n");
        System.out.format("getTokens(): %s%n", expression);
        
        for (int i=0; i < expression.length(); i++) {
            char ch = expression.charAt(i);
            char nextChar = ' ';    // An extra space at the end of the string doesn't matter
            if (i+1 < expression.length()) {
                nextChar = expression.charAt(i+1);
            }
            
            System.out.format("index %d: %s, %s, %c, %c%n", i, currentToken.tokenType.name(), currentToken.string, ch, nextChar);
            
            // Handle escaped characters
            if ((currentToken.tokenType == TokenType.STRING)
                    && (ch == '\\')
                    && ((nextChar == '\\') || (nextChar == '"'))) {
                
                currentToken.string += nextChar;
                i++;
                continue;
            }
            
            TokenType nextToken = getTokenType(currentToken, ch, nextChar);
            System.out.format("index %d: %s, %c%n", i, nextToken.name(), ch);
            
            if (nextToken == TokenType.SAME_AS_LAST) {
                currentToken.string += ch;
                continue;
            }
            
            switch (nextToken) {
                case ERROR:
                    throw new InvalidSyntaxException("invalid syntax at index "+Integer.toString(i), i);
                    
                case LEFT_PARENTHESIS:
                case RIGHT_PARENTHESIS:
                case NON_ALPHANUMERIC:
                case IDENTIFIER:
                    if ((currentToken.tokenType != TokenType.NONE) && (currentToken.tokenType != TokenType.SPACE)) {
                        System.out.format("Add: index %d: %s, %s, %c, %c%n", i, currentToken.tokenType.name(), currentToken.string, ch, nextChar);
                        tokens.add(currentToken);
                        currentToken = new Token();
                    }
                    currentToken.tokenType = nextToken;
                    break;
                    
                case NUMBER:
                    if ((currentToken.tokenType == TokenType.NUMBER) && !currentToken.string.isEmpty() && !isNumber(currentToken.string)) {
                        System.out.format("Not a number: '%s'%n", currentToken.string);
                        throw new InvalidSyntaxException("invalid syntax at index "+Integer.toString(i), i);
                    }
                    if ((currentToken.tokenType != TokenType.NONE) && (currentToken.tokenType != TokenType.SPACE)) {
                        tokens.add(currentToken);
                        currentToken = new Token();
                    }
                    currentToken.tokenType = nextToken;
                    break;
                    
                case STRING:
                    if (!currentToken.string.endsWith("\"")) {
                        throw new InvalidSyntaxException("invalid syntax at index "+Integer.toString(i), i);
                    }
                    if ((currentToken.tokenType != TokenType.NONE) && (currentToken.tokenType != TokenType.SPACE)) {
                        tokens.add(currentToken);
                        currentToken = new Token();
                    }
                    currentToken.tokenType = nextToken;
                    break;
                    
                case SPACE:
//                    if (currentToken.tokenType == TokenType.SPACE) {
//                        continue;
//                    }
                    // Fall through
                    
                case NONE:
                    if ((currentToken.tokenType == TokenType.STRING) && !currentToken.string.endsWith("\"")) {
                        throw new InvalidSyntaxException("invalid syntax at index "+Integer.toString(i), i);
                    }
                    if ((currentToken.tokenType != TokenType.NONE) && (currentToken.tokenType != TokenType.SPACE)) {
                        tokens.add(currentToken);
                        currentToken = new Token();
                    }
                    currentToken.tokenType = nextToken;
                    break;
                    
                default:
                    throw new RuntimeException("unknown token type: "+nextToken.name());
            }
            
            if (currentToken.tokenType != TokenType.SPACE) {
                currentToken.string += ch;
            }
            System.out.format("New string: '%s'%n", currentToken.string);
        }
        
        if (currentToken.tokenType != TokenType.NONE) {
            tokens.add(currentToken);
        }
        
        return tokens;
    }
    
    private TokenType getTokenType(Token currentToken, char ch, char nextChar) {
        if (ch == '"') {
            return TokenType.STRING;
        }
        
        if (Character.isSpaceChar(ch)) {
            return TokenType.SPACE;
        }
        
        if ((currentToken.tokenType == TokenType.STRING) && (ch != '"')) {
            return TokenType.SAME_AS_LAST;
        }
        
        if (ch == '(') {
            return TokenType.LEFT_PARENTHESIS;
        }
        
        if ((currentToken.tokenType != TokenType.LEFT_PARENTHESIS) && (ch == ')')) {
            return TokenType.RIGHT_PARENTHESIS;
        }
        
        if (!Character.isLetterOrDigit(ch) && (ch != '.')) {
            if (currentToken.tokenType == TokenType.NON_ALPHANUMERIC) {
                return TokenType.SAME_AS_LAST;
            } else {
                return TokenType.NON_ALPHANUMERIC;
            }
        }
        
        if ((currentToken.tokenType == TokenType.NUMBER) &&
                (isNumber(currentToken.string+ch) || isNumber(currentToken.string+ch+nextChar))) {
            return TokenType.SAME_AS_LAST;
        }
        
        if ((currentToken.tokenType == TokenType.IDENTIFIER) && (Character.isLetterOrDigit(ch))) {
            return TokenType.SAME_AS_LAST;
        }
        
        if (Character.isDigit(ch)) {
            return TokenType.NUMBER;
        }
        
        if ((currentToken.tokenType == TokenType.NUMBER) &&
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
    
    private boolean isNumber(String str) {
        return str.matches("\\d+") || str.matches("\\d+\\.\\d+");
    }
    
    public interface Function<E> {
        
        public E call(E param1, E param2);
    }
    
    
    public interface OperatorInfo<E> {
        
        public Function<E> getFunction();
        
        public int getPriority();
        
    }
    
    
    protected enum TokenType {
        ERROR,          // Invalid token, for example an identifier starting with a digit
        SAME_AS_LAST,   // The same token as last time
        NONE,
        SPACE,
        LEFT_PARENTHESIS,
        RIGHT_PARENTHESIS,
        NON_ALPHANUMERIC,   // Might be an operator, for example => or &&
        IDENTIFIER,
        NUMBER,
        STRING,
    }
    
    protected final class Token {
        protected TokenType tokenType;
        protected String string;
        
        protected Token() {
            tokenType = TokenType.NONE;
            string = "";
        }
    }
    
}
