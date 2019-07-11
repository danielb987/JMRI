package jmri.jmrit.logixng.string.actions;

import jmri.util.JUnitUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import jmri.jmrit.logixng.StringActionBean;

/**
 * Test ActionMany
 * 
 * @author Daniel Bergqvist 2018
 */
public class StringActionSetStringIOTest {

    @Test
    public void testCtor() {
        Assert.assertNotNull("exists", new SetStringIO("IQA55:1:SA321"));
    }
    
    @Test
    public void testShortDescription() {
        StringActionBean stringAction = new SetStringIO("IQA55:1:SA321");
        Assert.assertTrue("String matches", "Set string none".equals(stringAction.getShortDescription()));
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
