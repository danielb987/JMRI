package jmri.jmrit.logixng.analog.actions;

import jmri.InstanceManager;
import jmri.JmriException;
import jmri.implementation.AbstractNamedBean;
import jmri.jmrit.logixng.Base;
import jmri.jmrit.logixng.AnalogAction;
import jmri.jmrit.logixng.AnalogActionManager;
import jmri.jmrit.logixng.MaleSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The base class for LogixNG AnalogActions
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public abstract class AbstractAnalogAction extends AbstractNamedBean
        implements AnalogAction {

    private Base _parent = null;
    private Lock _lock = Lock.NONE;
    private boolean _userEnabled = false;


    public AbstractAnalogAction(String sys) throws BadSystemNameException {
        super(sys);
    }

    public AbstractAnalogAction(String sys, String user)
            throws BadUserNameException, BadSystemNameException {
        super(sys, user);
    }

    /** {@inheritDoc} */
    @Override
    public Base getParent() {
        return _parent;
    }

    /** {@inheritDoc} */
    @Override
    public void setParent(Base parent) {
        _parent = parent;
    }

    @Override
    public String getBeanType() {
        return Bundle.getMessage("BeanNameAnalogAction");
    }

    @Override
    public void setState(int s) throws JmriException {
        log.warn("Unexpected call to setState in AbstractAnalogAction.");  // NOI18N
    }

    @Override
    public int getState() {
        log.warn("Unexpected call to getState in AbstractAnalogAction.");  // NOI18N
        return UNKNOWN;
    }
    
    
    /** {@inheritDoc} */
    @Override
    public Lock getLock() {
        return _lock;
    }

    /** {@inheritDoc} */
    @Override
    public void setLock(Lock lock) {
        _lock = lock;
    }
    
    /** {@inheritDoc} */
    @Override
    public void dispose() {
        for (int i=0; i < getChildCount(); i++) {
            getChild(i).dispose();
        }
        super.dispose();
    }
    
    /** {@inheritDoc} */
    @Override
    public void setEnabled(boolean enable) {
        // Do nothing. This is handled by the male socket.
    }
    
    /** {@inheritDoc} */
    @Override
    public boolean isEnabled() {
        return _userEnabled && _parent.isEnabled();
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
    
    
    private final static Logger log = LoggerFactory.getLogger(AbstractAnalogAction.class);
}
