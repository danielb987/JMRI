package jmri.jmrit.logixng.actions;

import jmri.jmrit.logixng.actions.ActionIfThen;
import jmri.jmrit.logixng.Action;
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
public class ActionIfThenElseTest {

    @Test
    public void testCtor() {
        new ActionIfThen("IQA55:A321", null, ActionIfThen.Type.TRIGGER_ACTION);
        new ActionIfThen("IQA55:A321", null, ActionIfThen.Type.CONTINOUS_ACTION);
    }
    
    @Test
    public void testToString() {
        Action a1 = new ActionIfThen("IQA55:A321", null, ActionIfThen.Type.TRIGGER_ACTION);
        Assert.assertTrue("If E then A".equals(a1.getLongDescription()));
        Action a2 = new ActionIfThen("IQA55:A321", null, ActionIfThen.Type.CONTINOUS_ACTION);
        Assert.assertTrue("If E then A".equals(a2.getLongDescription()));
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
