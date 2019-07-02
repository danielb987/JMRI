package jmri.jmrit.logixng.util.parser;

/**
 * A parsed expression
 */
public class ExpressionNodeComparingOperator implements ExpressionNode {

    public enum ArithmeticOperator {
        EQUAL,
        NOT_EQUAL,
        LESS_THAN,
        LESS_OR_EQUAL,
        GREATER_THAN,
        GREATER_OR_EQUAL,
    }
    
    private final ArithmeticOperator _oper;
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
        
        switch (_tokenType) {
            case EQUAL:
                _oper = ArithmeticOperator.EQUAL;
                break;
                
            case NOT_EQUAL:
                _oper = ArithmeticOperator.NOT_EQUAL;
                break;
                
            case LESS_THAN:
                _oper = ArithmeticOperator.LESS_THAN;
                break;
                
            case LESS_OR_EQUAL:
                _oper = ArithmeticOperator.LESS_OR_EQUAL;
                break;
                
            case GREATER_THAN:
                _oper = ArithmeticOperator.GREATER_THAN;
                break;
                
            case GREATER_OR_EQUAL:
                _oper = ArithmeticOperator.GREATER_OR_EQUAL;
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
