package jmri.jmrit.logixng.expressions;

import jmri.jmrit.logixng.expressions.ExpressionTurnout;
import jmri.InstanceManager;
import jmri.NamedBean;
import jmri.jmrit.logixng.MaleExpressionSocket;
import jmri.jmrit.logixng.SocketAlreadyConnectedException;
import jmri.jmrit.logixng.expressions.ExpressionTriggerOnce;
import jmri.util.JUnitUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test ExpressionTriggerOnce
 * 
 * @author Daniel Bergqvist 2018
 */
public class ExpressionTriggerOnceTest {

    @Test
    public void testCtor()
            throws NamedBean.BadUserNameException,
                    NamedBean.BadSystemNameException,
                    SocketAlreadyConnectedException {
        ExpressionTurnout expression = new ExpressionTurnout("IQ:A:55:A:E321", null);
        MaleExpressionSocket expressionSocket = InstanceManager.getDefault(jmri.jmrit.logixng.ExpressionManager.class).register(expression);
        new ExpressionTriggerOnce("IQA55:E321", null, expressionSocket);
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
