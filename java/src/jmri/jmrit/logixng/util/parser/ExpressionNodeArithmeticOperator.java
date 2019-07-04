package jmri.jmrit.logixng.util.parser;

import jmri.util.TypeConversionUtil;

/**
 * A parsed expression
 */
public class ExpressionNodeArithmeticOperator implements ExpressionNode {

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
        
        // Verify that the token is of the correct type
        switch (_tokenType) {
            case ADD:
            case SUBTRACKT:
            case MULTIPLY:
            case DIVIDE:
            case MODULO:
                break;
                
            default:
                throw new RuntimeException("Unknown arithmetic operator: "+_tokenType.name());
        }
    }
    
    
    private Object add(Object left, Object right) throws CalculateException {
        if (TypeConversionUtil.isIntegerNumber(left)
                && TypeConversionUtil.isIntegerNumber(right)) {
            return ((Number)left).longValue() + ((Number)right).longValue();

        } else if (TypeConversionUtil.isFloatingNumber(left)
                && TypeConversionUtil.isFloatingNumber(right)) {
            return ((Number)left).doubleValue() + ((Number)right).doubleValue();

        } else {
            if (!(left instanceof String)) {
                // convertToString() do more than just call left.toString()
                left = TypeConversionUtil.convertToString(left, false);
            }
            if (!(right instanceof String)) {
                // convertToString() do more than just call right.toString()
                right = TypeConversionUtil.convertToString(right, false);
            }

            return ((String)left) + ((String)right);
        }
    }
    
    
    private Object subtract(Object left, Object right) throws CalculateException {
        if (TypeConversionUtil.isIntegerNumber(left)
                && TypeConversionUtil.isIntegerNumber(right)) {
            return ((Number)left).longValue() - ((Number)right).longValue();

        } else if (TypeConversionUtil.isFloatingNumber(left)
                && TypeConversionUtil.isFloatingNumber(right)) {
            return ((Number)left).doubleValue() - ((Number)right).doubleValue();

        } else {
            throw new CalculateException(Bundle.getMessage("ArithmeticNotNumberError", left, right));
        }
    }
    
    
    private Object multiply(Object left, Object right) throws CalculateException {
        if (TypeConversionUtil.isIntegerNumber(left)
                && TypeConversionUtil.isIntegerNumber(right)) {
            return ((Number)left).longValue() * ((Number)right).longValue();

        } else if (TypeConversionUtil.isFloatingNumber(left)
                && TypeConversionUtil.isFloatingNumber(right)) {
            return ((Number)left).doubleValue() * ((Number)right).doubleValue();

        } else {
            throw new CalculateException(Bundle.getMessage("ArithmeticNotNumberError", left, right));
        }
    }
    
    
    private Object divide(Object left, Object right) throws CalculateException {
        if (TypeConversionUtil.isIntegerNumber(left)
                && TypeConversionUtil.isIntegerNumber(right)) {
            return ((Number)left).longValue() / ((Number)right).longValue();

        } else if (TypeConversionUtil.isFloatingNumber(left)
                && TypeConversionUtil.isFloatingNumber(right)) {
            return ((Number)left).doubleValue() / ((Number)right).doubleValue();

        } else {
            throw new CalculateException(Bundle.getMessage("ArithmeticNotNumberError", left, right));
        }
    }
    
    
    private Object modulo(Object left, Object right) throws CalculateException {
        if (TypeConversionUtil.isIntegerNumber(left)
                && TypeConversionUtil.isIntegerNumber(right)) {
            
            // Both operands are integers
            return ((Number)left).longValue() % ((Number)right).longValue();

        } else if (!TypeConversionUtil.isFloatingNumber(left)
                || !TypeConversionUtil.isFloatingNumber(right)) {
            
            // At least one operand is neither integer or floating point number
            throw new CalculateException(Bundle.getMessage("ArithmeticNotNumberError", left, right));

        } else {
            
            // At least one operand is a floating point number
            throw new CalculateException(Bundle.getMessage("ArithmeticModuloFloatingError", left, right));
        }
    }
    
    
    @Override
    public Object calculate() throws CalculateException {
        
        Object left = _leftSide.calculate();
        Object right = _rightSide.calculate();
        
        // Convert a boolean value to an integer value
        if (left instanceof Boolean) {
            left = ((Boolean)left) ? 1 : 0;
        }
        if (right instanceof Boolean) {
            right = ((Boolean)right) ? 1 : 0;
        }
        
        if (_tokenType == TokenType.ADD) {
            // Add can handle String concatenation
            return add(left, right);
        } else {
            // For the other arithmetic operators, except add, only numbers can
            // be handled. For other types, return 0.
            if (! TypeConversionUtil.isFloatingNumber(left)) {
                return 0;
            }
            if (! TypeConversionUtil.isFloatingNumber(right)) {
                return 0;
            }
            
            switch (_tokenType) {
                case SUBTRACKT:
                    return subtract(left, right);
                case MULTIPLY:
                    return multiply(left, right);
                case DIVIDE:
                    return divide(left, right);
                case MODULO:
                    return modulo(left, right);

                default:
                    throw new RuntimeException("Unknown arithmetic operator: "+_tokenType.name());
            }
        }
        
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
    }
    
}
