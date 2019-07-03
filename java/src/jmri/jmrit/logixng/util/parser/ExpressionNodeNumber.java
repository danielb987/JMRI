package jmri.jmrit.logixng.util.parser;

/**
 * A parsed expression
 */
public class ExpressionNodeNumber implements ExpressionNode {

    private final Token _token;
    private final double _value;
    
    ExpressionNodeNumber(Token token) {
        _token = token;
        _value = Double.parseDouble(token._string);
    }
    
    @Override
    public Object calculate() {
        return _value;
    }
    
    /** {@inheritDoc} */
    @Override
    public String getDefinitionString() {
        return "Number:"+_token._string;
    }
    
}
