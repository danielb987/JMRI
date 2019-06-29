package jmri.jmrit.logixng.util.parser;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 *
 */
public final class Token {

    @SuppressFBWarnings(value="CI_CONFUSED_INHERITANCE", justification="These fields are updated by the Tokenizer class")
    
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
