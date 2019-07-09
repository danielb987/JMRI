package jmri.jmrit.logixng.digital.actions;

import jmri.InstanceManager;
import jmri.NamedBeanHandle;
import jmri.NamedBeanHandleManager;
import jmri.Light;
import jmri.LightManager;
import jmri.jmrit.logixng.Base;
import jmri.jmrit.logixng.Category;
import jmri.jmrit.logixng.ConditionalNG;
import jmri.jmrit.logixng.DigitalActionManager;
import jmri.jmrit.logixng.FemaleSocket;
import jmri.util.ThreadingUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This action sets the state of a light.
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public class ActionLight extends AbstractDigitalAction {

    private ActionLight _template;
    private String _lightName;
    private NamedBeanHandle<Light> _lightHandle;
    private LightState _lightState = LightState.ON;
    
    public ActionLight(ConditionalNG conditionalNG)
            throws BadUserNameException {
        super(InstanceManager.getDefault(DigitalActionManager.class).getNewSystemName(conditionalNG));
    }

    public ActionLight(String sys)
            throws BadUserNameException {
        super(sys);
//        jmri.InstanceManager.lightManagerInstance().addVetoableChangeListener(this);
    }

    public ActionLight(String sys, String user)
            throws BadUserNameException, BadSystemNameException {
        super(sys, user);
//        jmri.InstanceManager.lightManagerInstance().addVetoableChangeListener(this);
    }
    
    private ActionLight(ActionLight template, String sys) {
        super(sys);
        _template = template;
        if (_template == null) throw new NullPointerException();    // Temporary solution to make variable used.
    }
    
    /** {@inheritDoc} */
    @Override
    public Base getNewObjectBasedOnTemplate(String sys) {
        return new ActionLight(this, sys);
    }
    
    public void setLight(NamedBeanHandle<Light> handle) {
        _lightHandle = handle;
    }
    
    public void setLight(Light light) {
        _lightHandle = InstanceManager.getDefault(NamedBeanHandleManager.class)
                .getNamedBeanHandle(light.getDisplayName(), light);
    }
    
    public NamedBeanHandle<Light> getLight() {
        return _lightHandle;
    }
    
    public void setLightState(LightState state) {
        _lightState = state;
    }
    
    public LightState getLightState() {
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
    public boolean executeStart() {
        final Light t = _lightHandle.getBean();
//        final int newState = _lightState.getID();
        ThreadingUtil.runOnLayout(() -> {
            if (_lightState == LightState.TOGGLE) {
//                t.setCommandedState(newState);
                if (t.getCommandedState() == Light.OFF) {
                    t.setCommandedState(Light.ON);
                } else {
                    t.setCommandedState(Light.OFF);
                }
            } else {
                t.setCommandedState(_lightState.getID());
            }
        });
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public boolean executeContinue() {
        // The executeStart() metod never return True from this action and
        // therefore executeContinue() should never be called.
        throw new RuntimeException("Light don't support executeContinue()");
    }

    /** {@inheritDoc} */
    @Override
    public boolean executeRestart() {
        // The executeStart() metod never return True from this action and
        // therefore executeRestart() should never be called.
        throw new RuntimeException("Light don't support executeRestart()");
    }

    /** {@inheritDoc} */
    @Override
    public void abort() {
        // The executeStart() metod never return True from this action and
        // therefore abort() should never be called.
        throw new RuntimeException("Light don't support abort()");
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
        return Bundle.getMessage("Light_Short");
    }

    @Override
    public String getLongDescription() {
        String lightName;
        if (_lightHandle != null) {
            lightName = _lightHandle.getBean().getDisplayName();
        } else {
            lightName = Bundle.getMessage("BeanNotSelected");
        }
        return Bundle.getMessage("Light_Long", lightName, _lightState._text);
    }
    
    public void setLightName(String lightSystemName) {
        _lightName = lightSystemName;
    }
    
    /** {@inheritDoc} */
    @Override
    public void setup() {
        // Remove the old _turnoutHandle if it exists
        _lightHandle = null;
        
        if (_lightName != null) {
            Light t = InstanceManager.getDefault(LightManager.class).getLight(_lightName);
            if (t != null) {
                _lightHandle = InstanceManager.getDefault(jmri.NamedBeanHandleManager.class).getNamedBeanHandle(_lightName, t);
            } else {
                log.error("Light {} does not exists", _lightName);
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

    
    
    public enum LightState {
        OFF(Light.OFF, Bundle.getMessage("StateOff")),
        ON(Light.ON, Bundle.getMessage("StateOn")),
        TOGGLE(-1, Bundle.getMessage("LightToggleStatus"));
        
        private final int _id;
        private final String _text;
        
        private LightState(int id, String text) {
            this._id = id;
            this._text = text;
        }
        
        static public LightState get(int id) {
            switch (id) {
                case Light.OFF:
                    return OFF;
                    
                case Light.ON:
                    return ON;
                    
                default:
                    throw new IllegalArgumentException("invalid light state");
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
    
    
    private final static Logger log = LoggerFactory.getLogger(ActionLight.class);
    
}
