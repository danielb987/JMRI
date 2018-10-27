package jmri.jmrit.newlogix.engine;

import jmri.jmrit.newlogix.engine.InternalAction;
import jmri.util.JUnitUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import jmri.jmrit.newlogix.actions.ActionMany;
import jmri.jmrit.newlogix.Action;

/**
 * Test ExpressionTimer
 * 
 * @author Daniel Bergqvist 2018
 */
public class InternalActionTest {

    @Test
    public void testCtor() {
        Action action = new ActionMany("IQA55:A321");
        new InternalAction(action);
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
