package jmri.jmrit.logixng.util.parser;

/**
 * A token used by the tokenizer and the parser
 */
public final class Token {

    TokenType _tokenType;
    String _string;
    int _pos;

    Token() {
        _tokenType = TokenType.NONE;
        _string = "";
        _pos = 0;
    }
    
    public TokenType getTokenType() {
        return _tokenType;
    }
    
    public String getString() {
        return _string;
    }
    
    public int getPos() {
        return _pos;
    }
    
}
