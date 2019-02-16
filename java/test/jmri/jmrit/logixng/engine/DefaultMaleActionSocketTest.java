package jmri.jmrit.logixng.engine;

import jmri.jmrit.logixng.engine.DefaultMaleActionSocket;
import jmri.util.JUnitUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import jmri.jmrit.logixng.actions.ActionMany;
import jmri.jmrit.logixng.Action;

/**
 * Test ExpressionTimer
 * 
 * @author Daniel Bergqvist 2018
 */
public class DefaultMaleActionSocketTest {

    @Test
    public void testCtor() {
        Action action = new ActionMany("IQA55:A321");
        new DefaultMaleActionSocket(action);
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
