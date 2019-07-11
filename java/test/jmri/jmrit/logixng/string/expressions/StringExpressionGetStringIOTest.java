package jmri.jmrit.logixng.string.expressions;

import jmri.jmrit.logixng.string.expressions.GetStringIO;
import jmri.util.JUnitUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import jmri.jmrit.logixng.StringExpressionBean;

/**
 * Test GetStringIO
 * 
 * @author Daniel Bergqvist 2019
 */
public class StringExpressionGetStringIOTest {

    @Test
    public void testCtor() {
        Assert.assertNotNull("exists", new GetStringIO("IQA55:1:SE321"));
    }
    
    @Test
    public void testShortDescription() {
        StringExpressionBean stringExpression = new GetStringIO("IQA55:1:SE321");
        Assert.assertTrue("String matches", "Read string none".equals(stringExpression.getShortDescription()));
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
