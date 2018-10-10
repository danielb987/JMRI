package jmri.jmrit.newlogix;

import jmri.util.JUnitUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test ActionDoIf
 * 
 * @author Daniel Bergqvist 2018
 */
public class ActionDoIfTest {

    @Test
    public void testCtor() {
        new ActionDoIf(ActionDoIf.Type.TRIGGER_ACTION, null, null);
        new ActionDoIf(ActionDoIf.Type.CONTINOUS_ACTION, null, null);
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
