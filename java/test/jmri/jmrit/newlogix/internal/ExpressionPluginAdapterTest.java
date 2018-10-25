package jmri.jmrit.newlogix.internal;

import jmri.jmrit.newlogix.engine.ExpressionPluginAdapter;
import jmri.util.JUnitUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import jmri.jmrit.newlogix.expressions.ExpressionAnd;
import jmri.jmrit.newlogix.Expression;

/**
 * Test ExpressionTimer
 * 
 * @author Daniel Bergqvist 2018
 */
public class ExpressionPluginAdapterTest {

    @Test
    public void testCtor() {
        Expression expression = new ExpressionAnd("IQA55:E321");
        new ExpressionPluginAdapter("SystemName", expression);
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
