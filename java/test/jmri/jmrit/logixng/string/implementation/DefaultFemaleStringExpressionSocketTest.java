package jmri.jmrit.logixng.string.implementation;

import jmri.jmrit.logixng.FemaleSocket;
import jmri.util.JUnitUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import jmri.jmrit.logixng.FemaleSocketListener;
import jmri.jmrit.logixng.FemaleStringExpressionSocket;
import jmri.jmrit.logixng.MaleStringExpressionSocket;
import jmri.jmrit.logixng.StringExpression;
import jmri.jmrit.logixng.string.expressions.StringExpressionGetStringIO;

/**
 * Test ExpressionTimer
 * 
 * @author Daniel Bergqvist 2018
 */
public class DefaultFemaleStringExpressionSocketTest {

    @Test
    public void testFemaleSocket() {
        StringExpression expression = new StringExpressionGetStringIO("IQA55:A321");
        MaleStringExpressionSocket maleSocket = new DefaultMaleStringExpressionSocket(expression);
        FemaleStringExpressionSocket femaleSocket = new DefaultFemaleStringExpressionSocket(null, new FemaleSocketListener() {
            @Override
            public void connected(FemaleSocket socket) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void disconnected(FemaleSocket socket) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        }, "E1");
        
        Assert.assertTrue("String matches", "E1".equals(femaleSocket.getName()));
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
