package jmri.jmrit.logixng.digitalexpressions;

import jmri.jmrit.logixng.Category;
import jmri.util.JUnitUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test Timer
 * 
 * @author Daniel Bergqvist 2018
 */
public class TimerTest {

    @Test
    public void testCtor() {
        new Timer(null, "IQA55:E321", null);
    }
    
    @Test
    public void testGetCategory() {
        Assert.assertTrue(Category.COMMON.equals(new Timer(null, "IQA55:E321", null).getCategory()));
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
