package jmri.jmrit.logixng.digital.expressions;

import jmri.jmrit.logixng.Category;
import jmri.jmrit.logixng.DigitalExpression;
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
        DigitalExpression t = new Timer("IQA55:E321", null);
        Assert.assertNotNull("exists",t);
    }
    
    @Test
    public void testDescription() {
        DigitalExpression e1 = new Timer("IQA55:E321", null);
        Assert.assertTrue("Timer".equals(e1.getShortDescription()));
        Assert.assertTrue("Timer".equals(e1.getLongDescription()));
    }
    
    @Test
    public void testGetCategory() {
        Assert.assertTrue(Category.COMMON.equals(new Timer("IQA55:E321", null).getCategory()));
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
