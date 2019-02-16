package jmri.jmrit.logixng.actions;

import jmri.jmrit.logixng.actions.ActionIfThenElse;
import jmri.jmrit.logixng.Action;
import jmri.util.JUnitUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test ActionIfThenElse
 * 
 * @author Daniel Bergqvist 2018
 */
public class ActionIfThenElseTest {

    @Test
    public void testCtor() {
        new ActionIfThenElse("IQA55:A321", null, ActionIfThenElse.Type.TRIGGER_ACTION);
        new ActionIfThenElse("IQA55:A321", null, ActionIfThenElse.Type.CONTINOUS_ACTION);
    }
    
    @Test
    public void testToString() {
        Action a1 = new ActionIfThenElse("IQA55:A321", null, ActionIfThenElse.Type.TRIGGER_ACTION);
        Assert.assertTrue("If E1 then A1".equals(a1.getLongDescription()));
        Action a2 = new ActionIfThenElse("IQA55:A321", null, ActionIfThenElse.Type.CONTINOUS_ACTION);
        Assert.assertTrue("If E1 then A1".equals(a2.getLongDescription()));
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
