package jmri.jmrit.logixng.stringactions;

import jmri.jmrit.logixng.StringAction;
import jmri.util.JUnitUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test ActionMany
 * 
 * @author Daniel Bergqvist 2018
 */
public class StringActionSetStringIOTest {

    @Test
    public void testCtor() {
        new StringActionSetStringIO("IQA55:A321");
    }
    
    @Test
    public void testShortDescription() {
        StringAction stringAction = new StringActionSetStringIO("IQA55:A321");
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
