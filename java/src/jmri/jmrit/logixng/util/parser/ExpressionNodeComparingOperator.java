package jmri.jmrit.logixng.util.parser;

/**
 * A parsed expression
 */
public class ExpressionNodeComparingOperator implements ExpressionNode {

    private final TokenType _tokenType;
    private final ExpressionNode _leftSide;
    private final ExpressionNode _rightSide;
    
    ExpressionNodeComparingOperator(TokenType tokenType, ExpressionNode leftSide, ExpressionNode rightSide) {
        _tokenType = tokenType;
        _leftSide = leftSide;
        _rightSide = rightSide;
        
        if (_rightSide == null) {
            throw new IllegalArgumentException("rightSide must not be null");
        }
        
        // Verify that the token is of the correct type
        switch (_tokenType) {
            case EQUAL:
            case NOT_EQUAL:
            case LESS_THAN:
            case LESS_OR_EQUAL:
            case GREATER_THAN:
            case GREATER_OR_EQUAL:
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
            case EQUAL:
                operStr = "==";
                break;
                
            case NOT_EQUAL:
                operStr = "!=";
                break;
                
            case LESS_THAN:
                operStr = "<";
                break;
                
            case LESS_OR_EQUAL:
                operStr = "<=";
                break;
                
            case GREATER_THAN:
                operStr = ">";
                break;
                
            case GREATER_OR_EQUAL:
                operStr = ">=";
                break;
                
            default:
                throw new RuntimeException("Unknown arithmetic operator: "+_tokenType.name());
        }
        return "("+_leftSide.getDefinitionString()+")" + operStr + "("+_rightSide.getDefinitionString()+")";
    }
    
}
