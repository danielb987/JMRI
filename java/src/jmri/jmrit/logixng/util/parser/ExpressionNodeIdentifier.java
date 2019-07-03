package jmri.jmrit.logixng.util.parser;

/**
 * A parsed expression
 */
public class ExpressionNodeIdentifier implements ExpressionNode {

    private final Token _token;
    
    ExpressionNodeIdentifier(Token token) {
        _token = token;
    }
    
    public String getIdentifier() {
        return _token._string;
    }
    
    @Override
    public Object calculate() {
        Object value = null;
        return value;
    }
    
    /** {@inheritDoc} */
    @Override
    public String getDefinitionString() {
        return "Identifier:"+_token._string;
    }
    
}
