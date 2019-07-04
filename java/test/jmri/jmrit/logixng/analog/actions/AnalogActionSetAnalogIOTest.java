package jmri.jmrit.logixng.analog.actions;

import jmri.util.JUnitUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import jmri.jmrit.logixng.AnalogActionBean;

/**
 * Test SetAnalogIO
 * 
 * @author Daniel Bergqvist 2018
 */
public class AnalogActionSetAnalogIOTest {

    @Test
    public void testCtor() {
        new SetAnalogIO("IQA55:A321");
    }
    
    @Test
    public void testShortDescription() {
        AnalogActionBean analogAction = new SetAnalogIO("IQA55:A321");
        Assert.assertTrue("String matches", "Set analog none".equals(analogAction.getShortDescription()));
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
