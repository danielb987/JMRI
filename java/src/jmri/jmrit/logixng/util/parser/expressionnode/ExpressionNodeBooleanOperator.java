package jmri.jmrit.logixng.util.parser.expressionnode;

import jmri.jmrit.logixng.util.parser.TokenType;

/**
 * A parsed expression
 */
public class ExpressionNodeBooleanOperator implements ExpressionNode {

    private final TokenType _tokenType;
    private final ExpressionNode _leftSide;
    private final ExpressionNode _rightSide;
    
    public ExpressionNodeBooleanOperator(TokenType tokenType, ExpressionNode leftSide, ExpressionNode rightSide) {
        _tokenType = tokenType;
        _leftSide = leftSide;
        _rightSide = rightSide;
        
        if (_rightSide == null) {
            throw new IllegalArgumentException("rightSide must not be null");
        }
        
        // Verify that the token is of the correct type
        switch (_tokenType) {
            case BOOLEAN_OR:
            case BOOLEAN_AND:
            case BOOLEAN_NOT:
                break;
                
            default:
                throw new RuntimeException("Unknown arithmetic operator: "+_tokenType.name());
        }
    }
    
    @Override
    public Object calculate() {
        Object value = null;
        return value;
    }
    
    /** {@inheritDoc} */
    @Override
    public String getDefinitionString() {
        String operStr;
        switch (_tokenType) {
            case BOOLEAN_OR:
                operStr = "||";
                break;
                
            case BOOLEAN_AND:
                operStr = "&&";
                break;
                
            case BOOLEAN_NOT:
                operStr = "!";
                break;
                
            default:
                throw new RuntimeException("Unknown arithmetic operator: "+_tokenType.name());
        }
        if (_leftSide != null) {
            return "("+_leftSide.getDefinitionString()+")" + operStr + "("+_rightSide.getDefinitionString()+")";
        } else {
            return operStr + "("+_rightSide.getDefinitionString()+")";
        }
    }
    
}
