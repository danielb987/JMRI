package jmri.jmrit.logixng.digital.expressions;

import java.util.concurrent.atomic.AtomicBoolean;
import jmri.InstanceManager;
import jmri.JmriException;
import jmri.Sensor;
import jmri.SensorManager;
import jmri.jmrit.logixng.ConditionalNG;
import jmri.jmrit.logixng.DigitalActionManager;
import jmri.jmrit.logixng.DigitalExpressionManager;
import jmri.jmrit.logixng.LogixNG;
import jmri.jmrit.logixng.LogixNG_Manager;
import jmri.jmrit.logixng.MaleSocket;
import jmri.jmrit.logixng.SocketAlreadyConnectedException;
import jmri.jmrit.logixng.digital.actions.ActionAtomicBoolean;
import jmri.jmrit.logixng.digital.actions.IfThen;
import jmri.jmrit.logixng.Is_IsNot_Enum;
import jmri.jmrit.logixng.implementation.DefaultConditionalNG;
import jmri.util.JUnitUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import jmri.jmrit.logixng.DigitalExpressionBean;
import jmri.jmrit.logixng.DigitalActionBean;

/**
 * Test ExpressionSensor
 * 
 * @author Daniel Bergqvist 2018
 */
public class ExpressionSensorTest {

    @Test
    public void testCtor() {
        DigitalExpressionBean t = new ExpressionSensor("IQA55:1:DE321", null);
        Assert.assertNotNull("exists",t);
    }
    
    @Test
    public void testDescription() {
        ExpressionSensor expressionSensor = new ExpressionSensor("IQA55:1:DE321");
        Assert.assertTrue("Get sensor".equals(expressionSensor.getShortDescription()));
        Assert.assertTrue("Sensor Not selected is Active".equals(expressionSensor.getLongDescription()));
        Sensor sensor = InstanceManager.getDefault(SensorManager.class).provide("IS1");
        expressionSensor.setSensor(sensor);
        expressionSensor.set_Is_IsNot(Is_IsNot_Enum.IS);
        expressionSensor.setSensorState(ExpressionSensor.SensorState.INACTIVE);
        Assert.assertTrue("Sensor IS1 is Inactive".equals(expressionSensor.getLongDescription()));
        expressionSensor.set_Is_IsNot(Is_IsNot_Enum.IS_NOT);
        Assert.assertTrue("Sensor IS1 is not Inactive".equals(expressionSensor.getLongDescription()));
        expressionSensor.setSensorState(ExpressionSensor.SensorState.OTHER);
        Assert.assertTrue("Sensor IS1 is not Other".equals(expressionSensor.getLongDescription()));
    }
    
    @Test
    public void testExpression() throws SocketAlreadyConnectedException, JmriException {
        Sensor sensor = InstanceManager.getDefault(SensorManager.class).provide("IS1");
        sensor.setCommandedState(Sensor.INACTIVE);
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        LogixNG logixNG = InstanceManager.getDefault(LogixNG_Manager.class).createLogixNG("A logixNG");
        ConditionalNG conditionalNG = new DefaultConditionalNG(logixNG.getSystemName()+":1");
        logixNG.addConditionalNG(conditionalNG);
        logixNG.activateLogixNG();
        
        DigitalActionBean actionIfThen = new IfThen(conditionalNG, IfThen.Type.TRIGGER_ACTION);
        MaleSocket socketIfThen = InstanceManager.getDefault(DigitalActionManager.class).registerAction(actionIfThen);
        conditionalNG.getChild(0).connect(socketIfThen);
        
        ExpressionSensor expressionSensor = new ExpressionSensor(conditionalNG);
        expressionSensor.setSensor(sensor);
        expressionSensor.set_Is_IsNot(Is_IsNot_Enum.IS);
        expressionSensor.setSensorState(ExpressionSensor.SensorState.ACTIVE);
        MaleSocket socketSensor = InstanceManager.getDefault(DigitalExpressionManager.class).registerExpression(expressionSensor);
        socketIfThen.getChild(0).connect(socketSensor);
        
        DigitalActionBean actionAtomicBoolean = new ActionAtomicBoolean(conditionalNG, atomicBoolean, true);
        MaleSocket socketAtomicBoolean = InstanceManager.getDefault(DigitalActionManager.class).registerAction(actionAtomicBoolean);
        socketIfThen.getChild(1).connect(socketAtomicBoolean);
        
        // The action is not yet executed so the atomic boolean should be false
        Assert.assertFalse("atomicBoolean is false",atomicBoolean.get());
        // Throw the switch. This should not execute the conditional.
        sensor.setCommandedState(Sensor.ACTIVE);
        // The conditionalNG is not yet enabled so it shouldn't be executed.
        // So the atomic boolean should be false
        Assert.assertFalse("atomicBoolean is false",atomicBoolean.get());
        // Close the switch. This should not execute the conditional.
        sensor.setCommandedState(Sensor.INACTIVE);
        // Enable the conditionalNG and all its children.
        conditionalNG.setEnabled(true);
        // The action is not yet executed so the atomic boolean should be false
        Assert.assertFalse("atomicBoolean is false",atomicBoolean.get());
        // Throw the switch. This should execute the conditional.
        sensor.setCommandedState(Sensor.ACTIVE);
        // The action should now be executed so the atomic boolean should be true
        Assert.assertTrue("atomicBoolean is true",atomicBoolean.get());
    }
    
    // The minimal setup for log4J
    @Before
    public void setUp() {
        JUnitUtil.setUp();
        JUnitUtil.resetInstanceManager();
        JUnitUtil.initInternalSensorManager();
        JUnitUtil.initInternalSensorManager();
    }

    @After
    public void tearDown() {
        JUnitUtil.tearDown();
    }
    
}
