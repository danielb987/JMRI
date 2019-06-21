package jmri.jmrit.logixng.digital.expressions;

import java.util.concurrent.atomic.AtomicBoolean;
import jmri.InstanceManager;
import jmri.NamedBeanHandle;
import jmri.NamedBeanHandleManager;
import jmri.Sensor;
import jmri.SensorManager;
import jmri.jmrit.logixng.Base;
import jmri.jmrit.logixng.Category;
import jmri.jmrit.logixng.FemaleSocket;
import jmri.jmrit.logixng.enums.Is_IsNot_Enum;

    // For this expression to work, the LogixNG engine needs to notify those
    // who wants to be notified when all LogixNGs are finished running.
    
    // Enum Trigger: ON_TRUE, ON_FALSE, ON_CHANGE
    // Put this enum in Expression since it may be used by many expressions

/**
 * An expression that buffers the changes of a sensor.
 * 
 * A sensor, like an optical sensor looking at wheel axles or a button switch
 * may be "on" or "off" a very short time and in some cases that time might be
 * too short. This expression remembers the new state of a sensor and triggers
 * the LogixNGs again.
 * 
 * Each time a change of the sensor is notified which this expression should
 * trigger on, this expression notifies the LogixNGManager that it should
 * run all LogixNGs again. If LogixNGManager is already running the LogixNGs,
 * the expression waits to notify the LogixNGManager until it's done.
 * 
 * The expression remembers the state of the sensor until the LogixNGs has been
 * run.
 * 
 * @author Daniel Bergqvist 2018
 */
public class ExpressionBufferedSensor extends AbstractDigitalExpression {

    private ExpressionBufferedSensor _template;
    private String _sensorSystemName;
    private NamedBeanHandle<Sensor> _sensorHandle;
    private Is_IsNot_Enum _is_IsNot = Is_IsNot_Enum.IS;
    private ExpressionSensor.SensorState _sensorState = ExpressionSensor.SensorState.ACTIVE;
    
    public ExpressionBufferedSensor(String sys) throws BadSystemNameException {
        super(sys);
//        sensor = null;
    }

    public ExpressionBufferedSensor(String sys, String user)
            throws BadUserNameException, BadSystemNameException {
        super(sys, user);
//        sensor = null;
    }

    private ExpressionBufferedSensor(ExpressionBufferedSensor template, String sys) {
        super(sys);
        _template = template;
        if (_template == null) throw new NullPointerException();    // Temporary solution to make variable used.
    }
    
    public void setSensor(NamedBeanHandle<Sensor> handle) {
        _sensorHandle = handle;
    }
    
    public void setSensor(Sensor sensor) {
        _sensorHandle = InstanceManager.getDefault(NamedBeanHandleManager.class)
                .getNamedBeanHandle(sensor.getDisplayName(), sensor);
    }
    
    public NamedBeanHandle getSensor() {
        return _sensorHandle;
    }
    
    public void set_Is_IsNot(Is_IsNot_Enum is_IsNot) {
        _is_IsNot = is_IsNot;
    }
    
    public Is_IsNot_Enum get_Is_IsNot() {
        return _is_IsNot;
    }
    
    public void setSensorState(ExpressionSensor.SensorState state) {
        _sensorState = state;
    }
    
    public ExpressionSensor.SensorState getSensorState() {
        return _sensorState;
    }

    /** {@inheritDoc} */
    @Override
    public Base getNewObjectBasedOnTemplate(String sys) {
        return new ExpressionBufferedSensor(this, sys);
    }
    
    /** {@inheritDoc} */
    @Override
    public Category getCategory() {
        return Category.ITEM;
    }

    @Override
    public boolean isExternal() {
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public void initEvaluation() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public boolean evaluate(AtomicBoolean isCompleted) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void reset() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public FemaleSocket getChild(int index) throws IllegalArgumentException, UnsupportedOperationException {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public int getChildCount() {
        return 0;
    }

    @Override
    public String getShortDescription() {
        return Bundle.getMessage("BufferedSensor_Short");
    }
    
    @Override
    public String getLongDescription() {
        return Bundle.getMessage("BufferedSensor_Long");
    }
    
    public void setSensor_SystemName(String sensorSystemName) {
        _sensorSystemName = sensorSystemName;
    }
    
    /** {@inheritDoc} */
    @Override
    public void setup() {
        if ((_sensorHandle == null) && (_sensorSystemName != null)) {
            Sensor t = InstanceManager.getDefault(SensorManager.class).getBeanBySystemName(_sensorSystemName);
            _sensorHandle = InstanceManager.getDefault(jmri.NamedBeanHandleManager.class).getNamedBeanHandle(_sensorSystemName, t);
        }
    }
    
}
