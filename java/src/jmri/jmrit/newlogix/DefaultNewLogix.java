package jmri.jmrit.newlogix;

import static jmri.NamedBean.UNKNOWN;

import jmri.NewLogix;
import jmri.Action;
import jmri.JmriException;
import jmri.implementation.AbstractNamedBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The default implementation of NewLogix.
 * 
 * @author Daniel Bergqvist 2018
 */
public class DefaultNewLogix extends AbstractNamedBean
        implements NewLogix {
    
    private Action _action;
    private boolean _isActive = false;
    
    public DefaultNewLogix(String sys, String user, Action action) throws BadUserNameException, BadSystemNameException  {
        super(sys, user);
        _action = action;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute() {
        if (_isActive) {
            _isActive = _action.executeStart();
        } else {
            _isActive = _action.executeContinue();
        }
    }

    @Override
    public String getBeanType() {
        return Bundle.getMessage("BeanNameNewLogix");
    }

    @Override
    public void setState(int s) throws JmriException {
        log.warn("Unexpected call to setState in DefaultNewLogix.");  // NOI18N
    }

    @Override
    public int getState() {
        log.warn("Unexpected call to getState in DefaultNewLogix.");  // NOI18N
        return UNKNOWN;
    }

    private final static Logger log = LoggerFactory.getLogger(DefaultNewLogix.class);
}
