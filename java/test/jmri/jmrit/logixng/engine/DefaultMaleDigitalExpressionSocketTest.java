package jmri.jmrit.logixng.engine;

import jmri.jmrit.logixng.engine.DefaultMaleDigitalExpressionSocket;
import jmri.util.JUnitUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import jmri.jmrit.logixng.digitalexpressions.And;
import jmri.jmrit.logixng.DigitalExpression;

/**
 * Test ExpressionTimer
 * 
 * @author Daniel Bergqvist 2018
 */
public class DefaultMaleDigitalExpressionSocketTest {

    @Test
    public void testCtor() {
        DigitalExpression expression = new And(null, "IQA55:E321");
        new DefaultMaleDigitalExpressionSocket(expression);
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
