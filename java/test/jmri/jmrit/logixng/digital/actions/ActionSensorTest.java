package jmri.jmrit.logixng.digital.actions;

import jmri.InstanceManager;
import jmri.JmriException;
import jmri.Sensor;
import jmri.SensorManager;
import jmri.jmrit.logixng.ConditionalNG;
import jmri.jmrit.logixng.DigitalActionManager;
import jmri.jmrit.logixng.LogixNG;
import jmri.jmrit.logixng.LogixNG_Manager;
import jmri.jmrit.logixng.MaleSocket;
import jmri.jmrit.logixng.SocketAlreadyConnectedException;
import jmri.jmrit.logixng.implementation.DefaultConditionalNG;
import jmri.util.JUnitUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test ActionSensor
 * 
 * @author Daniel Bergqvist 2018
 */
public class ActionSensorTest extends AbstractDigitalActionTestBase {

    @Test
    public void testCtor() {
        ActionSensor t = new ActionSensor("IQA55:10:DA321", null);
        Assert.assertNotNull("exists",t);
    }
    
    @Test
    public void testAction() throws SocketAlreadyConnectedException, JmriException {
        Sensor sensor = InstanceManager.getDefault(SensorManager.class).provide("IT1");
        sensor.setCommandedState(Sensor.INACTIVE);
        LogixNG logixNG = InstanceManager.getDefault(LogixNG_Manager.class).createLogixNG("A logixNG");
        ConditionalNG conditionalNG = new DefaultConditionalNG(logixNG.getSystemName()+":1");
        logixNG.addConditionalNG(conditionalNG);
        conditionalNG.setEnabled(true);
        ActionSensor actionSensor = new ActionSensor(conditionalNG);
        actionSensor.setSensor(sensor);
        actionSensor.setSensorState(ActionSensor.SensorState.ACTIVE);
        MaleSocket socket = InstanceManager.getDefault(DigitalActionManager.class).registerAction(actionSensor);
        conditionalNG.getChild(0).connect(socket);
        // The action is not yet executed so the sensor should be closed
        Assert.assertTrue("sensor is closed",sensor.getCommandedState() == Sensor.INACTIVE);
        // Execute the conditional
        conditionalNG.execute();
        // The action should now be executed so the sensor should be thrown
        Assert.assertTrue("sensor is thrown",sensor.getCommandedState() == Sensor.ACTIVE);
        
        // Test to set sensor to closed
        actionSensor.setSensorState(ActionSensor.SensorState.INACTIVE);
        // Execute the conditional
        conditionalNG.execute();
        // The action should now be executed so the sensor should be thrown
        Assert.assertTrue("sensor is thrown",sensor.getCommandedState() == Sensor.INACTIVE);
        
        // Test to set sensor to toggle
        actionSensor.setSensorState(ActionSensor.SensorState.TOGGLE);
        // Execute the conditional
        conditionalNG.execute();
        // The action should now be executed so the sensor should be thrown
        Assert.assertTrue("sensor is thrown",sensor.getCommandedState() == Sensor.ACTIVE);
        
        // Test to set sensor to toggle
        actionSensor.setSensorState(ActionSensor.SensorState.TOGGLE);
        // Execute the conditional
        conditionalNG.execute();
        // The action should now be executed so the sensor should be thrown
        Assert.assertTrue("sensor is thrown",sensor.getCommandedState() == Sensor.INACTIVE);
    }
    
    // The minimal setup for log4J
    @Before
    public void setUp() {
        JUnitUtil.setUp();
        JUnitUtil.resetInstanceManager();
        JUnitUtil.initInternalSensorManager();
        JUnitUtil.initInternalSensorManager();
        _base = new ActionSensor("IQA55:10:DA321", null);
    }

    @After
    public void tearDown() {
        JUnitUtil.tearDown();
    }
    
}
