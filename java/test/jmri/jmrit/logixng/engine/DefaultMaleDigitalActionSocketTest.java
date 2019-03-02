package jmri.jmrit.logixng.engine;

import jmri.jmrit.logixng.engine.DefaultMaleDigitalActionSocket;
import jmri.util.JUnitUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import jmri.jmrit.logixng.digitalactions.Many;
import jmri.jmrit.logixng.DigitalAction;

/**
 * Test ExpressionTimer
 * 
 * @author Daniel Bergqvist 2018
 */
public class DefaultMaleDigitalActionSocketTest {

    @Test
    public void testCtor() {
        DigitalAction action = new Many(null, "IQA55:A321");
        new DefaultMaleDigitalActionSocket(action);
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
