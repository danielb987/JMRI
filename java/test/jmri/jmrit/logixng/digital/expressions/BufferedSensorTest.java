package jmri.jmrit.logixng.digital.expressions;

import jmri.jmrit.logixng.DigitalExpression;
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
public class BufferedSensorTest {

    @Test
    public void testCtor() {
        DigitalExpression t = new ExpressionBufferedSensor("IQA55:E321", null);
        Assert.assertNotNull("exists",t);
    }
    
    @Test
    public void testDescription() {
        DigitalExpression e1 = new ExpressionBufferedSensor("IQA55:E321");
        Assert.assertTrue("Get buffered sensor".equals(e1.getShortDescription()));
        Assert.assertTrue("Buffered sensor".equals(e1.getLongDescription()));
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
