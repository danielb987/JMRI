package jmri.jmrit.logixng.util.parser;

import java.util.List;

/**
 * A recursive descent parser
 */
public class RecursiveDescentParser {

    private List<Token> tokens;
    private int tokenIndex;
    private Token token;
    private int lastTokenPos = 0;
    
    
    private void next() {
        tokenIndex++;
        token = tokens.get(tokenIndex);
    }
    
    
    private boolean accept(TokenType tokenType) throws InvalidSyntaxException {
        if (token == null) {
            return false;
        }
        if (token._tokenType == tokenType) {
            tokenIndex++;
            if (tokenIndex < tokens.size()) {
                token = tokens.get(tokenIndex);
            } else {
                lastTokenPos = token._pos + token._string.length();
                token = null;
            }
            return true;
        } else {
            return false;
        }
    }
    
    
    private void expect(TokenType tokenType) throws InvalidSyntaxException {
        boolean result = accept(tokenType);
        if (! result) {
            if (token != null) {
                throw new InvalidSyntaxException("invalid syntax at index "+Integer.toString(token._pos)+". Token "+tokenType.name()+" expected", token._pos);
            } else {
                throw new InvalidSyntaxException("invalid syntax at index "+Integer.toString(lastTokenPos)+". Token "+tokenType.name()+" expected", lastTokenPos);
            }
        }
    }
    
    
    private void factor() throws InvalidSyntaxException {
        if (accept(TokenType.IDENTIFIER)) {
            
        } else if (accept(TokenType.NUMBER)) {
            
        } else if (accept(TokenType.STRING)) {
            
        } else if (accept(TokenType.LEFT_PARENTHESIS)) {
            expression();
            expect(TokenType.RIGHT_PARENTHESIS);
        } else {
            throw new InvalidSyntaxException("invalid syntax at index "+Integer.toString(token._pos), token._pos);
        }
    }
    
    
    private void term() throws InvalidSyntaxException {
        factor();
        while ((token != null)
                && ((token._tokenType == TokenType.MULTIPLY)
                        || (token._tokenType == TokenType.DIVIDE))) {
            next();
            factor();
        }
    }
    
    
    private void expression() throws InvalidSyntaxException {
        if ((token._tokenType == TokenType.ADD) || (token._tokenType == TokenType.SUBTRACKT)) {
            next();
        }
        term();
        while ((token != null)
                && ((token._tokenType == TokenType.ADD)
                        || (token._tokenType == TokenType.SUBTRACKT))) {
            next();
            term();
        }
    }

    private void condition() throws InvalidSyntaxException {
        if (accept(TokenType.BOOLEAN_NOT)) {
            expression();
        } else {
            expression();
            if (token == null) {
                return;
            }
            if ((token._tokenType == TokenType.EQUAL)
                    || (token._tokenType == TokenType.NOT_EQUAL)
                    || (token._tokenType == TokenType.LESS_THAN)
                    || (token._tokenType == TokenType.LESS_OR_EQUAL)
                    || (token._tokenType == TokenType.GREATER_THAN)
                    || (token._tokenType == TokenType.GREATER_OR_EQUAL)) {
                
                next();
                expression();
            } else {
                throw new InvalidSyntaxException("invalid syntax at index "+Integer.toString(token._pos), token._pos);
            }
        }
    }
    
    
    
    public ParsedExpression parseExpression(String expression) throws InvalidSyntaxException {
        tokens = Tokenizer.getTokens(expression);
        
        if (tokens.isEmpty()) {
            return null;
        }
        
        tokenIndex = 0;
        token = tokens.get(tokenIndex);
        
        condition();
        
        return null;
    }
    
}
