package jmri.jmrit.newlogix.engine;

import jmri.util.JUnitUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import jmri.jmrit.newlogix.analogactions.AnalogActionSetAnalogIO;
import jmri.jmrit.newlogix.AnalogAction;

/**
 * Test ExpressionTimer
 * 
 * @author Daniel Bergqvist 2018
 */
public class DefaultMaleAnalogActionSocketTest {

    @Test
    public void testCtor() {
        AnalogAction action = new AnalogActionSetAnalogIO("IQA55:A321");
        new DefaultMaleAnalogActionSocket(action);
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
