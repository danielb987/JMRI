package jmri.jmrit.logixng.util.parser;

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
        Token token = new Token();
        token._string = "1";
        ExpressionNodeFloatingNumber expressionNumber = new ExpressionNodeFloatingNumber(token);
        ExpressionNodeBooleanOperator t = new ExpressionNodeBooleanOperator(TokenType.BOOLEAN_NOT, null, expressionNumber);
        Assert.assertNotNull("exists", t);
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
