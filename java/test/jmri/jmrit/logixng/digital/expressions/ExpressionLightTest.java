package jmri.jmrit.logixng.digital.expressions;

import java.util.concurrent.atomic.AtomicBoolean;
import jmri.InstanceManager;
import jmri.JmriException;
import jmri.Light;
import jmri.LightManager;
import jmri.jmrit.logixng.ConditionalNG;
import jmri.jmrit.logixng.DigitalAction;
import jmri.jmrit.logixng.DigitalActionManager;
import jmri.jmrit.logixng.DigitalExpression;
import jmri.jmrit.logixng.DigitalExpressionManager;
import jmri.jmrit.logixng.LogixNG;
import jmri.jmrit.logixng.LogixNG_Manager;
import jmri.jmrit.logixng.MaleSocket;
import jmri.jmrit.logixng.SocketAlreadyConnectedException;
import jmri.jmrit.logixng.digital.actions.ActionAtomicBoolean;
import jmri.jmrit.logixng.digital.actions.IfThen;
import jmri.jmrit.logixng.enums.Is_IsNot_Enum;
import jmri.jmrit.logixng.implementation.DefaultConditionalNG;
import jmri.util.JUnitUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test ExpressionLight
 * 
 * @author Daniel Bergqvist 2018
 */
public class ExpressionLightTest {

    @Test
    public void testCtor() {
        DigitalExpression t = new ExpressionLight("IQA55:E321", null);
        Assert.assertNotNull("exists",t);
    }
    
    @Test
    public void testDescription() {
        ExpressionLight expressionLight = new ExpressionLight("IQA55:E321");
        Assert.assertTrue("Get light".equals(expressionLight.getShortDescription()));
        Assert.assertTrue("Light Not selected is On".equals(expressionLight.getLongDescription()));
        Light light = InstanceManager.getDefault(LightManager.class).provide("IL1");
        expressionLight.setLight(light);
        expressionLight.set_Is_IsNot(Is_IsNot_Enum.IS);
        expressionLight.setLightState(ExpressionLight.LightState.OFF);
        Assert.assertTrue("Light IL1 is Off".equals(expressionLight.getLongDescription()));
        expressionLight.set_Is_IsNot(Is_IsNot_Enum.IS_NOT);
        Assert.assertTrue("Light IL1 is not Off".equals(expressionLight.getLongDescription()));
        expressionLight.setLightState(ExpressionLight.LightState.OTHER);
        Assert.assertTrue("Light IL1 is not Other".equals(expressionLight.getLongDescription()));
    }
    
    @Test
    public void testExpression() throws SocketAlreadyConnectedException, JmriException {
        Light light = InstanceManager.getDefault(LightManager.class).provide("IL1");
        light.setCommandedState(Light.OFF);
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        LogixNG logixNG = InstanceManager.getDefault(LogixNG_Manager.class).createLogixNG("A logixNG");
        ConditionalNG conditionalNG = new DefaultConditionalNG(logixNG.getSystemName()+":1");
        logixNG.addConditionalNG(conditionalNG);
        logixNG.activateLogixNG();
        
        DigitalAction actionIfThen = new IfThen(conditionalNG, IfThen.Type.TRIGGER_ACTION);
        MaleSocket socketIfThen = InstanceManager.getDefault(DigitalActionManager.class).registerAction(actionIfThen);
        conditionalNG.getChild(0).connect(socketIfThen);
        
        ExpressionLight expressionLight = new ExpressionLight(conditionalNG);
        expressionLight.setLight(light);
        expressionLight.set_Is_IsNot(Is_IsNot_Enum.IS);
        expressionLight.setLightState(ExpressionLight.LightState.ON);
        MaleSocket socketLight = InstanceManager.getDefault(DigitalExpressionManager.class).registerExpression(expressionLight);
        socketIfThen.getChild(0).connect(socketLight);
        
        DigitalAction actionAtomicBoolean = new ActionAtomicBoolean(conditionalNG, atomicBoolean, true);
        MaleSocket socketAtomicBoolean = InstanceManager.getDefault(DigitalActionManager.class).registerAction(actionAtomicBoolean);
        socketIfThen.getChild(1).connect(socketAtomicBoolean);
        
        // The action is not yet executed so the atomic boolean should be false
        Assert.assertFalse("atomicBoolean is false",atomicBoolean.get());
        // Throw the switch. This should not execute the conditional.
        light.setCommandedState(Light.ON);
        // The conditionalNG is not yet enabled so it shouldn't be executed.
        // So the atomic boolean should be false
        Assert.assertFalse("atomicBoolean is false",atomicBoolean.get());
        // Close the switch. This should not execute the conditional.
        light.setCommandedState(Light.OFF);
        // Enable the conditionalNG and all its children.
        conditionalNG.setEnabled(true);
        // The action is not yet executed so the atomic boolean should be false
        Assert.assertFalse("atomicBoolean is false",atomicBoolean.get());
        // Throw the switch. This should execute the conditional.
        light.setCommandedState(Light.ON);
        // The action should now be executed so the atomic boolean should be true
        Assert.assertTrue("atomicBoolean is true",atomicBoolean.get());
    }
    
    // The minimal setup for log4J
    @Before
    public void setUp() {
        JUnitUtil.setUp();
        JUnitUtil.resetInstanceManager();
        JUnitUtil.initInternalSensorManager();
        JUnitUtil.initInternalLightManager();
    }

    @After
    public void tearDown() {
        JUnitUtil.tearDown();
    }
    
}
