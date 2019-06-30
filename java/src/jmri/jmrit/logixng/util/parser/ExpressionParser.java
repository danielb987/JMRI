package jmri.jmrit.logixng.util.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

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
    
    public ExpressionNode getExpressionNode(Token token, ExpressionNode lastExpressionNode) {
        return null;
    }
    
    public ParsedExpression parseExpression(String expression) throws InvalidSyntaxException {
        List<Token> tokens = Tokenizer.getTokens(expression);
        
//        List<ExpressionNode> expressionNodeList = new ArrayList<>();
//        ParsedExpression<E> expr = new ParsedExpression<>();
        
//        TokenType lastTokenType = TokenType.NONE;
//        ExpressionNode<E> exprNode;
        
//        List<ExpressionNode> currentExpressionNodeList = new ArrayList<>();
        
//        Stack<ExpressionNode> stack = new Stack<>();
        
        ExpressionNode lastExpressionNode = null;
        
        Token lastToken = null;
        
//        for (int i = tokens.size(); i > 0; i--) {
        for (Token token : tokens) {
//            System.out.println(tokens.get(i));
//            Token token = tokens.get(i);
            
            if (token == null) {
                // token should never be null
                throw new RuntimeException("token is null");
            }
            
            TokenType lastTokenType = token.getTokenType();
            if ((lastToken != null) && (! token.getTokenType().canFollow(lastTokenType))) {
                throw new InvalidSyntaxException("invalid syntax", token.getPos());
            }
            
            ExpressionNode exprNode = null;
            
            if ((lastToken == null)
                    || (token.getTokenType().hasSamePrecedence(lastToken.getTokenType())
                    || (token.getTokenType().hasHigherPrecedence(lastToken.getTokenType())))) {
                
                TokenType tokenType = token.getTokenType();
                if (tokenType == TokenType.IDENTIFIER) {
                    exprNode = new ExpressionNodeIdentifier(token);
                } else if (tokenType == TokenType.NUMBER) {
                    exprNode = new ExpressionNodeNumber(token);
                } else if (tokenType == TokenType.STRING) {
                    exprNode = new ExpressionNodeString(token);
                } else if (lastExpressionNode != null) {
                    exprNode = getExpressionNode(token, lastExpressionNode);
                } else {
                    throw new InvalidSyntaxException("invalid syntax", token.getPos());
                }
                lastToken = token;
                lastExpressionNode = exprNode;
            } else if (token.getTokenType().hasLowerPrecedence(lastToken.getTokenType())) {
                lastToken = token;
            } else {
                // We should not be here...
                throw new RuntimeException("error in parser");
            }
            
/*            
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
*/
        }
        
        return null;
    }
    
    
//    public interface Parameter<E> {
        
//    }
    
    
    public interface Function<E> {
        
        public E call(E param1, E param2);
//        public E call(List<E> params);
//        public E call(List<ExpressionNode<E>> params);
    }
    
    
    public interface OperatorInfo<E> {
        
        public Function<E> getFunction();
        
        public int getPriority();
        
    }
    
}
