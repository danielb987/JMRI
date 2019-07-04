package jmri.jmrit.logixng.string.expressions;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import jmri.JmriException;
import jmri.implementation.AbstractNamedBean;
import jmri.jmrit.logixng.Base;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jmri.jmrit.logixng.StringExpressionBean;

/**
 *
 */
public abstract class AbstractStringExpression extends AbstractNamedBean
        implements StringExpressionBean {

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
    @OverridingMethodsMustInvokeSuper
    public void dispose() {
        for (int i=0; i < getChildCount(); i++) {
            getChild(i).dispose();
        }
        super.dispose();
    }
    

    private final static Logger log = LoggerFactory.getLogger(AbstractStringExpression.class);
}
