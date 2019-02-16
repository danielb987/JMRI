package jmri.jmrit.logixng.engine;

import jmri.jmrit.logixng.engine.DefaultLogixNG;
import jmri.util.JUnitUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test DefaultLogixNG
 * 
 * @author Daniel Bergqvist 2018
 */
public class DefaultLogixNGTest {

    @Test
    public void testCtor() {
        new DefaultLogixNG("IQA55", null);
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
