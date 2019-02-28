package jmri.jmrit.logixng.actions;

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
public class ActionDoStringActionTest {

    @Test
    public void testCtor() {
        new ActionDoStringAction("IQA55:A321");
    }
    
    // The minimal setup for log4J
    @Before
    public void setUp() {
        JUnitUtil.setUp();
        JUnitUtil.resetInstanceManager();
        JUnitUtil.initInternalSensorManager();
        JUnitUtil.initInternalTurnoutManager();
        JUnitUtil.initLogixNGManager();
        JUnitUtil.initExpressionManager();
        JUnitUtil.initActionManager();
        JUnitUtil.initStringExpressionManager();
        JUnitUtil.initStringActionManager();
    }

    @After
    public void tearDown() {
        JUnitUtil.tearDown();
    }
    
}
