package jmri.jmrit.newlogix.expressions;

import jmri.jmrit.newlogix.Expression;
import jmri.util.JUnitUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test ExpressionHold
 * 
 * @author Daniel Bergqvist 2018
 */
public class ExpressionHoldTest {

    @Test
    public void testCtor() {
        new ExpressionHold("IQA55:E321");
    }
    
    @Test
    public void testToString() {
        Expression e1 = new ExpressionHold("IQA55:E321");
        Assert.assertTrue("Hold while E1. Trigger on E2".equals(e1.toString()));
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
