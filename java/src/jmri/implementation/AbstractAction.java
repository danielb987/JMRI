package jmri.implementation;

import jmri.JmriException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jmri.NewLogixAction;

/**
 * The base class for NewLogix Actions
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public abstract class AbstractAction extends AbstractNamedBean
        implements NewLogixAction {

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
    
    
    /** {@inheritDoc} */
    @Override
    public String getConfiguratorClassName() {
        String className = this.getClass().getName();
        log.trace("handle object of class {}", className);
        int lastDot = className.lastIndexOf(".");
        if (lastDot > 0) {
            // found package-class boundary OK
            String result = className.substring(0, lastDot)
                    + ".swing."
                    + className.substring(lastDot + 1, className.length())
                    + "Configurator";
            log.trace("adapter class name is {}", result);
            return result;
        } else {
            // no last dot found!
            log.error("No package name found, which is not yet handled!");
            return null;
        }
    }
    
    
    //************************************************************************
    // For testing only
    //************************************************************************
    
    @Override
    public String getShortDescription() {
        return this.getClass().getSimpleName();
    }
    
    @Override
    public String getLongDescription() {
        return this.getClass().getName();
    }

    private final static Logger log = LoggerFactory.getLogger(AbstractAction.class);
}
