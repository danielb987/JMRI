package jmri.jmrit.logixng.digital.expressions;

import jmri.InstanceManager;
import jmri.NamedBean;
import jmri.jmrit.logixng.DigitalExpressionManager;
import jmri.jmrit.logixng.MaleDigitalExpressionSocket;
import jmri.jmrit.logixng.SocketAlreadyConnectedException;
import jmri.util.JUnitUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test ResetOnTrue
 * 
 * @author Daniel Bergqvist 2018
 */
public class ResetOnTrueTest {

    @Test
    public void testCtor()
            throws NamedBean.BadUserNameException,
                    NamedBean.BadSystemNameException,
                    SocketAlreadyConnectedException {
        ExpressionTurnout expression = new ExpressionTurnout("IQ55:DE321", null);
        MaleDigitalExpressionSocket primaryExpressionSocket =
                InstanceManager.getDefault(DigitalExpressionManager.class).registerExpression(expression);
        expression = new ExpressionTurnout("IQ50:DE322", null);
        MaleDigitalExpressionSocket secondaryExpressionSocket =
                InstanceManager.getDefault(DigitalExpressionManager.class).registerExpression(expression);
        new ResetOnTrue("IQA55:DE323", null, primaryExpressionSocket, secondaryExpressionSocket);
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
