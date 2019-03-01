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
 * Test TriggerOnce
 * 
 * @author Daniel Bergqvist 2018
 */
public class TriggerOnceTest {

    @Test
    public void testCtor()
            throws NamedBean.BadUserNameException,
                    NamedBean.BadSystemNameException,
                    SocketAlreadyConnectedException {
        ExpressionTurnout expression = new ExpressionTurnout("IQ:A:55:A:E321", null);
        MaleDigitalExpressionSocket expressionSocket = InstanceManager.getDefault(jmri.jmrit.logixng.DigitalExpressionManager.class).register(expression);
        new TriggerOnce("IQA55:E321", null, expressionSocket);
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
