package jmri.jmrit.logixng.engine;

import static jmri.NamedBean.UNKNOWN;

import java.util.SortedSet;
import jmri.InstanceManager;
import jmri.JmriException;
import jmri.implementation.AbstractNamedBean;
import jmri.jmrit.logixng.Action;
import jmri.jmrit.logixng.ActionManager;
import jmri.jmrit.logixng.FemaleActionSocket;
import jmri.jmrit.logixng.FemaleSocket;
import jmri.jmrit.logixng.FemaleSocketListener;
import jmri.jmrit.logixng.MaleActionSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jmri.jmrit.logixng.LogixNG;
import jmri.jmrit.logixng.LogixNG_Manager;

/**
 * The default implementation of LogixNG.
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public final class DefaultLogixNG extends AbstractNamedBean
        implements LogixNG, FemaleSocketListener {
    
//    private Action _action;
    private final FemaleActionSocket _femaleActionSocket;
    private boolean _enabled = false;
    
    public DefaultLogixNG(String sys, String user) throws BadUserNameException, BadSystemNameException  {
        super(sys, user);
        _femaleActionSocket = InstanceManager.getDefault(ActionManager.class).createFemaleActionSocket(this, "");
    }

    public DefaultLogixNG(String sys, String user, MaleActionSocket action) throws BadUserNameException, BadSystemNameException  {
        super(sys, user);
        _femaleActionSocket = InstanceManager.getDefault(ActionManager.class).createFemaleActionSocket(this, "", action);
    }
    
    /** {@inheritDoc} */
    @Override
    public FemaleSocket getFemaleSocket() {
        return _femaleActionSocket;
    }
    
    /** {@inheritDoc} */
    @Override
    public void execute() {
        if (_enabled) {
            _enabled = _femaleActionSocket.executeStart();
        } else {
            _enabled = _femaleActionSocket.executeContinue();
        }
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

    /**
     * Set enabled status. Enabled is a bound property All conditionals are set
     * to UNKNOWN state and recalculated when the Logix is enabled, provided the
     * Logix has been previously activated.
     */
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
        }
    }

    /**
     * Get enabled status
     */
    @Override
    public boolean getEnabled() {
        return _enabled;
    }

    private final static Logger log = LoggerFactory.getLogger(DefaultLogixNG.class);

    @Override
    public void connected(FemaleSocket socket) {
        // Do nothing
    }

    @Override
    public void disconnected(FemaleSocket socket) {
        // Do nothing
    }
}
