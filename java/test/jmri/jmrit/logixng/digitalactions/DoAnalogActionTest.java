package jmri.jmrit.logixng.digitalactions;

import jmri.jmrit.logixng.digitalactions.DoAnalogAction;
import jmri.util.JUnitUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test ActionMany
 * 
 * @author Daniel Bergqvist 2018
 */
public class DoAnalogActionTest {

    @Test
    public void testCtor() {
        new DoAnalogAction(null, "IQA55:A321");
    }
    
    // The minimal setup for log4J
    @Before
    public void setUp() {
        JUnitUtil.setUp();
        JUnitUtil.resetInstanceManager();
        JUnitUtil.initInternalSensorManager();
        JUnitUtil.initInternalTurnoutManager();
        JUnitUtil.initLogixNGManager();
        JUnitUtil.initDigitalExpressionManager();
        JUnitUtil.initDigitalActionManager();
        JUnitUtil.initAnalogExpressionManager();
        JUnitUtil.initAnalogActionManager();
    }

    @After
    public void tearDown() {
        JUnitUtil.tearDown();
    }
    
}
