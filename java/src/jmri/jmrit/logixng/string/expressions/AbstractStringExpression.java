package jmri.jmrit.logixng.string.expressions;

import jmri.InstanceManager;
import jmri.JmriException;
import jmri.implementation.AbstractNamedBean;
import jmri.jmrit.logixng.Base;
import jmri.jmrit.logixng.MaleSocket;
import jmri.jmrit.logixng.StringExpression;
import jmri.jmrit.logixng.StringExpressionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public abstract class AbstractStringExpression extends AbstractNamedBean
        implements StringExpression {

    private Base _parent = null;
    private Lock _lock = Lock.NONE;


    public AbstractStringExpression(String sys) throws BadSystemNameException {
        super(sys);
    }

    public AbstractStringExpression(String sys, String user) throws BadUserNameException, BadSystemNameException {
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
        return Bundle.getMessage("BeanNameStringExpression");
    }

    @Override
    public void setState(int s) throws JmriException {
        log.warn("Unexpected call to setState in AbstractStringExpression.");  // NOI18N
    }

    @Override
    public int getState() {
        log.warn("Unexpected call to getState in AbstractStringExpression.");  // NOI18N
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
    
    @Override
    public MaleSocket register() {
        return InstanceManager.getDefault(StringExpressionManager.class).registerExpression(this);
    }
    
    
    private final static Logger log = LoggerFactory.getLogger(AbstractStringExpression.class);
}
