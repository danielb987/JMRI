package jmri.jmrit.logixng.digital.actions;

import jmri.util.JUnitUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import jmri.jmrit.logixng.DigitalActionBean;

/**
 * Test IfThen
 * 
 * @author Daniel Bergqvist 2018
 */
public class IfThenTest {

    @Test
    public void testCtor() {
        new IfThen("IQA55:A321", null, IfThen.Type.TRIGGER_ACTION);
        new IfThen("IQA55:A321", null, IfThen.Type.CONTINOUS_ACTION);
    }
    
    @Test
    public void testToString() {
        DigitalActionBean a1 = new IfThen("IQA55:A321", null, IfThen.Type.TRIGGER_ACTION);
        Assert.assertTrue("If E then A".equals(a1.getLongDescription()));
        DigitalActionBean a2 = new IfThen("IQA55:A321", null, IfThen.Type.CONTINOUS_ACTION);
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
