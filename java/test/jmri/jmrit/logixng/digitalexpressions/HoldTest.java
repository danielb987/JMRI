package jmri.jmrit.logixng.digitalexpressions;

import jmri.util.JUnitUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import jmri.jmrit.logixng.DigitalExpression;

/**
 * Test Hold
 * 
 * @author Daniel Bergqvist 2018
 */
public class HoldTest {

    @Test
    public void testCtor() {
        new Hold(null, "IQA55:E321");
    }
    
    @Test
    public void testShortDescription() {
        DigitalExpression e1 = new Hold(null, "IQA55:E321");
        Assert.assertTrue("Hold while E1. Trigger on E2".equals(e1.getShortDescription()));
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
