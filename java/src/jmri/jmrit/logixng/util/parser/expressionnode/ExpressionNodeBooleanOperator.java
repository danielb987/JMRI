package jmri.jmrit.logixng.util.parser.expressionnode;

import jmri.jmrit.logixng.util.parser.ParserException;
import jmri.jmrit.logixng.util.parser.TokenType;
import jmri.util.TypeConversionUtil;

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
                if (_leftSide == null) {
                    throw new IllegalArgumentException("leftSide must not be null for operators AND and OR");
                }
                break;
                
            case BOOLEAN_NOT:
                if (_leftSide != null) {
                    throw new IllegalArgumentException("leftSide must be null for operator NOT");
                }
                break;
                
            default:
                throw new RuntimeException("Unknown arithmetic operator: "+_tokenType.name());
        }
    }
    
    @Override
    public Object calculate() throws ParserException {
        
        Object rightValue = _rightSide.calculate();
        if (!(rightValue instanceof Boolean)) {
            rightValue = TypeConversionUtil.convertToBoolean(rightValue, false);
        }
        boolean right = (Boolean)rightValue;
        
        if (_tokenType == TokenType.BOOLEAN_NOT) {
            return ! right;
        }
        
        Object leftValue = _leftSide.calculate();
        if (!(leftValue instanceof Boolean)) {
            leftValue = TypeConversionUtil.convertToBoolean(leftValue, false);
        }
        boolean left = (Boolean)leftValue;
        
        switch (_tokenType) {
            case BOOLEAN_OR:
                return left || right;
                
            case BOOLEAN_AND:
                return left && right;
                
            default:
                throw new RuntimeException("Unknown arithmetic operator: "+_tokenType.name());
        }
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
