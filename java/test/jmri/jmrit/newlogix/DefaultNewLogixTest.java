package jmri.jmrit.newlogix;

import jmri.jmrit.newlogix.engine.DefaultNewLogix;
import jmri.util.JUnitUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test DefaultNewLogix
 * 
 * @author Daniel Bergqvist 2018
 */
public class DefaultNewLogixTest {

    @Test
    public void testCtor() {
        new DefaultNewLogix("IQA55", null);
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
