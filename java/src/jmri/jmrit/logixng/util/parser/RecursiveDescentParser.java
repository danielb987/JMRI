package jmri.jmrit.logixng.util.parser;

import java.util.List;

/**
 * A recursive descent parser
 */
public class RecursiveDescentParser {

    private List<Token> _tokens;
    private int _tokenIndex;
    private Token _token;
    private int _lastTokenPos = 0;
    
    
    private void next() {
        _tokenIndex++;
        _token = _tokens.get(_tokenIndex);
    }
    
    
    private boolean accept(TokenType tokenType) throws InvalidSyntaxException {
        if (_token == null) {
            return false;
        }
        if (_token._tokenType == tokenType) {
            _tokenIndex++;
            if (_tokenIndex < _tokens.size()) {
                _token = _tokens.get(_tokenIndex);
            } else {
                _lastTokenPos = _token._pos + _token._string.length();
                _token = null;
            }
            return true;
        } else {
            return false;
        }
    }
    
    
    private void expect(TokenType tokenType) throws InvalidSyntaxException {
        boolean result = accept(tokenType);
        if (! result) {
            if (_token != null) {
                throw new InvalidSyntaxException("invalid syntax at index "+Integer.toString(_token._pos)+". Token "+tokenType.name()+" expected", _token._pos);
            } else {
                throw new InvalidSyntaxException("invalid syntax at index "+Integer.toString(_lastTokenPos)+". Token "+tokenType.name()+" expected", _lastTokenPos);
            }
        }
    }
    
    
    private ExpressionNode factor() throws InvalidSyntaxException {
        if (accept(TokenType.IDENTIFIER)) {
            
        } else if (accept(TokenType.NUMBER)) {
            
        } else if (accept(TokenType.STRING)) {
            
        } else if (accept(TokenType.LEFT_PARENTHESIS)) {
//            expression();
            condition();
            expect(TokenType.RIGHT_PARENTHESIS);
        } else {
            throw new InvalidSyntaxException("invalid syntax at index "+Integer.toString(_token._pos), _token._pos);
        }
        
        // Does this factor has an index or a subset? For example: "A string"[2..4,6]
//        if (accept(TokenType.LEFT_SQUARE_BRACKET)) {
//            subset_expression();
//            expect(TokenType.RIGHT_SQUARE_BRACKET);
//        }
    }
    
    
    private void term() throws InvalidSyntaxException {
        factor();
        while ((_token != null)
                && ((_token._tokenType == TokenType.MULTIPLY)
                        || (_token._tokenType == TokenType.DIVIDE))) {
            next();
            factor();
        }
    }
    
    
    private void expression() throws InvalidSyntaxException {
        if ((_token._tokenType == TokenType.ADD) || (_token._tokenType == TokenType.SUBTRACKT)) {
            next();
        }
        term();
        while ((_token != null)
                && ((_token._tokenType == TokenType.ADD)
                        || (_token._tokenType == TokenType.SUBTRACKT))) {
            next();
            term();
        }
    }

    private void condition() throws InvalidSyntaxException {
        if (accept(TokenType.BOOLEAN_NOT)) {
            expression();
        } else {
            expression();
            if (_token == null) {
                return;
            }
            if ((_token._tokenType == TokenType.EQUAL)
                    || (_token._tokenType == TokenType.NOT_EQUAL)
                    || (_token._tokenType == TokenType.LESS_THAN)
                    || (_token._tokenType == TokenType.LESS_OR_EQUAL)
                    || (_token._tokenType == TokenType.GREATER_THAN)
                    || (_token._tokenType == TokenType.GREATER_OR_EQUAL)) {
                
                next();
                expression();
//            } else {
//                throw new InvalidSyntaxException("invalid syntax at index "+Integer.toString(token._pos), token._pos);
            }
        }
    }
    
    
    
    public ParsedExpression parseExpression(String expression) throws InvalidSyntaxException {
        _tokens = Tokenizer.getTokens(expression);
        
        if (_tokens.isEmpty()) {
            return null;
        }
        
        _tokenIndex = 0;
        _token = _tokens.get(_tokenIndex);
        
        condition();
        
        return null;
    }
    
}
