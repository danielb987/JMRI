package jmri.jmrit.logixng.string.implementation;

import jmri.util.JUnitUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import jmri.jmrit.logixng.string.expressions.GetStringIO;
import jmri.jmrit.logixng.StringExpressionBean;

/**
 * Test ExpressionTimer
 * 
 * @author Daniel Bergqvist 2018
 */
public class DefaultMaleStringExpressionSocketTest {

    @Test
    public void testCtor() {
        StringExpressionBean expression = new GetStringIO("IQA55:1:SE321");
        Assert.assertNotNull("exists", new DefaultMaleStringExpressionSocket(expression));
    }
    
    // The minimal setup for log4J
    @Before
    public void setUp() {
        JUnitUtil.setUp();
        JUnitUtil.resetProfileManager();
        JUnitUtil.resetInstanceManager();
        JUnitUtil.initInternalSensorManager();
        JUnitUtil.initInternalTurnoutManager();
    }

    @After
    public void tearDown() {
        JUnitUtil.tearDown();
    }
    
}
