package jmri.jmrit.logixng.digital.actions;

import jmri.InstanceManager;
import jmri.NamedBeanHandle;
import jmri.NamedBeanHandleManager;
import jmri.Sensor;
import jmri.SensorManager;
import jmri.jmrit.logixng.Base;
import jmri.jmrit.logixng.Category;
import jmri.jmrit.logixng.ConditionalNG;
import jmri.jmrit.logixng.DigitalActionManager;
import jmri.jmrit.logixng.FemaleSocket;
import jmri.util.ThreadingUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This action sets the state of a sensor.
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public class ActionSensor extends AbstractDigitalAction {

    private ActionSensor _template;
    private String _sensorName;
    private NamedBeanHandle<Sensor> _sensorHandle;
    private SensorState _sensorState = SensorState.ACTIVE;
    
    public ActionSensor(ConditionalNG conditionalNG)
            throws BadUserNameException {
        super(InstanceManager.getDefault(DigitalActionManager.class).getNewSystemName(conditionalNG));
    }

    public ActionSensor(String sys)
            throws BadUserNameException {
        super(sys);
//        jmri.InstanceManager.sensorManagerInstance().addVetoableChangeListener(this);
    }

    public ActionSensor(String sys, String user)
            throws BadUserNameException, BadSystemNameException {
        super(sys, user);
//        jmri.InstanceManager.sensorManagerInstance().addVetoableChangeListener(this);
    }
    
    private ActionSensor(ActionSensor template, String sys) {
        super(sys);
        _template = template;
        if (_template == null) throw new NullPointerException();    // Temporary solution to make variable used.
    }
    
    /** {@inheritDoc} */
    @Override
    public Base getNewObjectBasedOnTemplate(String sys) {
        return new ActionSensor(this, sys);
    }
    
    public void setSensor(NamedBeanHandle<Sensor> handle) {
        _sensorHandle = handle;
    }
    
    public void setSensor(Sensor sensor) {
        _sensorHandle = InstanceManager.getDefault(NamedBeanHandleManager.class)
                .getNamedBeanHandle(sensor.getDisplayName(), sensor);
    }
    
    public NamedBeanHandle<Sensor> getSensor() {
        return _sensorHandle;
    }
    
    public void setSensorState(SensorState state) {
        _sensorState = state;
    }
    
    public SensorState getSensorState() {
        return _sensorState;
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
    public boolean executeStart() {
        final Sensor t = _sensorHandle.getBean();
//        final int newState = _sensorState.getID();
        ThreadingUtil.runOnLayout(() -> {
            if (_sensorState == SensorState.TOGGLE) {
//                t.setCommandedState(newState);
                if (t.getCommandedState() == Sensor.INACTIVE) {
                    t.setCommandedState(Sensor.ACTIVE);
                } else {
                    t.setCommandedState(Sensor.INACTIVE);
                }
            } else {
                t.setCommandedState(_sensorState.getID());
            }
        });
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public boolean executeContinue() {
        // The executeStart() metod never return True from this action and
        // therefore executeContinue() should never be called.
        throw new RuntimeException("Sensor don't support executeContinue()");
    }

    /** {@inheritDoc} */
    @Override
    public boolean executeRestart() {
        // The executeStart() metod never return True from this action and
        // therefore executeRestart() should never be called.
        throw new RuntimeException("Sensor don't support executeRestart()");
    }

    /** {@inheritDoc} */
    @Override
    public void abort() {
        // The executeStart() metod never return True from this action and
        // therefore abort() should never be called.
        throw new RuntimeException("Sensor don't support abort()");
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
        String sensorName;
        if (_sensorHandle != null) {
            sensorName = _sensorHandle.getBean().getDisplayName();
        } else {
            sensorName = Bundle.getMessage("BeanNotSelected");
        }
        return Bundle.getMessage("Sensor_Long", sensorName, _sensorState._text);
    }
    
    public void setSensorName(String sensorSystemName) {
        _sensorName = sensorSystemName;
    }
    
    /** {@inheritDoc} */
    @Override
    public void setup() {
        // Don't setup again if we already has the correct sensor
        if ((_sensorHandle != null) && (_sensorHandle.getName().equals(_sensorName))) {
            return;
        }
        
        // Remove the old _turnoutHandle if it exists
        _sensorHandle = null;
        
        if (_sensorName != null) {
            Sensor t = InstanceManager.getDefault(SensorManager.class).getSensor(_sensorName);
            if (t != null) {
                _sensorHandle = InstanceManager.getDefault(jmri.NamedBeanHandleManager.class).getNamedBeanHandle(_sensorName, t);
            } else {
                log.error("Sensor {} does not exists", _sensorName);
            }
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public void registerListenersForThisClass() {
    }
    
    /** {@inheritDoc} */
    @Override
    public void unregisterListenersForThisClass() {
    }
    
    /** {@inheritDoc} */
    @Override
    public void disposeMe() {
    }
    
    
    
    public enum SensorState {
        INACTIVE(Sensor.INACTIVE, Bundle.getMessage("SensorStateInactive")),
        ACTIVE(Sensor.ACTIVE, Bundle.getMessage("SensorStateActive")),
        TOGGLE(-1, Bundle.getMessage("SensorToggleStatus"));
        
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
                    throw new IllegalArgumentException("invalid sensor state");
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
    
    
    private final static Logger log = LoggerFactory.getLogger(ActionSensor.class);
    
}
