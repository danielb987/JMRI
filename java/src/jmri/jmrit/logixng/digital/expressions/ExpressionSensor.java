package jmri.jmrit.logixng.digital.expressions;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.concurrent.atomic.AtomicBoolean;
import jmri.InstanceManager;
import jmri.NamedBeanHandle;
import jmri.NamedBeanHandleManager;
import jmri.Sensor;
import jmri.SensorManager;
import jmri.jmrit.logixng.Base;
import jmri.jmrit.logixng.Category;
import jmri.jmrit.logixng.ConditionalNG;
import jmri.jmrit.logixng.DigitalExpressionManager;
import jmri.jmrit.logixng.FemaleSocket;
import jmri.jmrit.logixng.enums.Is_IsNot_Enum;

/**
 * Evaluates the state of a Sensor.
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public class ExpressionSensor extends AbstractDigitalExpression implements PropertyChangeListener {

    private ExpressionSensor _template;
    private String _lightSystemName;
    private NamedBeanHandle<Sensor> _sensorHandle;
    private Is_IsNot_Enum _is_IsNot = Is_IsNot_Enum.IS;
    private SensorState _lightState = SensorState.ACTIVE;
    private boolean _listenersAreRegistered = false;

    public ExpressionSensor(ConditionalNG conditionalNG)
            throws BadUserNameException {
        super(InstanceManager.getDefault(DigitalExpressionManager.class).getNewSystemName(conditionalNG));
    }

    public ExpressionSensor(String sys)
            throws BadUserNameException, BadSystemNameException {
        super(sys);
    }

    public ExpressionSensor(String sys, String user)
            throws BadUserNameException, BadSystemNameException {
        super(sys, user);
    }
    
    private ExpressionSensor(ExpressionSensor template, String sys) {
        super(sys);
        _template = template;
        if (_template == null) throw new NullPointerException();    // Temporary solution to make variable used.
    }
    
    /** {@inheritDoc} */
    @Override
    public Base getNewObjectBasedOnTemplate(String sys) {
        return new ExpressionSensor(this, sys);
    }
    
    public void setSensor(NamedBeanHandle<Sensor> handle) {
        _sensorHandle = handle;
    }
    
    public void setSensor(Sensor light) {
        _sensorHandle = InstanceManager.getDefault(NamedBeanHandleManager.class)
                .getNamedBeanHandle(light.getDisplayName(), light);
    }
    
    public NamedBeanHandle<Sensor> getSensor() {
        return _sensorHandle;
    }
    
    public void set_Is_IsNot(Is_IsNot_Enum is_IsNot) {
        _is_IsNot = is_IsNot;
    }
    
    public Is_IsNot_Enum get_Is_IsNot() {
        return _is_IsNot;
    }
    
    public void setSensorState(SensorState state) {
        _lightState = state;
    }
    
    public SensorState getSensorState() {
        return _lightState;
    }

    /** {@inheritDoc} */
    @Override
    public Category getCategory() {
        return Category.ITEM;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isExternal() {
        return true;
    }
    
    /** {@inheritDoc} */
    @Override
    public void initEvaluation() {
        // Do nothing
    }
    
    /** {@inheritDoc} */
    @Override
    public boolean evaluate(AtomicBoolean isCompleted) {
        SensorState currentSensorState = SensorState.get(_sensorHandle.getBean().getCommandedState());
        if (_is_IsNot == Is_IsNot_Enum.IS) {
            return currentSensorState == _lightState;
        } else {
            return currentSensorState != _lightState;
        }
    }

    /** {@inheritDoc} */
    @Override
    public void reset() {
        // Do nothing.
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
        return Bundle.getMessage("Sensor_Short");
    }

    @Override
    public String getLongDescription() {
        String lightName;
        if (_sensorHandle != null) {
            lightName = _sensorHandle.getBean().getDisplayName();
        } else {
            lightName = Bundle.getMessage("BeanNotSelected");
        }
        return Bundle.getMessage("Sensor_Long", lightName, _is_IsNot.toString(), _lightState._text);
    }
    
    public void setSensor_SystemName(String lightSystemName) {
        _lightSystemName = lightSystemName;
    }
    
    /** {@inheritDoc} */
    @Override
    public void setup() {
        if ((_sensorHandle == null) && (_lightSystemName != null)) {
            Sensor t = InstanceManager.getDefault(SensorManager.class).getBeanBySystemName(_lightSystemName);
            _sensorHandle = InstanceManager.getDefault(jmri.NamedBeanHandleManager.class).getNamedBeanHandle(_lightSystemName, t);
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public void registerListeners() {
        if (! _listenersAreRegistered) {
            _sensorHandle.getBean().addPropertyChangeListener("KnownState", this);
            _listenersAreRegistered = true;
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public void unregisterListeners() {
        if (! _listenersAreRegistered) {
            _sensorHandle.getBean().removePropertyChangeListener("KnownState", this);
            _listenersAreRegistered = false;
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        getConditionalNG().execute();
    }
    
    
    
    public enum SensorState {
        INACTIVE(Sensor.INACTIVE, Bundle.getMessage("SensorStateInactive")),
        ACTIVE(Sensor.ACTIVE, Bundle.getMessage("SensorStateActive")),
        OTHER(-1, Bundle.getMessage("SensorOtherStatus"));
        
        private final int _id;
        private final String _text;
        
        private SensorState(int id, String text) {
            this._id = id;
            this._text = text;
        }
        
        static public SensorState get(int id) {
            switch (id) {
                case Sensor.INACTIVE:
                    return INACTIVE;
                    
                case Sensor.ACTIVE:
                    return ACTIVE;
                    
                default:
                    return OTHER;
            }
        }
        
        public int getID() {
            return _id;
        }
        
        @Override
        public String toString() {
            return _text;
        }
        
    }
    
}
