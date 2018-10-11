package jmri.jmrit.newlogix;

import jmri.util.JUnitUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test ActionTurnout
 * 
 * @author Daniel Bergqvist 2018
 */
public class ActionTurnoutTest {

    @Test
    public void testCtor() {
        new ActionTurnout("IQA55:A321", null);
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
