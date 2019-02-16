package jmri.jmrit.logixng.expressions;

import jmri.jmrit.logixng.expressions.ExpressionTurnout;
import jmri.InstanceManager;
import jmri.NamedBean;
import jmri.jmrit.logixng.MaleExpressionSocket;
import jmri.jmrit.logixng.SocketAlreadyConnectedException;
import jmri.jmrit.logixng.expressions.ExpressionResetOnTrue;
import jmri.util.JUnitUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test ExpressionResetOnTrue
 * 
 * @author Daniel Bergqvist 2018
 */
public class ExpressionResetOnTrueTest {

    @Test
    public void testCtor()
            throws NamedBean.BadUserNameException,
                    NamedBean.BadSystemNameException,
                    SocketAlreadyConnectedException {
        ExpressionTurnout expression = new ExpressionTurnout("IQ:A:55:A:E321", null);
        MaleExpressionSocket primaryExpressionSocket = InstanceManager.getDefault(jmri.jmrit.logixng.ExpressionManager.class).register(expression);
        expression = new ExpressionTurnout("IQ:50:E322", null);
        MaleExpressionSocket secondaryExpressionSocket = InstanceManager.getDefault(jmri.jmrit.logixng.ExpressionManager.class).register(expression);
        new ExpressionResetOnTrue("IQA55:E321", null, primaryExpressionSocket, secondaryExpressionSocket);
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
