package jmri.jmrit.logixng;

import jmri.InstanceManager;
import jmri.jmrit.logixng.implementation.DefaultLogixNG;
import jmri.jmrit.logixng.digital.actions.IfThen;
import jmri.jmrit.logixng.digital.actions.ActionTurnout;
import jmri.jmrit.logixng.digital.expressions.And;
import jmri.jmrit.logixng.digital.expressions.ExpressionTurnout;
import jmri.util.JUnitUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test LogixNG
 * 
 * @author Daniel Bergqvist 2018
 */
public class LogixNGTest {

    @Test
    public void testManagers() {
        String systemName;
        LogixNG logixNG = InstanceManager.getDefault(LogixNG_Manager.class).createLogixNG("A new logix for test");  // NOI18N
        systemName = InstanceManager.getDefault(DigitalExpressionManager.class).getNewSystemName(logixNG);
        DigitalExpression expression = new ExpressionTurnout(systemName, "An expression for test");  // NOI18N
        InstanceManager.getDefault(DigitalExpressionManager.class).registerExpression(expression);
//        InstanceManager.getDefault(jmri.DigitalExpressionManager.class).addExpression(new ExpressionTurnout(systemName, "LogixNG 102, DigitalExpression 26"));  // NOI18N
        systemName = InstanceManager.getDefault(DigitalActionManager.class).getNewSystemName(logixNG);
        DigitalAction action = new ActionTurnout(systemName, "An action for test");  // NOI18N
        InstanceManager.getDefault(DigitalActionManager.class).registerAction(action);
    }
    
    @Test
    public void testExceptions() {
        new SocketAlreadyConnectedException().getMessage();
    }
    
    @Test
    public void testBundle() {
        Assert.assertTrue("bean type is correct", "LogixNG".equals(new DefaultLogixNG("IQA55").getBeanType()));
        Assert.assertTrue("bean type is correct", "Action".equals(new IfThen("IQA55:A321", IfThen.Type.TRIGGER_ACTION).getBeanType()));
        Assert.assertTrue("bean type is correct", "Expression".equals(new And("IQA55:E321").getBeanType()));
    }
    
    // The minimal setup for log4J
    @Before
    public void setUp() {
        JUnitUtil.setUp();
        JUnitUtil.resetInstanceManager();
        JUnitUtil.initInternalSensorManager();
        JUnitUtil.initInternalTurnoutManager();
        JUnitUtil.initLogixNGManager();
        JUnitUtil.initDigitalExpressionManager();
        JUnitUtil.initDigitalActionManager();
    }

    @After
    public void tearDown() {
        JUnitUtil.tearDown();
    }
    
}
