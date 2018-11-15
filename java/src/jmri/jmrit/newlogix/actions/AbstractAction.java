package jmri.jmrit.newlogix.actions;

import jmri.JmriException;
import jmri.implementation.AbstractNamedBean;
import jmri.jmrit.newlogix.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The base class for NewLogix Actions
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public abstract class AbstractAction extends AbstractNamedBean
        implements Action {

    public AbstractAction(String sys) throws BadSystemNameException {
        super(sys);
    }

    public AbstractAction(String sys, String user) throws BadUserNameException, BadSystemNameException {
        super(sys, user);
    }
    
    public String getNewSocketName() {
        int x = 1;
        while (x < 10000) {     // Protect from infinite loop
            boolean validName = true;
            for (int i=0; i < getChildCount(); i++) {
                String name = "A" + Integer.toString(x);
                if (name.equals(getChild(i).getName())) {
                    validName = false;
                    break;
                }
            }
            if (validName) {
                return "A" + Integer.toString(x);
            }
            x++;
        }
        throw new RuntimeException("Unable to find a new socket name");
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
