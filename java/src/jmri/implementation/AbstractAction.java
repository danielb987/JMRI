package jmri.implementation;

import jmri.JmriException;
import jmri.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public abstract class AbstractAction extends AbstractNamedBean
        implements Action {

    public AbstractAction(String sys) throws BadSystemNameException {
        super(sys);
    }

    public AbstractAction(String sys, String user) throws BadUserNameException, BadSystemNameException {
        super(sys, user);
    }

    @Override
    public String getBeanType() {
        return Bundle.getMessage("BeanNameAction");
    }

    @Override
    public void setState(int s) throws JmriException {
        log.warn("Unexpected call to setState in AbstractAction.");  // NOI18N
    }

    @Override
    public int getState() {
        log.warn("Unexpected call to getState in AbstractAction.");  // NOI18N
        return UNKNOWN;
    }

    private final static Logger log = LoggerFactory.getLogger(AbstractAction.class);
}
