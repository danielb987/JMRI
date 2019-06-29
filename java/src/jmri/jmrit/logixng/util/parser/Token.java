package jmri.jmrit.logixng.util.parser;

/**
 * A token used by the tokenizer and the parser
 */
public final class Token {

    TokenType _tokenType;
    String _string;

    Token() {
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
