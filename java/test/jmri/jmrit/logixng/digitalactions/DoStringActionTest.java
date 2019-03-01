package jmri.jmrit.logixng.digitalactions;

import jmri.jmrit.logixng.digitalactions.DoStringAction;
import jmri.util.JUnitUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test DoStringAction
 * 
 * @author Daniel Bergqvist 2019
 */
public class DoStringActionTest {

    @Test
    public void testCtor() {
        new DoStringAction("IQA55:A321");
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
        JUnitUtil.initStringExpressionManager();
        JUnitUtil.initStringActionManager();
    }

    @After
    public void tearDown() {
        JUnitUtil.tearDown();
    }
    
}
