package jmri.jmrit.logixng.util.parser;

/**
 * A parsed expression
 */
public class ExpressionNodeString implements ExpressionNode {

    private final Token _token;
    
    ExpressionNodeString(Token token) {
        _token = token;
    }
    
    @Override
    public Object calculate() {
        Object value = null;
        return value;
    }
    
    /** {@inheritDoc} */
    @Override
    public String getDefinitionString() {
        return "String:"+_token._string;
    }
    
}
