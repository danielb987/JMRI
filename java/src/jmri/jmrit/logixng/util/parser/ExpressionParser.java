package jmri.jmrit.logixng.util.parser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Parses and calculates an expression, for example "sin(2*pi*x)/3"
 * 
 * @author Daniel Bergqvist 2019
 */
public class ExpressionParser<E> {
    
    private final Map<String, OperatorInfo<E>> unaryOperators = new HashMap<>();
    private final Map<String, OperatorInfo<E>> binaryOperators = new HashMap<>();
    private final Map<String, Function<E>> functions = new HashMap<>();
    
    
    public void addUnaryOperator(String operator, OperatorInfo<E> operatorInfo) {
        unaryOperators.put(operator, operatorInfo);
    }
    
    public void addBinaryOperator(String operator, OperatorInfo<E> operatorInfo) {
        binaryOperators.put(operator, operatorInfo);
    }
    
    public void addFunction(String operator, Function<E> operatorInfo) {
        functions.put(operator, operatorInfo);
    }
    
    public ParsedExpression<E> parseExpression(String expression) throws InvalidSyntaxException {
        List<Token> tokens = Tokenizer.getTokens(expression);
        
//        ParsedExpression<E> expr = new ParsedExpression<>();
        
//        TokenType lastTokenType = TokenType.NONE;
//        ExpressionNode<E> exprNode;
        
        for (int i = tokens.size(); i > 0; i--) {
//            System.out.println(tokens.get(i));
            Token token = tokens.get(i);
            switch (token.getTokenType()) {
                //$FALL-THROUGH$
                case LEFT_PARENTHESIS:
                case RIGHT_PARENTHESIS:
                case IDENTIFIER:
                case NUMBER:
                case STRING:
                    
                case ERROR:          // Invalid token, for example an identifier starting with a digit
                case SAME_AS_LAST:   // The same token as last time
                case NONE:
                case SPACE:
                    throw new RuntimeException("this token type should not be here: "+token.getTokenType());
                    
                default:
                    throw new RuntimeException("unknown token type: "+token.getTokenType());
            }
        }
        
        return null;
    }
    
    
    public interface Function<E> {
        
        public E call(E param1, E param2);
//        public E call(List<E> params);
    }
    
    
    public interface OperatorInfo<E> {
        
        public Function<E> getFunction();
        
        public int getPriority();
        
    }
    
}
