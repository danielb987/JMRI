package jmri.jmrit.logixng.digitalexpressions;

import jmri.util.JUnitUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test ExpressionTurnout
 * 
 * @author Daniel Bergqvist 2018
 */
public class ExpressionTurnoutTest {

    @Test
    public void testCtor() {
        new ExpressionTurnout("IQA55:E321", null);
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
