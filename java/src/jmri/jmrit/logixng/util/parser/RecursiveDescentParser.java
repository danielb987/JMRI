package jmri.jmrit.logixng.util.parser;

import java.util.List;
import org.slf4j.LoggerFactory;

/**
 * A recursive descent parser
 */
public class RecursiveDescentParser {

    private List<Token> _tokens;
    
    
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
//            if (state._token != null) {
//                throw new InvalidSyntaxException("invalid syntax at index "+Integer.toString(state._token._pos)+". Token "+tokenType.name()+" expected", state._token._pos);
//            } else {
//                throw new InvalidSyntaxException("invalid syntax"+Integer.toString(state._lastTokenPos)+". Token "+tokenType.name()+" expected", state._lastTokenPos);
//            }
        }
        return newState;
    }
    
    
    public ExpressionNode parseExpression(String expression) throws InvalidSyntaxException {
        _tokens = Tokenizer.getTokens(expression);
        
        if (_tokens.isEmpty()) {
            return null;
        }
        
        ExpressionNodeAndState exprNodeAndState = rule0.parse(new State(0, _tokens.get(0), 0, new Token()));
        
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
//            throw new InvalidSyntaxException(
//                    "invalid syntax at index "
//                            + Integer.toString(exprNodeAndState._state._token._pos)
//                            + ". The expression is not fully parsed",
//                    exprNodeAndState._state._token._pos);
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
    
    
    private final Rule rule0 = new Rule0();
    private final Rule rule1 = new Rule1();
    private final Rule rule2 = new Rule2();
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
    
    
    private class Rule0 implements Rule {

        @Override
        public ExpressionNodeAndState parse(State state) throws InvalidSyntaxException {
            return rule1.parse(state);
        }
        
    }
    
    
    private class Rule1 implements Rule {

        @Override
        public ExpressionNodeAndState parse(State state) throws InvalidSyntaxException {
            return rule2.parse(state);
        }
        
    }
    
    
    private class Rule2 implements Rule {

        @Override
        public ExpressionNodeAndState parse(State state) throws InvalidSyntaxException {
            return rule3.parse(state);
        }
        
    }
    
    
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
                ExpressionNodeAndState exprNodeAndState = rule0.parse(newState);
                if (exprNodeAndState._state._token == null) {
                    throw new InvalidSyntaxException("invalid syntax");
                }
                System.err.format("Rule16: %s, %s%n", exprNodeAndState, exprNodeAndState);
                System.err.format("Rule16: %s, %s%n", exprNodeAndState._state, exprNodeAndState);
                System.err.format("Rule16: %s, %s%n", exprNodeAndState._state._token, exprNodeAndState);
                System.err.format("Rule16: %s, %s%n", exprNodeAndState._state._token._tokenType, exprNodeAndState);
                System.err.format("Rule16: %s, %s%n", exprNodeAndState._state._token._tokenType.name(), exprNodeAndState);
                System.err.format("Rule16: %s, %s%n", exprNodeAndState._state._token._tokenType.name(), exprNodeAndState._state);
                System.err.format("Rule16: %s, %s%n", exprNodeAndState._state._token._tokenType.name(), exprNodeAndState._state._token);
                System.err.format("Rule16: %s, %s%n", exprNodeAndState._state._token._tokenType.name(), exprNodeAndState._state._token._string);
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
                exprNode = new ExpressionNodeIdentifier(newState._lastToken);
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
    
    
    private final static org.slf4j.Logger log = LoggerFactory.getLogger(RecursiveDescentParser.class);
    
}
