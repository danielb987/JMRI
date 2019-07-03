package jmri.jmrit.logixng.util.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A recursive descent parser
 */
public class RecursiveDescentParser {

    private List<Token> _tokens;
    private final Map<String, Variable> _variables;
    
    
    public RecursiveDescentParser(Map<String, Variable> variables) {
        _variables = variables;
    }
    
    private State next(State state) {
        int newTokenIndex = state._tokenIndex+1;
        return new State(newTokenIndex, _tokens.get(newTokenIndex), state._tokenIndex, state._token);
    }
    
    
    private State accept(TokenType tokenType, State state) throws InvalidSyntaxException {
        if (state._token == null) {
            return null;
        }
        if (state._token._tokenType == tokenType) {
            int newTokenIndex = state._tokenIndex+1;
            Token newToken;
            int lastTokenPos = state._lastTokenPos;
            if (newTokenIndex < _tokens.size()) {
                newToken = _tokens.get(newTokenIndex);
            } else {
                lastTokenPos = state._token._pos + state._token._string.length();
                newToken = null;
            }
            return new State(newTokenIndex, newToken, lastTokenPos, state._token);
        } else {
            return null;
        }
    }
    
    
    private State expect(TokenType tokenType, State state) throws InvalidSyntaxException {
        State newState = accept(tokenType, state);
        if (newState == null) {
            throw new InvalidSyntaxException("invalid syntax");
        }
        return newState;
    }
    
    
    public ExpressionNode parseExpression(String expression) throws InvalidSyntaxException {
        _tokens = Tokenizer.getTokens(expression);
        
        if (_tokens.isEmpty()) {
            return null;
        }
        
        ExpressionNodeAndState exprNodeAndState = firstRule.parse(new State(0, _tokens.get(0), 0, new Token()));
        
        if (exprNodeAndState == null) {
            return null;
        }
        
        if (exprNodeAndState._exprNode != null) {
            System.err.format("Expression: \"%s\"%n", exprNodeAndState._exprNode.getDefinitionString());
        } else {
            System.err.format("Expression: null%n");
        }
        
        if ((exprNodeAndState._state != null)
                && (exprNodeAndState._state._tokenIndex < _tokens.size())) {
            
            throw new InvalidSyntaxException("Invalid syntax. The expression is not fully parsed");
        }
        return exprNodeAndState._exprNode;
    }
    
    
    
    
    private static class State {
        
        private final int _tokenIndex;
        private final Token _token;
        private final int _lastTokenPos;
        private final Token _lastToken;
        
        public State(int tokenIndex, Token token, int lastTokenPos, Token lastToken) {
            _tokenIndex = tokenIndex;
            _token = token;
            _lastTokenPos = lastTokenPos;
            _lastToken = lastToken;
        }
    }
    
    
    private static class ExpressionNodeAndState {
        private final ExpressionNode _exprNode;
        private final State _state;
        
        private ExpressionNodeAndState(ExpressionNode exprNode, State state) {
            _exprNode = exprNode;
            _state = state;
        }
    }
    
    private interface Rule {
        
        public ExpressionNodeAndState parse(State state) throws InvalidSyntaxException;
        
    }
    
    
//    private final Rule rule1 = new Rule1();
    private final Rule rule3 = new Rule3();
    private final Rule rule4 = new Rule4();
    private final Rule rule5 = new Rule5();
    private final Rule rule6 = new Rule6();
    private final Rule rule7 = new Rule7();
    private final Rule rule8 = new Rule8();
    private final Rule rule9 = new Rule9();
    private final Rule rule10 = new Rule10();
    private final Rule rule11 = new Rule11();
    private final Rule rule12 = new Rule12();
    private final Rule rule14 = new Rule14();
    private final Rule rule16 = new Rule16();
    private final Rule rule20 = new Rule20();
    private final Rule21 rule21 = new Rule21();
    
    private final Rule firstRule = rule3;
    
/*    
    // "[" a ".." b "]" - intervalls
    private class Rule1 implements Rule {

        @Override
        public ExpressionNodeAndState parse(State state) throws InvalidSyntaxException {
            return rule3.parse(state);
        }
        
    }
*/    
    
    private class Rule3 implements Rule {

        @Override
        public ExpressionNodeAndState parse(State state) throws InvalidSyntaxException {
            return rule4.parse(state);
        }
        
    }
    
    
    private class Rule4 implements Rule {

        @Override
        public ExpressionNodeAndState parse(State state) throws InvalidSyntaxException {
            return rule5.parse(state);
        }
        
    }
    
    
    private class Rule5 implements Rule {

        @Override
        public ExpressionNodeAndState parse(State state) throws InvalidSyntaxException {
            return rule6.parse(state);
        }
        
    }
    
    
    private class Rule6 implements Rule {

        @Override
        public ExpressionNodeAndState parse(State state) throws InvalidSyntaxException {
            return rule7.parse(state);
        }
        
    }
    
    
    private class Rule7 implements Rule {

        @Override
        public ExpressionNodeAndState parse(State state) throws InvalidSyntaxException {
            return rule8.parse(state);
        }
        
    }
    
    
    private class Rule8 implements Rule {

        @Override
        public ExpressionNodeAndState parse(State state) throws InvalidSyntaxException {
            ExpressionNodeAndState leftSide = rule9.parse(state);
            if (leftSide == null) {
                return null;
            }
            State newState = leftSide._state;
            while ((newState._token != null)
                    && ((newState._token._tokenType == TokenType.EQUAL)
                            || (newState._token._tokenType == TokenType.NOT_EQUAL))) {

                TokenType operatorTokenType = newState._token._tokenType;
                newState = next(newState);
                ExpressionNodeAndState rightSide = rule9.parse(newState);

                ExpressionNode exprNode = new ExpressionNodeComparingOperator(operatorTokenType, leftSide._exprNode, rightSide._exprNode);
                leftSide = new ExpressionNodeAndState(exprNode, rightSide._state);
                newState = rightSide._state;
            }
            return leftSide;
        }
        
    }
    
    
    private class Rule9 implements Rule {

        @Override
        public ExpressionNodeAndState parse(State state) throws InvalidSyntaxException {
            ExpressionNodeAndState leftSide = rule10.parse(state);
            if (leftSide == null) {
                return null;
            }
            State newState = leftSide._state;
            while ((newState._token != null)
                    && ((newState._token._tokenType == TokenType.LESS_THAN)
                            || (newState._token._tokenType == TokenType.LESS_OR_EQUAL)
                            || (newState._token._tokenType == TokenType.GREATER_THAN)
                            || (newState._token._tokenType == TokenType.GREATER_OR_EQUAL))) {

                TokenType operatorTokenType = newState._token._tokenType;
                newState = next(newState);
                ExpressionNodeAndState rightSide = rule10.parse(newState);

                ExpressionNode exprNode = new ExpressionNodeComparingOperator(operatorTokenType, leftSide._exprNode, rightSide._exprNode);
                leftSide = new ExpressionNodeAndState(exprNode, rightSide._state);
                newState = rightSide._state;
            }
            return leftSide;
        }
        
    }
    
    
    private class Rule10 implements Rule {

        @Override
        public ExpressionNodeAndState parse(State state) throws InvalidSyntaxException {
            return rule11.parse(state);
        }
        
    }
    
    
    private class Rule11 implements Rule {

        @Override
        public ExpressionNodeAndState parse(State state) throws InvalidSyntaxException {
            ExpressionNodeAndState leftSide = rule12.parse(state);
            if (leftSide == null) {
                return null;
            }
            State newState = leftSide._state;
            while ((newState._token != null)
                    && ((newState._token._tokenType == TokenType.ADD)
                            || (newState._token._tokenType == TokenType.SUBTRACKT))) {

                TokenType operatorTokenType = newState._token._tokenType;
                newState = next(newState);
                ExpressionNodeAndState rightSide = rule12.parse(newState);

                ExpressionNode exprNode = new ExpressionNodeArithmeticOperator(operatorTokenType, leftSide._exprNode, rightSide._exprNode);
                leftSide = new ExpressionNodeAndState(exprNode, rightSide._state);
                newState = rightSide._state;
            }
            return leftSide;
        }
        
    }
    
    
    private class Rule12 implements Rule {

        @Override
        public ExpressionNodeAndState parse(State state) throws InvalidSyntaxException {
            ExpressionNodeAndState leftSide = rule14.parse(state);
            if (leftSide == null) {
                return null;
            }
            State newState = leftSide._state;
            while ((newState._token != null)
                    && ((newState._token._tokenType == TokenType.MULTIPLY)
                            || (newState._token._tokenType == TokenType.DIVIDE))) {

                TokenType operatorTokenType = newState._token._tokenType;
                newState = next(newState);
                ExpressionNodeAndState rightSide = rule14.parse(newState);

                ExpressionNode exprNode = new ExpressionNodeArithmeticOperator(operatorTokenType, leftSide._exprNode, rightSide._exprNode);
                leftSide = new ExpressionNodeAndState(exprNode, rightSide._state);
                newState = rightSide._state;
            }
            return leftSide;
        }
        
    }
    
    
    private class Rule14 implements Rule {

        @Override
        public ExpressionNodeAndState parse(State state) throws InvalidSyntaxException {
            return rule16.parse(state);
        }
        
    }
    
    
    private class Rule16 implements Rule {

        @Override
        public ExpressionNodeAndState parse(State state) throws InvalidSyntaxException {
            
            State newState = accept(TokenType.LEFT_PARENTHESIS, state);
            
            if (newState != null) {
                ExpressionNodeAndState exprNodeAndState = firstRule.parse(newState);
                if (exprNodeAndState._state._token == null) {
                    throw new InvalidSyntaxException("invalid syntax");
                }
                newState = expect(TokenType.RIGHT_PARENTHESIS, exprNodeAndState._state);
                return new ExpressionNodeAndState(exprNodeAndState._exprNode, newState);
            } else {
                return rule20.parse(state);
            }
        }
        
    }
    
    
    private class Rule20 implements Rule {

        @Override
        public ExpressionNodeAndState parse(State state) throws InvalidSyntaxException {
            ExpressionNode exprNode;

            State newState;
            if ((newState = accept(TokenType.IDENTIFIER, state)) != null) {
                ExpressionNodeIdentifier expressionNodeIdentifier = new ExpressionNodeIdentifier(newState._lastToken, _variables);
                State newState2;
                if ((newState2 = accept(TokenType.LEFT_PARENTHESIS, newState)) != null) {
                    ExpressionNodeAndState exprNodeAndState =
                            rule21.parse(newState2, expressionNodeIdentifier.getIdentifier());
                    if (exprNodeAndState._state._token == null) {
                        throw new InvalidSyntaxException("invalid syntax");
                    }
                    newState2 = expect(TokenType.RIGHT_PARENTHESIS, exprNodeAndState._state);
                    return new ExpressionNodeAndState(exprNodeAndState._exprNode, newState2);
                } else {
                    exprNode = expressionNodeIdentifier;
                }
            } else if ((newState = accept(TokenType.NUMBER, state)) != null) {
                exprNode = new ExpressionNodeNumber(newState._lastToken);
            } else if ((newState = accept(TokenType.STRING, state)) != null) {
                exprNode = new ExpressionNodeString(newState._lastToken);
            } else {
                return null;
    //            throw new InvalidSyntaxException("invalid syntax at index "+Integer.toString(_token._pos), _token._pos);
            }

            // Does this factor has an index or a subset? For example: "A string"[2..4,6]
    //        if (accept(TokenType.LEFT_SQUARE_BRACKET)) {
    //            subset_expression();
    //            expect(TokenType.RIGHT_SQUARE_BRACKET);
    //        }

            return new ExpressionNodeAndState(exprNode, newState);
        }
        
    }
    
    
    // a "," b "," c - parameter lists
    private class Rule21 {

        public ExpressionNodeAndState parse(State state, String identifier) throws InvalidSyntaxException {
            
            List<ExpressionNode> parameterList = new ArrayList<>();
            
            State newState = state;
            State newState2;
            if ((accept(TokenType.RIGHT_PARENTHESIS, newState)) == null) {
                ExpressionNodeAndState exprNodeAndState = rule3.parse(state);
                parameterList.add(exprNodeAndState._exprNode);
                
                while ((newState2 = accept(TokenType.COMMA, exprNodeAndState._state)) != null) {
                    exprNodeAndState = rule3.parse(newState2);
                    parameterList.add(exprNodeAndState._exprNode);
                }
                
                newState = exprNodeAndState._state;
            }
            ExpressionNode exprNode = new ExpressionNodeFunction(identifier, parameterList);
            return new ExpressionNodeAndState(exprNode, newState);
        }
        
    }
    
    
}
