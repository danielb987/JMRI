package jmri.jmrit.newlogix.actions;

import jmri.jmrit.newlogix.Action;
import jmri.util.JUnitUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test ActionIfThen
 * 
 * @author Daniel Bergqvist 2018
 */
public class ActionDoIfTest {

    @Test
    public void testCtor() {
        new ActionIfThen("IQA55:A321", null, ActionIfThen.Type.TRIGGER_ACTION);
        new ActionIfThen("IQA55:A321", null, ActionIfThen.Type.CONTINOUS_ACTION);
    }
    
    @Test
    public void testToString() {
        Action a1 = new ActionIfThen("IQA55:A321", null, ActionIfThen.Type.TRIGGER_ACTION);
        Assert.assertTrue("If E1 then A1".equals(a1.toString()));
        Action a2 = new ActionIfThen("IQA55:A321", null, ActionIfThen.Type.CONTINOUS_ACTION);
        Assert.assertTrue("If E1 then A1".equals(a2.toString()));
    }
    
    // The minimal setup for log4J
    @Before
    public void setUp() {
        JUnitUtil.setUp();
        JUnitUtil.resetProfileManager();
        JUnitUtil.resetInstanceManager();
        JUnitUtil.initInternalSensorManager();
        JUnitUtil.initInternalTurnoutManager();
    }

    @After
    public void tearDown() {
        JUnitUtil.tearDown();
    }
    
}
