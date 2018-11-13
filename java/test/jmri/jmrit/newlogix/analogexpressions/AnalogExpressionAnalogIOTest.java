package jmri.jmrit.newlogix.analogexpressions;

import jmri.util.JUnitUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test ExpressionAnd
 * 
 * @author Daniel Bergqvist 2018
 */
public class AnalogExpressionAnalogIOTest {

    @Test
    public void testCtor() {
        new AnalogExpressionAnalogIO("IQA55:E321");
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
