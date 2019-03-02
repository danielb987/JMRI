package jmri.jmrit.logixng.digitalactions;

import jmri.jmrit.logixng.digitalactions.IfThen;
import jmri.util.JUnitUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import jmri.jmrit.logixng.DigitalAction;

/**
 * Test IfThen
 * 
 * @author Daniel Bergqvist 2018
 */
public class IfThenElseTest {

    @Test
    public void testCtor() {
        new IfThen(null, "IQA55:A321", null, IfThen.Type.TRIGGER_ACTION);
        new IfThen(null, "IQA55:A321", null, IfThen.Type.CONTINOUS_ACTION);
    }
    
    @Test
    public void testToString() {
        DigitalAction a1 = new IfThen(null, "IQA55:A321", null, IfThen.Type.TRIGGER_ACTION);
        Assert.assertTrue("If E then A".equals(a1.getLongDescription()));
        DigitalAction a2 = new IfThen(null, "IQA55:A321", null, IfThen.Type.CONTINOUS_ACTION);
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
