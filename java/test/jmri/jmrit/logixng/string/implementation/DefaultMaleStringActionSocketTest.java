package jmri.jmrit.logixng.string.implementation;

import jmri.util.JUnitUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import jmri.jmrit.logixng.string.actions.SetStringIO;
import jmri.jmrit.logixng.StringActionBean;

/**
 * Test ExpressionTimer
 * 
 * @author Daniel Bergqvist 2018
 */
public class DefaultMaleStringActionSocketTest {

    @Test
    public void testCtor() {
        StringActionBean action = new SetStringIO("IQA55:1:SA321");
        Assert.assertNotNull("exists", new DefaultMaleStringActionSocket(action));
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
