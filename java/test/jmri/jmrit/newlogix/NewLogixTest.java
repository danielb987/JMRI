package jmri.jmrit.newlogix;

import jmri.util.JUnitUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test NewLogix
 * 
 * @author Daniel Bergqvist 2018
 */
public class NewLogixTest {

    @Test
    public void testBundle() {
        Assert.assertTrue("bean type is correct", "New Logix".equals(new DefaultNewLogix("IQA55", null, null).getBeanType()));
        Assert.assertTrue("bean type is correct", "Action".equals(new ActionDoIf("IQA55:A321", null, ActionDoIf.Type.TRIGGER_ACTION, null, null).getBeanType()));
        Assert.assertTrue("bean type is correct", "Expression".equals(new ExpressionAnd("IQA55:E321", null).getBeanType()));
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
