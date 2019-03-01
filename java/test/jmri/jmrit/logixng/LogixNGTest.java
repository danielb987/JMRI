package jmri.jmrit.logixng;

import jmri.jmrit.logixng.SocketAlreadyConnectedException;
import jmri.jmrit.logixng.Action;
import jmri.jmrit.logixng.engine.DefaultLogixNG;
import jmri.jmrit.logixng.actions.ActionIfThen;
import jmri.jmrit.logixng.actions.ActionTurnout;
import jmri.jmrit.logixng.digitalexpressions.And;
import jmri.jmrit.logixng.digitalexpressions.ExpressionTurnout;
import jmri.util.JUnitUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import jmri.InstanceManager;
import jmri.jmrit.logixng.LogixNG;
import jmri.jmrit.logixng.DigitalExpression;

/**
 * Test LogixNG
 * 
 * @author Daniel Bergqvist 2018
 */
public class LogixNGTest {

    @Test
    public void testManagers() {
        String systemName;
        LogixNG newLogix = InstanceManager.getDefault(jmri.jmrit.logixng.LogixNG_Manager.class).createLogixNG("A new logix for test");  // NOI18N
        systemName = InstanceManager.getDefault(jmri.jmrit.logixng.DigitalExpressionManager.class).getNewSystemName(newLogix);
        DigitalExpression expression = new ExpressionTurnout(systemName, "An expression for test");  // NOI18N
        InstanceManager.getDefault(jmri.jmrit.logixng.DigitalExpressionManager.class).register(expression);
//        InstanceManager.getDefault(jmri.DigitalExpressionManager.class).addExpression(new ExpressionTurnout(systemName, "LogixNG 102, DigitalExpression 26"));  // NOI18N
        systemName = InstanceManager.getDefault(jmri.jmrit.logixng.ActionManager.class).getNewSystemName(newLogix);
        Action action = new ActionTurnout(systemName, "An action for test");  // NOI18N
        InstanceManager.getDefault(jmri.jmrit.logixng.ActionManager.class).register(action);
    }
    
    @Test
    public void testExceptions() {
        new SocketAlreadyConnectedException().getMessage();
    }
    
    @Test
    public void testBundle() {
        Assert.assertTrue("bean type is correct", "LogixNG".equals(new DefaultLogixNG("IQA55", null).getBeanType()));
        Assert.assertTrue("bean type is correct", "Action".equals(new ActionIfThen("IQA55:A321", null, ActionIfThen.Type.TRIGGER_ACTION).getBeanType()));
        Assert.assertTrue("bean type is correct", "Expression".equals(new And("IQA55:E321", null).getBeanType()));
    }
    
    // The minimal setup for log4J
    @Before
    public void setUp() {
        JUnitUtil.setUp();
        JUnitUtil.resetInstanceManager();
        JUnitUtil.initInternalSensorManager();
        JUnitUtil.initInternalTurnoutManager();
        JUnitUtil.initLogixNGManager();
        JUnitUtil.initExpressionManager();
        JUnitUtil.initActionManager();
    }

    @After
    public void tearDown() {
        JUnitUtil.tearDown();
    }
    
}
