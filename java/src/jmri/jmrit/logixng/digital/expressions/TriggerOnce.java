package jmri.jmrit.logixng.digital.expressions;

import java.util.concurrent.atomic.AtomicBoolean;
import jmri.InstanceManager;
import jmri.jmrit.logixng.Base;
import jmri.jmrit.logixng.Category;
import jmri.jmrit.logixng.FemaleSocket;
import jmri.jmrit.logixng.FemaleSocketListener;
import jmri.jmrit.logixng.SocketAlreadyConnectedException;
import jmri.jmrit.logixng.DigitalExpressionManager;
import jmri.jmrit.logixng.FemaleDigitalExpressionSocket;
import jmri.jmrit.logixng.MaleDigitalExpressionSocket;

/**
 * An Expression that returns True only once while its child expression returns
 True.
 * 
 * The first time the child expression returns True, this expression returns
 * True. After that, this expression returns False until the child expression
 * returns False and again returns True.
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public class TriggerOnce extends AbstractDigitalExpression implements FemaleSocketListener {

    private TriggerOnce _template;
    private String _childExpressionSystemName;
    private final FemaleDigitalExpressionSocket _childExpression;
    private boolean _childLastState = false;
    
    public TriggerOnce(String sys, String user)
            throws BadUserNameException, BadSystemNameException, SocketAlreadyConnectedException {
        
        super(sys, user);
        
        _childExpression = InstanceManager.getDefault(DigitalExpressionManager.class)
                .createFemaleSocket(this, this, "E1");
    }
    
    public TriggerOnce(String sys, String user, MaleDigitalExpressionSocket expression)
            throws BadUserNameException, BadSystemNameException, SocketAlreadyConnectedException {
        
        super(sys, user);
        
        _childExpression = InstanceManager.getDefault(DigitalExpressionManager.class)
                .createFemaleSocket(this, this, "E1");
        _childExpression.connect(expression);
    }
    
    private TriggerOnce(TriggerOnce template, String sys) {
        super(sys);
        _template = template;
        _childExpression = InstanceManager.getDefault(DigitalExpressionManager.class)
                .createFemaleSocket(this, this, _template._childExpression.getName());
    }
    
    /** {@inheritDoc} */
    @Override
    public Base getNewObjectBasedOnTemplate(String sys) {
        return new TriggerOnce(this, sys);
    }
    
    /** {@inheritDoc} */
    @Override
    public Category getCategory() {
        return Category.OTHER;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isExternal() {
        return false;
    }
    
    /** {@inheritDoc} */
    @Override
    public void initEvaluation() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    /** {@inheritDoc} */
    @Override
    public boolean evaluate(AtomicBoolean isCompleted) {
        if (_childExpression.evaluate(isCompleted) && !_childLastState) {
            _childLastState = true;
            return true;
        }
        _childLastState = _childExpression.evaluate(isCompleted);
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public void reset() {
        _childLastState = false;
    }

    @Override
    public FemaleSocket getChild(int index) throws IllegalArgumentException, UnsupportedOperationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getChildCount() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public void connected(FemaleSocket socket) {
        // This class doesn't care.
    }
    
    @Override
    public void disconnected(FemaleSocket socket) {
        // This class doesn't care.
    }

    @Override
    public String getShortDescription() {
        return Bundle.getMessage("TriggerOnce_Short");
    }
    
    @Override
    public String getLongDescription() {
        return Bundle.getMessage("TriggerOnce_Long");
    }

    public void setAnalogActionSocketSystemName(String systemName) {
        _childExpressionSystemName = systemName;
    }

    /** {@inheritDoc} */
    @Override
    public void setup() {
        try {
            if ((!_childExpression.isConnected()) && (_childExpressionSystemName != null)) {
                _childExpression.connect(
                        InstanceManager.getDefault(DigitalExpressionManager.class)
                                .getBeanBySystemName(_childExpressionSystemName));
            }
        } catch (SocketAlreadyConnectedException ex) {
            // This shouldn't happen and is a runtime error if it does.
            throw new RuntimeException("socket is already connected");
        }
    }

}
