package jmri.jmrit.logixng.util.parser.expressionnode;

import java.util.concurrent.atomic.AtomicBoolean;
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
public class ExpressionNodeBooleanOperatorTest {

    @Test
    public void testCtor() {
        Token token = new Token(TokenType.NONE, "1", 0);
        ExpressionNodeFloatingNumber expressionNumber = new ExpressionNodeFloatingNumber(token);
        ExpressionNodeBooleanOperator t = new ExpressionNodeBooleanOperator(TokenType.BOOLEAN_NOT, null, expressionNumber);
        Assert.assertNotNull("exists", t);
        
        AtomicBoolean hasThrown = new AtomicBoolean(false);
        
        // Test right side is null
        try {
            new ExpressionNodeBooleanOperator(TokenType.BOOLEAN_NOT, null, null);
        } catch (IllegalArgumentException e) {
            hasThrown.set(true);
        }
        Assert.assertTrue("exception is thrown", hasThrown.get());
        
        // Test invalid token
        try {
            new ExpressionNodeBooleanOperator(TokenType.BINARY_AND, null, null);
        } catch (IllegalArgumentException e) {
            hasThrown.set(true);
        }
        Assert.assertTrue("exception is thrown", hasThrown.get());
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
