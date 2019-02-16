package jmri.jmrit.logixng;

import jmri.util.JUnitUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import jmri.jmrit.logixng.Expression;

/**
 * Test Expression
 * 
 * @author Daniel Bergqvist 2018
 */
public class ExpressionTest {

    @Test
    public void testEnum() {
        Assert.assertTrue("TRUE".equals(Expression.TriggerCondition.TRUE.name()));
        Assert.assertTrue("FALSE".equals(Expression.TriggerCondition.FALSE.name()));
        Assert.assertTrue("CHANGE".equals(Expression.TriggerCondition.CHANGE.name()));
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
