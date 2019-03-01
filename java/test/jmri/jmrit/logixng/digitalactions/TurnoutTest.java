package jmri.jmrit.logixng.digitalactions;

import jmri.jmrit.logixng.digitalactions.Turnout;
import jmri.util.JUnitUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test Turnout
 * 
 * @author Daniel Bergqvist 2018
 */
public class TurnoutTest {

    @Test
    public void testCtor() {
        new Turnout("IQA55:A321", null);
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
