package jmri.jmrit.logixng.engine;

import jmri.jmrit.logixng.engine.DefaultMaleExpressionSocket;
import jmri.util.JUnitUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import jmri.jmrit.logixng.expressions.ExpressionAnd;
import jmri.jmrit.logixng.Expression;

/**
 * Test ExpressionTimer
 * 
 * @author Daniel Bergqvist 2018
 */
public class DefaultMaleExpressionSocketTest {

    @Test
    public void testCtor() {
        Expression expression = new ExpressionAnd("IQA55:E321");
        new DefaultMaleExpressionSocket(expression);
    }
    
    // The minimal setup for log4J
    @Before
    public void setUp() {
        JUnitUtil.setUp();
        JUnitUtil.resetProfileManager();
        JUnitUtil.resetInstanceManager();
        JUnitUtil.initInternalSensorManager();
        JUnitUtil.initInternalTurnoutManager();
    }

    @After
    public void tearDown() {
        JUnitUtil.tearDown();
    }
    
}
