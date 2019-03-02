package jmri.jmrit.logixng.analogactions;

import jmri.jmrit.logixng.AnalogAction;
import jmri.util.JUnitUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test AnalogActionSetAnalogIO
 * 
 * @author Daniel Bergqvist 2018
 */
public class AnalogActionSetAnalogIOTest {

    @Test
    public void testCtor() {
        new AnalogActionSetAnalogIO(null, "IQA55:A321");
    }
    
    @Test
    public void testShortDescription() {
        AnalogAction analogAction = new AnalogActionSetAnalogIO(null, "IQA55:A321");
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
