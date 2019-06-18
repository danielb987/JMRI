package jmri.jmrit.logixng.implementation;

import static jmri.NamedBean.UNKNOWN;

import java.util.ArrayList;
import java.util.HashMap;
import jmri.InstanceManager;
import jmri.JmriException;
import jmri.implementation.AbstractNamedBean;
import jmri.implementation.JmriSimplePropertyListener;
import jmri.jmrit.logixng.Base;
import jmri.jmrit.logixng.Category;
import jmri.jmrit.logixng.ConditionalNG;
import jmri.jmrit.logixng.FemaleSocket;
import jmri.jmrit.logixng.FemaleSocketListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jmri.jmrit.logixng.LogixNG;
import jmri.jmrit.logixng.DigitalActionManager;
import jmri.jmrit.logixng.FemaleDigitalActionSocket;
import jmri.jmrit.logixng.MaleDigitalActionSocket;
import jmri.jmrit.logixng.MaleSocket;
import jmri.jmrit.logixng.SocketAlreadyConnectedException;
import jmri.jmrit.logixng.analog.actions.SetAnalogIO;

/**
 * The default implementation of LogixNG.
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public final class DefaultLogixNG extends AbstractNamedBean
        implements LogixNG, FemaleSocketListener {
    
    private DefaultLogixNG _template;
    private Base _parent = null;
    private String _socketSystemName = null;
    private final FemaleDigitalActionSocket _femaleActionSocket;
    private boolean _enabled = false;
    private boolean _userEnabled = false;
    
    public DefaultLogixNG(String sys) throws BadUserNameException, BadSystemNameException  {
        super(sys);
        _femaleActionSocket = InstanceManager.getDefault(DigitalActionManager.class).createFemaleSocket(this, this, "");
    }
    
    public DefaultLogixNG(String sys, String user) throws BadUserNameException, BadSystemNameException  {
        super(sys, user);
        _femaleActionSocket = InstanceManager.getDefault(DigitalActionManager.class).createFemaleSocket(this, this, "");
    }
    
    public DefaultLogixNG(String sys, MaleDigitalActionSocket action) throws BadUserNameException, BadSystemNameException  {
        super(sys);
        _femaleActionSocket = InstanceManager.getDefault(DigitalActionManager.class).createFemaleActionSocket(this, this, "", action);
    }
    
    public DefaultLogixNG(String sys, String user, MaleDigitalActionSocket action) throws BadUserNameException, BadSystemNameException  {
        super(sys, user);
        _femaleActionSocket = InstanceManager.getDefault(DigitalActionManager.class).createFemaleActionSocket(this, this, "", action);
    }
    
    private DefaultLogixNG(DefaultLogixNG template, String sys) {
        super(sys);
        _template = template;
        _femaleActionSocket = InstanceManager.getDefault(DigitalActionManager.class)
                .createFemaleSocket(this, this, _template._femaleActionSocket.getName());
    }
    
    /** {@inheritDoc} */
    @Override
    public Base getNewObjectBasedOnTemplate(String sys) {
        return new DefaultLogixNG(this, sys);
    }
    
    @Override
    public Base getParent() {
        return _parent;
    }
    
    @Override
    public void setParent(Base parent) {
        _parent = parent;
    }
    
    @Override
    public String getBeanType() {
        return Bundle.getMessage("BeanNameLogixNG");
    }

    @Override
    public void setState(int s) throws JmriException {
        log.warn("Unexpected call to setState in DefaultLogixNG.");  // NOI18N
    }

    @Override
    public int getState() {
        log.warn("Unexpected call to getState in DefaultLogixNG.");  // NOI18N
        return UNKNOWN;
    }

    /*.*
     * Set enabled status. Enabled is a bound property All conditionals are set
     * to UNKNOWN state and recalculated when the Logix is enabled, provided the
     * Logix has been previously activated.
     *./
    @Override
    public void setEnabled(boolean state) {

        boolean old = _enabled;
        _enabled = state;
        if (old != state) {
/*            
            boolean active = _isActivated;
            deActivateLogix();
            activateLogix();
            _isActivated = active;
            for (int i = _listeners.size() - 1; i >= 0; i--) {
                _listeners.get(i).setEnabled(state);
            }
            firePropertyChange("Enabled", Boolean.valueOf(old), Boolean.valueOf(state));  // NOI18N
*/            
//        }
//    }

    /*.*
     * Get enabled status
     */
//    @Override
//    public boolean getEnabled() {
//        return _enabled;
//    }

    private final static Logger log = LoggerFactory.getLogger(DefaultLogixNG.class);

    @Override
    public void connected(FemaleSocket socket) {
        // Do nothing
    }

    @Override
    public void disconnected(FemaleSocket socket) {
        // Do nothing
    }

    @Override
    public String getShortDescription() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public String getLongDescription() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public FemaleSocket getChild(int index) throws IllegalArgumentException, UnsupportedOperationException {
        if (index != 0) {
            throw new IllegalArgumentException(
                    String.format("index has invalid value: %d", index));
        }
        
        return _femaleActionSocket;
    }

    @Override
    public int getChildCount() {
        return 1;
    }

    @Override
    public Category getCategory() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public boolean isExternal() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public Lock getLock() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public void setLock(Lock lock) {
        throw new UnsupportedOperationException("Not supported.");
    }

    public void setSocketSystemName(String systemName) {
        _socketSystemName = systemName;
    }

    /** {@inheritDoc} */
    @Override
    final public void setup() {
        if ((! _femaleActionSocket.isConnected()) && (_socketSystemName != null)) {
            try {
                MaleSocket maleSocket = InstanceManager.getDefault(DigitalActionManager.class).getBeanBySystemName(_socketSystemName);
                if (maleSocket != null) {
                    _femaleActionSocket.connect(maleSocket);
                    maleSocket.setup();
                } else {
                    log.error("digital action is not found: " + _socketSystemName);
                }
            } catch (SocketAlreadyConnectedException ex) {
                // This shouldn't happen and is a runtime error if it does.
                throw new RuntimeException("socket is already connected");
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    final public void dispose() {
        _femaleActionSocket.dispose();
    }
    
    /** {@inheritDoc} */
    @Override
    public void setEnabled(boolean enable) {
        _enabled = enable;
        if (enable) {
            registerListeners();
        } else {
            unregisterListeners();
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public boolean isEnabled() {
        return _enabled && _userEnabled;
//        return _enabled && _userEnabled && _parent.isEnabled();
    }
    
    /**
     * Set whenether this object is enabled or disabled by the user.
     * 
     * @param enable true if this object should be enabled, false otherwise
     */
    public void setUserEnabled(boolean enable) {
        _userEnabled = enable;
    }
    
    /**
     * Determines whether this object is enabled by the user.
     * 
     * @return true if the object is enabled, false otherwise
     */
    public boolean isUserEnabled() {
        return _userEnabled;
    }
    
    
    /**
     * Persistant instance variables (saved between runs)
     */
    ArrayList<String> _conditionalNGSystemNames = new ArrayList<>();
    ArrayList<JmriSimplePropertyListener> _listeners = new ArrayList<>();
    
    /**
     * Maintain a list of conditional objects.  The key is the conditional system name
     */
    HashMap<String, ConditionalNG> _conditionalNGMap = new HashMap<>();
    
    /**
     * Operational instance variables (not saved between runs)
     */
    private boolean _isActivated = false;
    
    /** {@inheritDoc} */
    @Override
    public int getNumConditionalNGs() {
        return _conditionalNGSystemNames.size();
    }

    /** {@inheritDoc} */
    @Override
    public void swapConditionalNG(int nextInOrder, int row) {
        if (row <= nextInOrder) {
            return;
        }
        String temp = _conditionalNGSystemNames.get(row);
        for (int i = row; i > nextInOrder; i--) {
            _conditionalNGSystemNames.set(i, _conditionalNGSystemNames.get(i - 1));
        }
        _conditionalNGSystemNames.set(nextInOrder, temp);
    }

    /** {@inheritDoc} */
    @Override
    public ConditionalNG getConditionalNG(int order) {
        try {
            return getConditionalNG(_conditionalNGSystemNames.get(order));
        } catch (java.lang.IndexOutOfBoundsException ioob) {
            return null;
        }
    }

    /** {@inheritDoc} */
    @Override
    public String getConditionalNGByNumberOrder(int order) {
        try {
            return _conditionalNGSystemNames.get(order);
        } catch (java.lang.IndexOutOfBoundsException ioob) {
            return null;
        }
    }

    /** {@inheritDoc} */
    @Override
    public boolean addConditionalNG(ConditionalNG conditionalNG) {
        ConditionalNG chkDuplicate = _conditionalNGMap.putIfAbsent(conditionalNG.getSystemName(), conditionalNG);
        if (chkDuplicate == null) {
            return (true);
        }
        log.error("ConditionalNG '{}' has already been added to LogixNG '{}'", conditionalNG.getSystemName(), getSystemName());  // NOI18N
        return (false);
    }

    /** {@inheritDoc} */
    @Override
    public ConditionalNG getConditionalNG(String systemName) {
        return _conditionalNGMap.get(systemName);
    }
    
    /** {@inheritDoc} */
    @Override
    public ConditionalNG getConditionalNGByUserName(String userName) {
        for (ConditionalNG conditionalNG : _conditionalNGMap.values()) {
            if (userName.equals(conditionalNG.getUserName())) {
                return conditionalNG;
            }
        }
        return null;
    }
    
    /** {@inheritDoc} */
    @Override
    public void deleteConditionalNG(String systemName) {
        if (_conditionalNGSystemNames.size() <= 0) {
            return;
        }

        // Remove Conditional from this logix
        if (!_conditionalNGSystemNames.remove(systemName)) {
            log.error("attempt to delete ConditionalNG not in LogixNG: " + systemName);  // NOI18N
            return;
        }
        _conditionalNGMap.remove(systemName);
    }
    
    /** {@inheritDoc} */
    @Override
    public void activateLogixNG() {
        // if the Logix is already busy, simply return
        if (_isActivated) {
            return;
        }
        
        _isActivated = true;
        
        for (ConditionalNG conditionalNG : _conditionalNGMap.values()) {
//            if (conditionalNG.isE
            conditionalNG.registerListeners();
        }
        
        throw new UnsupportedOperationException("Throw exception for now until this is fixed");
/*        
        // set the state of all Conditionals to UNKNOWN
        resetConditionals();
        // assemble a list of needed listeners
        assembleListenerList();
        // create and attach the needed property change listeners
        // start a minute Listener if needed
        for (int i = 0; i < _listeners.size(); i++) {
            startListener(_listeners.get(i));
        }
        // mark this Logix as busy
        _isActivated = true;
        // calculate this Logix to set initial state of Conditionals
        calculateConditionals();
*/
    }

    /** {@inheritDoc} */
    @Override
    public void deActivateLogixNG() {
        if (_isActivated) {
            // Logix is active, deactivate it and all listeners
            _isActivated = false;
            // remove listeners if there are any
            for (ConditionalNG conditionalNG : _conditionalNGMap.values()) {
                conditionalNG.unregisterListeners();
            }
            throw new UnsupportedOperationException("Throw exception for now until this is fixed");
/*        
            for (int i = _listeners.size() - 1; i >= 0; i--) {
                removeListener(_listeners.get(i));
            }
*/
        }
    }

    /** {@inheritDoc} */
    @Override
    public void calculateConditionalNGs() {
        // are there Conditionals to calculate?
        // There are conditionals to calculate
        String cName = "";
        ConditionalNG c = null;
        for (int i = 0; i < _conditionalNGSystemNames.size(); i++) {
            cName = _conditionalNGSystemNames.get(i);
            c = getConditionalNG(cName);
            if (c == null) {
                log.error("Invalid conditional system name when calculating Logix - " + cName);  // NOI18N
            } else {
                // Execute the conditionalNG
                c.execute();
                // calculate without taking any action unless Logix is enabled
//                c.calculate(null);
            }
        }
    }

}
