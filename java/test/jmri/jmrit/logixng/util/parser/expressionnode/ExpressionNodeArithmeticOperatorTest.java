package jmri.jmrit.logixng.util.parser.expressionnode;

import java.util.concurrent.atomic.AtomicBoolean;
import jmri.jmrit.logixng.util.parser.ParserException;
import jmri.jmrit.logixng.util.parser.Token;
import jmri.jmrit.logixng.util.parser.TokenType;
import jmri.util.JUnitUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test ParsedExpression
 * 
 * @author Daniel Bergqvist 2019
 */
public class ExpressionNodeArithmeticOperatorTest {

    @Test
    public void testEnum() {
        jmri.jmrit.logixng.TestEnumScaffold testEnumScaffold = new jmri.jmrit.logixng.TestEnumScaffold();
        testEnumScaffold.test();
    }
    
    @Test
    public void testCtor() throws ParserException {
        
        ExpressionNode exprTrue = new ExpressionNodeTrue();
        
        Token token = new Token(TokenType.NONE, "1", 0);
        ExpressionNodeFloatingNumber expressionNumber = new ExpressionNodeFloatingNumber(token);
        ExpressionNodeArithmeticOperator t = new ExpressionNodeArithmeticOperator(TokenType.ADD, null, expressionNumber);
        Assert.assertNotNull("exists", t);
        
        AtomicBoolean hasThrown = new AtomicBoolean(false);
        
        // Test right side is null
        try {
            new ExpressionNodeArithmeticOperator(TokenType.ADD, null, null);
        } catch (IllegalArgumentException e) {
            hasThrown.set(true);
        }
        Assert.assertTrue("exception is thrown", hasThrown.get());
        
        // Test invalid token
        hasThrown.set(false);
        try {
            new ExpressionNodeArithmeticOperator(TokenType.BINARY_AND, null, null);
        } catch (IllegalArgumentException e) {
            hasThrown.set(true);
        }
        Assert.assertTrue("exception is thrown", hasThrown.get());
        
        // MULTIPLY requires two operands
        hasThrown.set(false);
        try {
            new ExpressionNodeArithmeticOperator(TokenType.MULTIPLY, null, exprTrue);
        } catch (IllegalArgumentException e) {
            hasThrown.set(true);
        }
        Assert.assertTrue("exception is thrown", hasThrown.get());
        
        // DIVIDE requires two operands
        hasThrown.set(false);
        try {
            new ExpressionNodeArithmeticOperator(TokenType.DIVIDE, null, exprTrue);
        } catch (IllegalArgumentException e) {
            hasThrown.set(true);
        }
        Assert.assertTrue("exception is thrown", hasThrown.get());
    }
    
    @Test
    public void testCalculate() throws ParserException {
        
        ExpressionNode expr12_34 = new ExpressionNodeFloatingNumber(new Token(TokenType.NONE, "12.34", 0));
        ExpressionNode expr25_46 = new ExpressionNodeFloatingNumber(new Token(TokenType.NONE, "25.46", 0));
        ExpressionNode expr12 = new ExpressionNodeFloatingNumber(new Token(TokenType.NONE, "12", 0));
        ExpressionNode expr23 = new ExpressionNodeFloatingNumber(new Token(TokenType.NONE, "23", 0));
        
        Assert.assertEquals("calculate() gives the correct value",
                37.8,
                (double)new ExpressionNodeArithmeticOperator(TokenType.ADD, expr12_34, expr25_46).calculate(),
                0.00000001);
        Assert.assertEquals("calculate() gives the correct value",
                35,
                (double)new ExpressionNodeArithmeticOperator(TokenType.ADD, expr12, expr23).calculate(),
                0.00000001);
        Assert.assertEquals("calculate() gives the correct value",
                24.34,
                (double)new ExpressionNodeArithmeticOperator(TokenType.ADD, expr12, expr12_34).calculate(),
                0.00000001);
        
        Assert.assertEquals("calculate() gives the correct value",
                -13.12,
                (double)new ExpressionNodeArithmeticOperator(TokenType.SUBTRACKT, expr12_34, expr25_46).calculate(),
                0.00000001);
        Assert.assertEquals("calculate() gives the correct value",
                -11,
                (double)new ExpressionNodeArithmeticOperator(TokenType.SUBTRACKT, expr12, expr23).calculate(),
                0.00000001);
        Assert.assertEquals("calculate() gives the correct value",
                -0.34,
                (double)new ExpressionNodeArithmeticOperator(TokenType.SUBTRACKT, expr12, expr12_34).calculate(),
                0.00000001);
        
        Assert.assertEquals("calculate() gives the correct value",
                314.1764,
                (double)new ExpressionNodeArithmeticOperator(TokenType.MULTIPLY, expr12_34, expr25_46).calculate(),
                0.00000001);
        Assert.assertEquals("calculate() gives the correct value",
                276,
                (double)new ExpressionNodeArithmeticOperator(TokenType.MULTIPLY, expr12, expr23).calculate(),
                0.00000001);
        Assert.assertEquals("calculate() gives the correct value",
                148.08,
                (double)new ExpressionNodeArithmeticOperator(TokenType.MULTIPLY, expr12, expr12_34).calculate(),
                0.00000001);
        
        Assert.assertEquals("calculate() gives the correct value",
                0.4846818538884525,
                (double)new ExpressionNodeArithmeticOperator(TokenType.DIVIDE, expr12_34, expr25_46).calculate(),
                0.00000001);
        Assert.assertEquals("calculate() gives the correct value",
                0.5217391304347826,
                (double)new ExpressionNodeArithmeticOperator(TokenType.DIVIDE, expr12, expr23).calculate(),
                0.00000001);
        Assert.assertEquals("calculate() gives the correct value",
                0.9724473257698542,
                (double)new ExpressionNodeArithmeticOperator(TokenType.DIVIDE, expr12, expr12_34).calculate(),
                0.00000001);
    }
    
    @Test
    public void testGetDefinitionString() throws ParserException {
        
        ExpressionNode expr12_34 = new ExpressionNodeFloatingNumber(new Token(TokenType.NONE, "12.34", 0));
        ExpressionNode expr25_46 = new ExpressionNodeFloatingNumber(new Token(TokenType.NONE, "25.46", 0));
        ExpressionNode expr12 = new ExpressionNodeFloatingNumber(new Token(TokenType.NONE, "12", 0));
        ExpressionNode expr23 = new ExpressionNodeFloatingNumber(new Token(TokenType.NONE, "23", 0));
        
        Assert.assertEquals("getDefinitionString() gives the correct value",
                "(null)+(FloatNumber:12.34)",
                new ExpressionNodeArithmeticOperator(TokenType.ADD, null, expr12_34)
                        .getDefinitionString());
        Assert.assertEquals("calculate gives the correct value",
                "(null)-(FloatNumber:12)",
                new ExpressionNodeArithmeticOperator(TokenType.SUBTRACKT, null, expr12)
                        .getDefinitionString());
        
        Assert.assertEquals("getDefinitionString() gives the correct value",
                "(FloatNumber:12.34)+(FloatNumber:25.46)",
                new ExpressionNodeArithmeticOperator(TokenType.ADD, expr12_34, expr25_46)
                        .getDefinitionString());
        Assert.assertEquals("getDefinitionString() gives the correct value",
                "(FloatNumber:12.34)+(FloatNumber:12)",
                new ExpressionNodeArithmeticOperator(TokenType.ADD, expr12_34, expr12)
                        .getDefinitionString());
        Assert.assertEquals("getDefinitionString() gives the correct value",
                "(FloatNumber:12)+(FloatNumber:23)",
                new ExpressionNodeArithmeticOperator(TokenType.ADD, expr12, expr23)
                        .getDefinitionString());
        Assert.assertEquals("getDefinitionString() gives the correct value",
                "(FloatNumber:12)+(FloatNumber:12.34)",
                new ExpressionNodeArithmeticOperator(TokenType.ADD, expr12, expr12_34)
                        .getDefinitionString());
        
        Assert.assertEquals("getDefinitionString() gives the correct value",
                "(FloatNumber:12.34)-(FloatNumber:25.46)",
                new ExpressionNodeArithmeticOperator(TokenType.SUBTRACKT, expr12_34, expr25_46)
                        .getDefinitionString());
        Assert.assertEquals("getDefinitionString() gives the correct value",
                "(FloatNumber:12.34)-(FloatNumber:12)",
                new ExpressionNodeArithmeticOperator(TokenType.SUBTRACKT, expr12_34, expr12)
                        .getDefinitionString());
        Assert.assertEquals("getDefinitionString() gives the correct value",
                "(FloatNumber:12)-(FloatNumber:23)",
                new ExpressionNodeArithmeticOperator(TokenType.SUBTRACKT, expr12, expr23)
                        .getDefinitionString());
        Assert.assertEquals("getDefinitionString() gives the correct value",
                "(FloatNumber:12)-(FloatNumber:12.34)",
                new ExpressionNodeArithmeticOperator(TokenType.SUBTRACKT, expr12, expr12_34)
                        .getDefinitionString());
        
        Assert.assertEquals("getDefinitionString() gives the correct value",
                "(FloatNumber:12.34)*(FloatNumber:25.46)",
                new ExpressionNodeArithmeticOperator(TokenType.MULTIPLY, expr12_34, expr25_46)
                        .getDefinitionString());
        Assert.assertEquals("getDefinitionString() gives the correct value",
                "(FloatNumber:12.34)*(FloatNumber:12)",
                new ExpressionNodeArithmeticOperator(TokenType.MULTIPLY, expr12_34, expr12)
                        .getDefinitionString());
        Assert.assertEquals("getDefinitionString() gives the correct value",
                "(FloatNumber:12)*(FloatNumber:23)",
                new ExpressionNodeArithmeticOperator(TokenType.MULTIPLY, expr12, expr23)
                        .getDefinitionString());
        Assert.assertEquals("getDefinitionString() gives the correct value",
                "(FloatNumber:12)*(FloatNumber:12.34)",
                new ExpressionNodeArithmeticOperator(TokenType.MULTIPLY, expr12, expr12_34)
                        .getDefinitionString());
        
        Assert.assertEquals("getDefinitionString() gives the correct value",
                "(FloatNumber:12.34)/(FloatNumber:25.46)",
                new ExpressionNodeArithmeticOperator(TokenType.DIVIDE, expr12_34, expr25_46)
                        .getDefinitionString());
        Assert.assertEquals("getDefinitionString() gives the correct value",
                "(FloatNumber:12.34)/(FloatNumber:12)",
                new ExpressionNodeArithmeticOperator(TokenType.DIVIDE, expr12_34, expr12)
                        .getDefinitionString());
        Assert.assertEquals("getDefinitionString() gives the correct value",
                "(FloatNumber:12)/(FloatNumber:23)",
                new ExpressionNodeArithmeticOperator(TokenType.DIVIDE, expr12, expr23)
                        .getDefinitionString());
        Assert.assertEquals("getDefinitionString() gives the correct value",
                "(FloatNumber:12)/(FloatNumber:12.34)",
                new ExpressionNodeArithmeticOperator(TokenType.DIVIDE, expr12, expr12_34)
                        .getDefinitionString());
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
    
}
