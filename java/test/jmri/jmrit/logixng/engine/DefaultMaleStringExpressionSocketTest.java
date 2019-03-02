package jmri.jmrit.logixng.engine;

import jmri.util.JUnitUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import jmri.jmrit.logixng.StringExpression;
import jmri.jmrit.logixng.stringexpressions.StringExpressionGetStringIO;

/**
 * Test ExpressionTimer
 * 
 * @author Daniel Bergqvist 2018
 */
public class DefaultMaleStringExpressionSocketTest {

    @Test
    public void testCtor() {
        StringExpression expression = new StringExpressionGetStringIO(null, "IQA55:E321");
        new DefaultMaleStringExpressionSocket(expression);
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
