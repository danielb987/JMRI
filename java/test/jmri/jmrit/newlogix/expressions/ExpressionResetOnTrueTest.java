package jmri.jmrit.newlogix.expressions;

import jmri.InstanceManager;
import jmri.NamedBean;
import jmri.jmrit.newlogix.MaleExpressionSocket;
import jmri.jmrit.newlogix.SocketAlreadyConnectedException;
import jmri.jmrit.newlogix.expressions.ExpressionResetOnTrue;
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
        MaleExpressionSocket primaryExpressionSocket = InstanceManager.getDefault(jmri.jmrit.newlogix.ExpressionManager.class).register(expression);
        expression = new ExpressionTurnout("IQ:50:E322", null);
        MaleExpressionSocket secondaryExpressionSocket = InstanceManager.getDefault(jmri.jmrit.newlogix.ExpressionManager.class).register(expression);
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
