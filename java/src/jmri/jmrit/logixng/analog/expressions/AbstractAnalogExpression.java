package jmri.jmrit.logixng.analog.expressions;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import jmri.JmriException;
import jmri.jmrit.logixng.implementation.AbstractBase;
import jmri.jmrit.logixng.Base;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jmri.jmrit.logixng.AnalogExpressionBean;

/**
 *
 */
public abstract class AbstractAnalogExpression extends AbstractBase
        implements AnalogExpressionBean {

    private Base _parent = null;
    private Lock _lock = Lock.NONE;
    private int _state = AnalogExpressionBean.UNKNOWN;


    public AbstractAnalogExpression(String sys) throws BadSystemNameException {
        super(sys);
    }

    public AbstractAnalogExpression(String sys, String user)
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
        return Bundle.getMessage("BeanNameAnalogExpression");
    }

    @Override
    public void setState(int s) throws JmriException {
        log.warn("Unexpected call to setState in AbstractAnalogExpression.");  // NOI18N
        _state = s;
    }

    @Override
    public int getState() {
        log.warn("Unexpected call to getState in AbstractAnalogExpression.");  // NOI18N
        return _state;
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
    public void disposeMe() {
    }
    

    private final static Logger log = LoggerFactory.getLogger(AbstractAnalogExpression.class);
}
