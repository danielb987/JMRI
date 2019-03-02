package jmri.jmrit.logixng.engine;

import jmri.util.JUnitUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import jmri.jmrit.logixng.StringAction;
import jmri.jmrit.logixng.stringactions.StringActionSetStringIO;

/**
 * Test ExpressionTimer
 * 
 * @author Daniel Bergqvist 2018
 */
public class DefaultMaleStringActionSocketTest {

    @Test
    public void testCtor() {
        StringAction action = new StringActionSetStringIO(null, "IQA55:A321");
        new DefaultMaleStringActionSocket(action);
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
