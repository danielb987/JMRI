package jmri.jmrit.logixng;

import jmri.util.JUnitUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test DigitalExpression
 * 
 * @author Daniel Bergqvist 2018
 */
public class DigitalExpressionTest {

    @Test
    public void testEnum() {
        Assert.assertTrue("TRUE".equals(DigitalExpression.TriggerCondition.TRUE.name()));
        Assert.assertTrue("FALSE".equals(DigitalExpression.TriggerCondition.FALSE.name()));
        Assert.assertTrue("CHANGE".equals(DigitalExpression.TriggerCondition.CHANGE.name()));
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
