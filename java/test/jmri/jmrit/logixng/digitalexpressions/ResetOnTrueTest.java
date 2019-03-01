package jmri.jmrit.logixng.digitalexpressions;

import jmri.InstanceManager;
import jmri.NamedBean;
import jmri.jmrit.logixng.SocketAlreadyConnectedException;
import jmri.util.JUnitUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import jmri.jmrit.logixng.MaleDigitalExpressionSocket;

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
        ExpressionTurnout expression = new ExpressionTurnout("IQ:A:55:A:E321", null);
        MaleDigitalExpressionSocket primaryExpressionSocket = InstanceManager.getDefault(jmri.jmrit.logixng.DigitalExpressionManager.class).register(expression);
        expression = new ExpressionTurnout("IQ:50:E322", null);
        MaleDigitalExpressionSocket secondaryExpressionSocket = InstanceManager.getDefault(jmri.jmrit.logixng.DigitalExpressionManager.class).register(expression);
        new ResetOnTrue("IQA55:E321", null, primaryExpressionSocket, secondaryExpressionSocket);
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
