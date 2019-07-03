package jmri.jmrit.logixng.util.parser;

import java.util.List;

/**
 * A parsed expression
 */
public class ExpressionNodeFunction implements ExpressionNode {

    private final String _identifier;
    private final List<ExpressionNode> _parameterList;
    
    ExpressionNodeFunction(String identifier, List<ExpressionNode> parameterList) {
        _identifier = identifier;
        _parameterList = parameterList;
    }
    
    @Override
    public Object calculate() {
        Object value = null;
        return value;
    }
    
    /** {@inheritDoc} */
    @Override
    public String getDefinitionString() {
        StringBuilder str = new StringBuilder();
        str.append("Function:");
        str.append(_identifier);
        str.append("(");
        for (int i=0; i < _parameterList.size(); i++) {
            if (i > 0) {
                str.append(",");
            }
            str.append(_parameterList.get(i).getDefinitionString());
        }
        str.append(")");
        return str.toString();
    }
    
}
