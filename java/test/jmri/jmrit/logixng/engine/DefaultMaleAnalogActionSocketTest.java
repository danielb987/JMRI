package jmri.jmrit.logixng.engine;

import jmri.util.JUnitUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import jmri.jmrit.logixng.AnalogAction;
import jmri.jmrit.logixng.analogactions.AnalogActionSetAnalogIO;

/**
 * Test ExpressionTimer
 * 
 * @author Daniel Bergqvist 2018
 */
public class DefaultMaleAnalogActionSocketTest {

    @Test
    public void testCtor() {
        AnalogAction action = new AnalogActionSetAnalogIO(null, "IQA55:A321");
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
