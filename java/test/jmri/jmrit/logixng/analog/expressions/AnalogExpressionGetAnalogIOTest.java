package jmri.jmrit.logixng.analog.expressions;

import jmri.jmrit.logixng.analog.expressions.GetAnalogIO;
import jmri.util.JUnitUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import jmri.jmrit.logixng.AnalogExpressionBean;

/**
 * Test GetAnalogIO
 * 
 * @author Daniel Bergqvist 2018
 */
public class AnalogExpressionGetAnalogIOTest {

    @Test
    public void testCtor() {
        new GetAnalogIO("IQA55:E321");
    }
    
    @Test
    public void testShortDescription() {
        AnalogExpressionBean analogExpression = new GetAnalogIO("IQA55:E321");
        Assert.assertTrue("String matches", "Read analog none".equals(analogExpression.getShortDescription()));
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
