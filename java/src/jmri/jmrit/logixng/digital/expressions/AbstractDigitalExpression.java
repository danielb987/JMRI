package jmri.jmrit.logixng.digital.expressions;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import jmri.InstanceManager;
import jmri.JmriException;
import jmri.implementation.AbstractNamedBean;
import jmri.jmrit.logixng.Base;
import jmri.jmrit.logixng.DigitalExpressionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jmri.jmrit.logixng.MaleSocket;
import jmri.jmrit.logixng.DigitalExpressionBean;

/**
 *
 */
public abstract class AbstractDigitalExpression extends AbstractNamedBean
        implements DigitalExpressionBean {

    private Base _parent = null;
    private Lock _lock = Lock.NONE;
    
    
    public AbstractDigitalExpression(String sys) throws BadSystemNameException {
        super(sys);
    }

    public AbstractDigitalExpression(String sys, String user)
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
        return Bundle.getMessage("BeanNameDigitalExpression");
    }

    @Override
    public void setState(int s) throws JmriException {
        log.warn("Unexpected call to setState in AbstractExpression.");  // NOI18N
    }

    @Override
    public int getState() {
        log.warn("Unexpected call to getState in AbstractExpression.");  // NOI18N
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
    
    public String getNewSocketName() {
        int x = 1;
        while (x < 10000) {     // Protect from infinite loop
            boolean validName = true;
            for (int i=0; i < getChildCount(); i++) {
                String name = "E" + Integer.toString(x);
                if (name.equals(getChild(i).getName())) {
                    validName = false;
                    break;
                }
            }
            if (validName) {
                return "E" + Integer.toString(x);
            }
            x++;
        }
        throw new RuntimeException("Unable to find a new socket name");
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
    
    
    private final static Logger log = LoggerFactory.getLogger(AbstractDigitalExpression.class);
}
