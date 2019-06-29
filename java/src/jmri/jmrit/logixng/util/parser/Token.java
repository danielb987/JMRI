package jmri.jmrit.logixng.util.parser;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * A token used by the tokenizer and the parser
 */
public final class Token {

    @SuppressFBWarnings(value="CI_CONFUSED_INHERITANCE", justification="This field is updated by the Tokenizer class")
    protected TokenType _tokenType;
    
    @SuppressFBWarnings(value="CI_CONFUSED_INHERITANCE", justification="This field is updated by the Tokenizer class")
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
