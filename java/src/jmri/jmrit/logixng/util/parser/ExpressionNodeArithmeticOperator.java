package jmri.jmrit.logixng.util.parser;

/**
 * A parsed expression
 */
public class ExpressionNodeArithmeticOperator implements ExpressionNode {

    public enum ArithmeticOperator {
        ADD,
        SUBTRACKT,
        MULTIPLY,
        DIVIDE,
        MODULO,
    }
    
    private final ArithmeticOperator _oper;
    private final TokenType _tokenType;
    private final ExpressionNode _leftSide;
    private final ExpressionNode _rightSide;
    
    ExpressionNodeArithmeticOperator(TokenType tokenType, ExpressionNode leftSide, ExpressionNode rightSide) {
        _tokenType = tokenType;
        _leftSide = leftSide;
        _rightSide = rightSide;
        
        if (_rightSide == null) {
            throw new IllegalArgumentException("rightSide must not be null");
        }
        
        switch (_tokenType) {
            case ADD:
                _oper = ArithmeticOperator.ADD;
                break;
                
            case SUBTRACKT:
                _oper = ArithmeticOperator.SUBTRACKT;
                break;
                
            case MULTIPLY:
                _oper = ArithmeticOperator.MULTIPLY;
                break;
                
            case DIVIDE:
                _oper = ArithmeticOperator.DIVIDE;
                break;
                
            case MODULO:
                _oper = ArithmeticOperator.MODULO;
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
            case ADD:
                operStr = "+";
                break;
                
            case SUBTRACKT:
                operStr = "-";
                break;
                
            case MULTIPLY:
                operStr = "*";
                break;
                
            case DIVIDE:
                operStr = "/";
                break;
                
            case MODULO:
                operStr = "%";
                break;
                
            default:
                throw new RuntimeException("Unknown arithmetic operator: "+_tokenType.name());
        }
        
        String leftSideString = _leftSide != null ? _leftSide.getDefinitionString() : "null";
        String rightSideString = _rightSide != null ? _rightSide.getDefinitionString() : "null";
        return "("+leftSideString+")" + operStr + "("+rightSideString+")";
//        return "("+leftSideString+")" + operStr + "("+_rightSide.getDefinitionString()+") ";
//        return "("+_leftSide.getDefinitionString()+")" + operStr + "("+_rightSide.getDefinitionString()+") ";
    }
    
}
