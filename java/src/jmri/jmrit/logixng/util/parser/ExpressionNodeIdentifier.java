package jmri.jmrit.logixng.util.parser;

import java.util.Map;

/**
 * A parsed expression
 */
public class ExpressionNodeIdentifier implements ExpressionNode {

    private final Token _token;
    private final Variable _variable;
    
    ExpressionNodeIdentifier(Token token, Map<String, Variable> variables) {
        _token = token;
        _variable = variables.get(token._string);
    }
    
    public String getIdentifier() {
        return _token._string;
    }
    
    @Override
    public Object calculate() {
        return _variable.getValue();
    }
    
    /** {@inheritDoc} */
    @Override
    public String getDefinitionString() {
        return "Identifier:"+_token._string;
    }
    
}
