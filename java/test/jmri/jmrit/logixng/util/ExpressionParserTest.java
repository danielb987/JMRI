package jmri.jmrit.logixng.util;

import java.util.List;
import jmri.jmrit.logixng.util.ExpressionParser.Function;
import jmri.jmrit.logixng.util.ExpressionParser.OperatorInfo;
import jmri.jmrit.logixng.util.ExpressionParser.TokenType;
import jmri.util.JUnitUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test ExpressionParser
 * 
 * @author Daniel Bergqvist 2018
 */
public class ExpressionParserTest {

    @Test
    public void testCtor() {
        ExpressionParser t = new ExpressionParser();
        Assert.assertNotNull("not null", t);
    }
    
    private void checkFirstToken(
            List<ExpressionParser<Float>.Token> tokens,
            TokenType tokenType, String string) {
        
        Assert.assertTrue("list is not empty", tokens.size() > 0);
        System.out.format("Type: %s, String: '%s'%n", tokens.get(0).tokenType, tokens.get(0).string);
        Assert.assertTrue("token type matches", tokens.get(0).tokenType == tokenType);
        Assert.assertTrue("string matches", string.equals(tokens.get(0).string));
        
        tokens.remove(0);
    }
    
    @Test
    public void testGetTokens() throws InvalidSyntaxException {
        ExpressionParser<Float> parser = new ExpressionParser<>();
        Assert.assertNotNull("not null", parser);
        
        List<ExpressionParser<Float>.Token> tokens;
        
        tokens = parser.getTokens("");
        Assert.assertTrue("list is empty", tokens.isEmpty());
        
        tokens = parser.getTokens("R1ABC");
        checkFirstToken(tokens, TokenType.IDENTIFIER, "R1ABC");
        Assert.assertTrue("list is empty", tokens.isEmpty());
        
        tokens = parser.getTokens("R1ABC");
        checkFirstToken(tokens, TokenType.IDENTIFIER, "R1ABC");
        Assert.assertTrue("list is empty", tokens.isEmpty());
        
        tokens = parser.getTokens("321");
        checkFirstToken(tokens, TokenType.NUMBER, "321");
        Assert.assertTrue("list is empty", tokens.isEmpty());
        
        tokens = parser.getTokens("32.221");
        checkFirstToken(tokens, TokenType.NUMBER, "32.221");
        Assert.assertTrue("list is empty", tokens.isEmpty());
        
        tokens = parser.getTokens("321 353");
        checkFirstToken(tokens, TokenType.NUMBER, "321");
        checkFirstToken(tokens, TokenType.NUMBER, "353");
        Assert.assertTrue("list is empty", tokens.isEmpty());
        
        tokens = parser.getTokens("321   353");
        checkFirstToken(tokens, TokenType.NUMBER, "321");
        checkFirstToken(tokens, TokenType.NUMBER, "353");
        Assert.assertTrue("list is empty", tokens.isEmpty());
        
        tokens = parser.getTokens("321354");
        checkFirstToken(tokens, TokenType.NUMBER, "321354");
        Assert.assertTrue("list is empty", tokens.isEmpty());
        
        tokens = parser.getTokens("(");
        checkFirstToken(tokens, TokenType.LEFT_PARENTHESIS, "(");
        Assert.assertTrue("list is empty", tokens.isEmpty());
        
        tokens = parser.getTokens(")");
        checkFirstToken(tokens, TokenType.RIGHT_PARENTHESIS, ")");
        Assert.assertTrue("list is empty", tokens.isEmpty());
        
        tokens = parser.getTokens("(R1)");
        checkFirstToken(tokens, TokenType.LEFT_PARENTHESIS, "(");
        checkFirstToken(tokens, TokenType.IDENTIFIER, "R1");
        checkFirstToken(tokens, TokenType.RIGHT_PARENTHESIS, ")");
        Assert.assertTrue("list is empty", tokens.isEmpty());
        
        tokens = parser.getTokens("&&");
        checkFirstToken(tokens, TokenType.BOOLEAN_AND, "&");    // The second & is eaten by the parser and not included in the string.
        Assert.assertTrue("list is empty", tokens.isEmpty());
        
        tokens = parser.getTokens("R1 && R2");
        checkFirstToken(tokens, TokenType.IDENTIFIER, "R1");
        checkFirstToken(tokens, TokenType.BOOLEAN_AND, "&");    // The second & is eaten by the parser and not included in the string.
        checkFirstToken(tokens, TokenType.IDENTIFIER, "R2");
        Assert.assertTrue("list is empty", tokens.isEmpty());
        
        tokens = parser.getTokens("R1(x)");
        checkFirstToken(tokens, TokenType.IDENTIFIER, "R1");
        checkFirstToken(tokens, TokenType.LEFT_PARENTHESIS, "(");
        checkFirstToken(tokens, TokenType.IDENTIFIER, "x");
        checkFirstToken(tokens, TokenType.RIGHT_PARENTHESIS, ")");
        Assert.assertTrue("list is empty", tokens.isEmpty());
        
        tokens = parser.getTokens("R1[x]");
        checkFirstToken(tokens, TokenType.IDENTIFIER, "R1");
        checkFirstToken(tokens, TokenType.LEFT_SQUARE_BRACKET, "[");
        checkFirstToken(tokens, TokenType.IDENTIFIER, "x");
        checkFirstToken(tokens, TokenType.RIGHT_SQUARE_BRACKET, "]");
        Assert.assertTrue("list is empty", tokens.isEmpty());
        
        tokens = parser.getTokens("{x,y,z}[a]");
        checkFirstToken(tokens, TokenType.LEFT_CURLY_BRACKET, "{");
        checkFirstToken(tokens, TokenType.IDENTIFIER, "x");
        checkFirstToken(tokens, TokenType.COMMA, ",");
        checkFirstToken(tokens, TokenType.IDENTIFIER, "y");
        checkFirstToken(tokens, TokenType.COMMA, ",");
        checkFirstToken(tokens, TokenType.IDENTIFIER, "z");
        checkFirstToken(tokens, TokenType.RIGHT_CURLY_BRACKET, "}");
        checkFirstToken(tokens, TokenType.LEFT_SQUARE_BRACKET, "[");
        checkFirstToken(tokens, TokenType.IDENTIFIER, "a");
        checkFirstToken(tokens, TokenType.RIGHT_SQUARE_BRACKET, "]");
        Assert.assertTrue("list is empty", tokens.isEmpty());
        
        tokens = parser.getTokens("{x,y,z}[a..b]");
        checkFirstToken(tokens, TokenType.LEFT_CURLY_BRACKET, "{");
        checkFirstToken(tokens, TokenType.IDENTIFIER, "x");
        checkFirstToken(tokens, TokenType.COMMA, ",");
        checkFirstToken(tokens, TokenType.IDENTIFIER, "y");
        checkFirstToken(tokens, TokenType.COMMA, ",");
        checkFirstToken(tokens, TokenType.IDENTIFIER, "z");
        checkFirstToken(tokens, TokenType.RIGHT_CURLY_BRACKET, "}");
        checkFirstToken(tokens, TokenType.LEFT_SQUARE_BRACKET, "[");
        checkFirstToken(tokens, TokenType.IDENTIFIER, "a");
        checkFirstToken(tokens, TokenType.DOT_DOT, ".");    // The second dot is eaten by the parser and not included in the string.
        checkFirstToken(tokens, TokenType.IDENTIFIER, "b");
        checkFirstToken(tokens, TokenType.RIGHT_SQUARE_BRACKET, "]");
        Assert.assertTrue("list is empty", tokens.isEmpty());
        
        tokens = parser.getTokens("{x,y,z}[a..b,c]");
        checkFirstToken(tokens, TokenType.LEFT_CURLY_BRACKET, "{");
        checkFirstToken(tokens, TokenType.IDENTIFIER, "x");
        checkFirstToken(tokens, TokenType.COMMA, ",");
        checkFirstToken(tokens, TokenType.IDENTIFIER, "y");
        checkFirstToken(tokens, TokenType.COMMA, ",");
        checkFirstToken(tokens, TokenType.IDENTIFIER, "z");
        checkFirstToken(tokens, TokenType.RIGHT_CURLY_BRACKET, "}");
        checkFirstToken(tokens, TokenType.LEFT_SQUARE_BRACKET, "[");
        checkFirstToken(tokens, TokenType.IDENTIFIER, "a");
        checkFirstToken(tokens, TokenType.DOT_DOT, ".");    // The second dot is eaten by the parser and not included in the string.
        checkFirstToken(tokens, TokenType.IDENTIFIER, "b");
        checkFirstToken(tokens, TokenType.COMMA, ",");
        checkFirstToken(tokens, TokenType.IDENTIFIER, "c");
        checkFirstToken(tokens, TokenType.RIGHT_SQUARE_BRACKET, "]");
        Assert.assertTrue("list is empty", tokens.isEmpty());
        
        tokens = parser.getTokens("{x,y,z}[a,b..c]");
        checkFirstToken(tokens, TokenType.LEFT_CURLY_BRACKET, "{");
        checkFirstToken(tokens, TokenType.IDENTIFIER, "x");
        checkFirstToken(tokens, TokenType.COMMA, ",");
        checkFirstToken(tokens, TokenType.IDENTIFIER, "y");
        checkFirstToken(tokens, TokenType.COMMA, ",");
        checkFirstToken(tokens, TokenType.IDENTIFIER, "z");
        checkFirstToken(tokens, TokenType.RIGHT_CURLY_BRACKET, "}");
        checkFirstToken(tokens, TokenType.LEFT_SQUARE_BRACKET, "[");
        checkFirstToken(tokens, TokenType.IDENTIFIER, "a");
        checkFirstToken(tokens, TokenType.COMMA, ",");
        checkFirstToken(tokens, TokenType.IDENTIFIER, "b");
        checkFirstToken(tokens, TokenType.DOT_DOT, ".");    // The second dot is eaten by the parser and not included in the string.
        checkFirstToken(tokens, TokenType.IDENTIFIER, "c");
        checkFirstToken(tokens, TokenType.RIGHT_SQUARE_BRACKET, "]");
        Assert.assertTrue("list is empty", tokens.isEmpty());
        
        tokens = parser.getTokens("{x,y,z}[a,b..c,d,e,f..g]");
        checkFirstToken(tokens, TokenType.LEFT_CURLY_BRACKET, "{");
        checkFirstToken(tokens, TokenType.IDENTIFIER, "x");
        checkFirstToken(tokens, TokenType.COMMA, ",");
        checkFirstToken(tokens, TokenType.IDENTIFIER, "y");
        checkFirstToken(tokens, TokenType.COMMA, ",");
        checkFirstToken(tokens, TokenType.IDENTIFIER, "z");
        checkFirstToken(tokens, TokenType.RIGHT_CURLY_BRACKET, "}");
        checkFirstToken(tokens, TokenType.LEFT_SQUARE_BRACKET, "[");
        checkFirstToken(tokens, TokenType.IDENTIFIER, "a");
        checkFirstToken(tokens, TokenType.COMMA, ",");
        checkFirstToken(tokens, TokenType.IDENTIFIER, "b");
        checkFirstToken(tokens, TokenType.DOT_DOT, ".");    // The second dot is eaten by the parser and not included in the string.
        checkFirstToken(tokens, TokenType.IDENTIFIER, "c");
        checkFirstToken(tokens, TokenType.COMMA, ",");
        checkFirstToken(tokens, TokenType.IDENTIFIER, "d");
        checkFirstToken(tokens, TokenType.COMMA, ",");
        checkFirstToken(tokens, TokenType.IDENTIFIER, "e");
        checkFirstToken(tokens, TokenType.COMMA, ",");
        checkFirstToken(tokens, TokenType.IDENTIFIER, "f");
        checkFirstToken(tokens, TokenType.DOT_DOT, ".");    // The second dot is eaten by the parser and not included in the string.
        checkFirstToken(tokens, TokenType.IDENTIFIER, "g");
        checkFirstToken(tokens, TokenType.RIGHT_SQUARE_BRACKET, "]");
        Assert.assertTrue("list is empty", tokens.isEmpty());
        
        tokens = parser.getTokens("(R1(x))");
        checkFirstToken(tokens, TokenType.LEFT_PARENTHESIS, "(");
        checkFirstToken(tokens, TokenType.IDENTIFIER, "R1");
        checkFirstToken(tokens, TokenType.LEFT_PARENTHESIS, "(");
        checkFirstToken(tokens, TokenType.IDENTIFIER, "x");
        checkFirstToken(tokens, TokenType.RIGHT_PARENTHESIS, ")");
        checkFirstToken(tokens, TokenType.RIGHT_PARENTHESIS, ")");
        Assert.assertTrue("list is empty", tokens.isEmpty());
        
        tokens = parser.getTokens("R1(x)*(y+21.2)-2.12/R12");
        checkFirstToken(tokens, TokenType.IDENTIFIER, "R1");
        checkFirstToken(tokens, TokenType.LEFT_PARENTHESIS, "(");
        checkFirstToken(tokens, TokenType.IDENTIFIER, "x");
        checkFirstToken(tokens, TokenType.RIGHT_PARENTHESIS, ")");
        checkFirstToken(tokens, TokenType.MULTIPLY, "*");
        checkFirstToken(tokens, TokenType.LEFT_PARENTHESIS, "(");
        checkFirstToken(tokens, TokenType.IDENTIFIER, "y");
        checkFirstToken(tokens, TokenType.ADD, "+");
        checkFirstToken(tokens, TokenType.NUMBER, "21.2");
        checkFirstToken(tokens, TokenType.RIGHT_PARENTHESIS, ")");
        checkFirstToken(tokens, TokenType.SUBTRACKT, "-");
        checkFirstToken(tokens, TokenType.NUMBER, "2.12");
        checkFirstToken(tokens, TokenType.DIVIDE, "/");
        checkFirstToken(tokens, TokenType.IDENTIFIER, "R12");
        Assert.assertTrue("list is empty", tokens.isEmpty());
        
    }
    
    @Test
    public void testParseAndCalculate() {
        ExpressionParser<Float> parser = new ExpressionParser<>();
        Assert.assertNotNull("not null", parser);
        
//        parser.addUnaryOperator("-", negateOperator);
        parser.addBinaryOperator("+", addOperator);
        parser.addBinaryOperator("-", subtractOperator);
        parser.addBinaryOperator("*", multiplyOperator);
        parser.addBinaryOperator("/", divideOperator);
    }
    
    // The minimal setup for log4J
    @Before
    public void setUp() {
        JUnitUtil.setUp();
        JUnitUtil.resetInstanceManager();
        JUnitUtil.initInternalSensorManager();
        JUnitUtil.initInternalTurnoutManager();
    }

    @After
    public void tearDown() {
        JUnitUtil.tearDown();
    }
    
    
/*    
    private final OperatorInfo<Float> negateOperator = new OperatorInfo<Float>() {
            @Override
            public Function<Float> getFunction() {
                return (Float param1, Float param2) -> param1 + param2;
            }

            @Override
            public int getPriority() {
                return 1;
            }
        };
*/    
    private final OperatorInfo<Float> addOperator = new OperatorInfo<Float>() {
            @Override
            public Function<Float> getFunction() {
                return (Float param1, Float param2) -> param1 + param2;
            }

            @Override
            public int getPriority() {
                return 1;
            }
        };
    
    private final OperatorInfo<Float> subtractOperator = new OperatorInfo<Float>() {
            @Override
            public Function<Float> getFunction() {
                return (Float param1, Float param2) -> param1 - param2;
            }

            @Override
            public int getPriority() {
                return 1;
            }
        };
    
    private final OperatorInfo<Float> multiplyOperator = new OperatorInfo<Float>() {
            @Override
            public Function<Float> getFunction() {
                return (Float param1, Float param2) -> param1 * param2;
            }

            @Override
            public int getPriority() {
                return 2;
            }
        };
    
    private final OperatorInfo<Float> divideOperator = new OperatorInfo<Float>() {
            @Override
            public Function<Float> getFunction() {
                return (Float param1, Float param2) -> param1 / param2;
            }

            @Override
            public int getPriority() {
                return 2;
            }
        };
    
    
}
