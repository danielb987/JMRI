package jmri.jmrit.logixng.actions.configurexml;

import jmri.jmrit.logixng.actions.configurexml.ActionManyXml;
import jmri.util.JUnitUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test ActionDoIfXml
 * 
 * @author Daniel Bergqvist 2018
 */
public class ActionManyXmlTest {

    @Test
    public void testCtor() {
        new ActionManyXml();
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
