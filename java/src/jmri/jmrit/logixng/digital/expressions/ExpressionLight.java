package jmri.jmrit.logixng.digital.expressions;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.annotation.CheckForNull;
import jmri.InstanceManager;
import jmri.NamedBeanHandle;
import jmri.NamedBeanHandleManager;
import jmri.Light;
import jmri.LightManager;
import jmri.jmrit.logixng.Base;
import jmri.jmrit.logixng.Category;
import jmri.jmrit.logixng.ConditionalNG;
import jmri.jmrit.logixng.DigitalExpressionManager;
import jmri.jmrit.logixng.FemaleSocket;
import jmri.jmrit.logixng.Is_IsNot_Enum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Evaluates the state of a Light.
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public class ExpressionLight extends AbstractDigitalExpression implements PropertyChangeListener {

    private ExpressionLight _template;
    private String _lightName;
    private NamedBeanHandle<Light> _lightHandle;
    private Is_IsNot_Enum _is_IsNot = Is_IsNot_Enum.IS;
    private LightState _lightState = LightState.ON;
    private boolean _listenersAreRegistered = false;

    public ExpressionLight()
            throws BadUserNameException {
        super(InstanceManager.getDefault(DigitalExpressionManager.class).getNewSystemName());
    }

    public ExpressionLight(String sys)
            throws BadUserNameException, BadSystemNameException {
        super(sys);
    }

    public ExpressionLight(String sys, String user)
            throws BadUserNameException, BadSystemNameException {
        super(sys, user);
    }
    
    private ExpressionLight(ExpressionLight template, String sys) {
        super(sys);
        _template = template;
        if (_template == null) throw new NullPointerException();    // Temporary solution to make variable used.
    }
    
    /** {@inheritDoc} */
    @Override
    public Base getNewObjectBasedOnTemplate(String sys) {
        return new ExpressionLight(this, sys);
    }
    
    public void setLight(NamedBeanHandle<Light> handle) {
        _lightHandle = handle;
    }
    
    public void setLight(@CheckForNull Light light) {
        if (light != null) {
            _lightHandle = InstanceManager.getDefault(NamedBeanHandleManager.class)
                    .getNamedBeanHandle(light.getDisplayName(), light);
        } else {
            _lightHandle = null;
        }
    }
    
    public NamedBeanHandle<Light> getLight() {
        return _lightHandle;
    }
    
    public void set_Is_IsNot(Is_IsNot_Enum is_IsNot) {
        _is_IsNot = is_IsNot;
    }
    
    public Is_IsNot_Enum get_Is_IsNot() {
        return _is_IsNot;
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
    public boolean evaluate() {
        LightState currentLightState = LightState.get(_lightHandle.getBean().getCommandedState());
        if (_is_IsNot == Is_IsNot_Enum.IS) {
            return currentLightState == _lightState;
        } else {
            return currentLightState != _lightState;
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
        return Bundle.getMessage("Light_Long", lightName, _is_IsNot.toString(), _lightState._text);
    }
    
    public void setLightName(String lightSystemName) {
        _lightName = lightSystemName;
    }
    
    /** {@inheritDoc} */
    @Override
    public void setup() {
        if (_listenersAreRegistered) {
            RuntimeException e = new RuntimeException("setup must not be called when listeners are registered");
            log.error("setup must not be called when listeners are registered", e);
            throw e;
        }
        
        // Don't setup again if we already has the correct light
        if ((_lightHandle != null) && (_lightHandle.getName().equals(_lightName))) {
            return;
        }
        
        // Remove the old _lightHandle if it exists
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
        if (!_listenersAreRegistered && (_lightHandle != null)) {
            _lightHandle.getBean().addPropertyChangeListener("KnownState", this);
            _listenersAreRegistered = true;
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public void unregisterListenersForThisClass() {
        if (_listenersAreRegistered) {
            _lightHandle.getBean().removePropertyChangeListener("KnownState", this);
            _listenersAreRegistered = false;
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        getConditionalNG().execute();
    }
    
    /** {@inheritDoc} */
    @Override
    public void disposeMe() {
    }
    
    
    
    public enum LightState {
        OFF(Light.OFF, Bundle.getMessage("StateOff")),
        ON(Light.ON, Bundle.getMessage("StateOn")),
        OTHER(-1, Bundle.getMessage("LightOtherStatus"));
        
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
    
    
    private final static Logger log = LoggerFactory.getLogger(ExpressionLight.class);
    
}
