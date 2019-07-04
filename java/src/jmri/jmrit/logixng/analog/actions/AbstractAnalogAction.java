package jmri.jmrit.logixng.analog.actions;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import jmri.InstanceManager;
import jmri.JmriException;
import jmri.implementation.AbstractNamedBean;
import jmri.jmrit.logixng.Base;
import jmri.jmrit.logixng.AnalogActionManager;
import jmri.jmrit.logixng.MaleSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jmri.jmrit.logixng.AnalogActionBean;

/**
 * The base class for LogixNG AnalogActions
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public abstract class AbstractAnalogAction extends AbstractNamedBean
        implements AnalogActionBean {

    private Base _parent = null;
    private Lock _lock = Lock.NONE;


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
    @OverridingMethodsMustInvokeSuper
    public void dispose() {
        for (int i=0; i < getChildCount(); i++) {
            getChild(i).dispose();
        }
        super.dispose();
    }
    
    
    private final static Logger log = LoggerFactory.getLogger(AbstractAnalogAction.class);
}
