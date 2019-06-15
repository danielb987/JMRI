package jmri.jmrit.logixng.digital.expressions;

import jmri.jmrit.logixng.DigitalExpression;
import jmri.util.JUnitUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test And
 * 
 * @author Daniel Bergqvist 2018
 */
public class FalseTest {

    @Test
    public void testCtor() {
        DigitalExpression t = new False("IQA55:1:E321");
        Assert.assertNotNull("exists",t);
    }
    
    @Test
    public void testExpression() {
        DigitalExpression t = new False("IQA55:E321");
        Assert.assertFalse("Expression is false",t.evaluate());
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
