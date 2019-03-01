package jmri.jmrit.logixng.digitalactions;

import jmri.JmriException;
import jmri.implementation.AbstractNamedBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jmri.jmrit.logixng.DigitalAction;

/**
 * The base class for LogixNG Actions
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public abstract class AbstractDigitalAction extends AbstractNamedBean
        implements DigitalAction {

    private Lock _lock = Lock.NONE;
    
    
    public AbstractDigitalAction(String sys) throws BadSystemNameException {
        super(sys);
    }

    public AbstractDigitalAction(String sys, String user) throws BadUserNameException, BadSystemNameException {
        super(sys, user);
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
    
    
    private final static Logger log = LoggerFactory.getLogger(AbstractDigitalAction.class);
}
