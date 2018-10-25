package jmri.jmrit.newlogix;

import jmri.jmrit.newlogix.expressions.ExpressionTriggerOnce;
import jmri.util.JUnitUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test ExpressionTriggerOnce
 * 
 * @author Daniel Bergqvist 2018
 */
public class ExpressionTriggerOnceTest {

    @Test
    public void testCtor() {
        new ExpressionTriggerOnce("IQA55:E321", null, null);
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
