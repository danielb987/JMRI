package jmri.jmrit.logixng.util.parser;

/**
 * A parsed expression
 */
public class ExpressionNodeIdentifier implements ExpressionNode {

//    private final Token _token;
    
    ExpressionNodeIdentifier(Token token) {
//        _token = token;
    }
    
    @Override
    public Object calculate() {
        Object value = null;
        return value;
    }
    
}
