package jmri.jmrit.logixng.digital.actions.configurexml;

import jmri.util.JUnitUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test IfThenXml
 * 
 * @author Daniel Bergqvist 2018
 */
public class IfThenXmlTest {

    @Test
    public void testCtor() {
        IfThenXml b = new IfThenXml();
        Assert.assertNotNull("exists", b);
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
