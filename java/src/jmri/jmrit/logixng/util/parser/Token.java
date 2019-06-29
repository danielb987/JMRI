package jmri.jmrit.logixng.util.parser;

/**
 *
 */
public final class Token {

    protected TokenType _tokenType;
    protected String _string;

    protected Token() {
        _tokenType = TokenType.NONE;
        _string = "";
    }
    
    public TokenType getTokenType() {
        return _tokenType;
    }
    
    public String getString() {
        return _string;
    }
    
}
