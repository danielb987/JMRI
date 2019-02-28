package jmri.jmrit.logixng.stringexpressions;

import jmri.jmrit.logixng.StringExpression;
import jmri.util.JUnitUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test StringExpressionGetStringIO
 * 
 * @author Daniel Bergqvist 2019
 */
public class StringExpressionGetStringIOTest {

    @Test
    public void testCtor() {
        new StringExpressionGetStringIO("IQA55:E321");
    }
    
    @Test
    public void testShortDescription() {
        StringExpression stringExpression = new StringExpressionGetStringIO("IQA55:E321");
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
