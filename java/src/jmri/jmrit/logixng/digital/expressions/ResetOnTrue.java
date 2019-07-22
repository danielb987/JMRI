package jmri.jmrit.logixng.digital.expressions;

import java.util.concurrent.atomic.AtomicBoolean;
import jmri.InstanceManager;
import jmri.jmrit.logixng.Base;
import jmri.jmrit.logixng.Category;
import jmri.jmrit.logixng.ConditionalNG;
import jmri.jmrit.logixng.FemaleSocket;
import jmri.jmrit.logixng.FemaleSocketListener;
import jmri.jmrit.logixng.SocketAlreadyConnectedException;
import jmri.jmrit.logixng.DigitalExpressionManager;
import jmri.jmrit.logixng.FemaleDigitalExpressionSocket;
import jmri.jmrit.logixng.MaleDigitalExpressionSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This Expression has two expressions, the primary expression and the secondary
 * expression. When the primary expression becomes True after have been False,
 * the secondary expression is reset.
 * 
 * The result of the evaluation of this expression is True if both the
 * expressions evaluates to True.
 * 
 * This expression is used for example if one expression should trigger a timer.
 * If the primary expression is a sensor having a certain state and the secondary
 * expression is a timer, this expression will evaluate to True if the sensor
 * has had that state during the specified time.
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public class ResetOnTrue extends AbstractDigitalExpression implements FemaleSocketListener {

    private ResetOnTrue _template;
    private String _primaryExpressionSocketSystemName;
    private String _secondaryExpressionSocketSystemName;
    private final FemaleDigitalExpressionSocket _primaryExpressionSocket;
    private final FemaleDigitalExpressionSocket _secondaryExpressionSocket;
    private boolean _lastMainResult = false;
    
    public ResetOnTrue(ConditionalNG conditionalNG) {
        
        super(InstanceManager.getDefault(DigitalExpressionManager.class).getNewSystemName(conditionalNG));
        
        _primaryExpressionSocket = InstanceManager.getDefault(DigitalExpressionManager.class)
                .createFemaleSocket(this, this, "E1");
        _secondaryExpressionSocket = InstanceManager.getDefault(DigitalExpressionManager.class)
                .createFemaleSocket(this, this, "E2");
    }
    
    public ResetOnTrue(String sys, String user) {
        
        super(sys, user);
        
        _primaryExpressionSocket = InstanceManager.getDefault(DigitalExpressionManager.class)
                .createFemaleSocket(this, this, "E1");
        _secondaryExpressionSocket = InstanceManager.getDefault(DigitalExpressionManager.class)
                .createFemaleSocket(this, this, "E2");
    }
    
    public ResetOnTrue(String sys, String user,
            MaleDigitalExpressionSocket primaryExpression,
            MaleDigitalExpressionSocket secondaryExpression) {
        
        super(sys, user);
        
        _primaryExpressionSocket = InstanceManager.getDefault(DigitalExpressionManager.class)
                .createFemaleSocket(this, this, "E1");
        _secondaryExpressionSocket = InstanceManager.getDefault(DigitalExpressionManager.class)
                .createFemaleSocket(this, this, "E2");
        
        try {
            _primaryExpressionSocket.connect(primaryExpression);
            _secondaryExpressionSocket.connect(secondaryExpression);
        } catch (SocketAlreadyConnectedException ex) {
            log.error("socket is already connected", ex);
        }
    }
    
    private ResetOnTrue(ResetOnTrue template, String sys) {
        super(sys);
        _template = template;
        _primaryExpressionSocket = InstanceManager.getDefault(DigitalExpressionManager.class)
                .createFemaleSocket(this, this, _template._primaryExpressionSocket.getName());
        _secondaryExpressionSocket = InstanceManager.getDefault(DigitalExpressionManager.class)
                .createFemaleSocket(this, this, _template._secondaryExpressionSocket.getName());
    }
    
    /** {@inheritDoc} */
    @Override
    public Base getNewObjectBasedOnTemplate(String sys) {
        return new ResetOnTrue(this, sys);
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
    public boolean evaluate() {
        boolean result = _primaryExpressionSocket.evaluate();
        if (!_lastMainResult && result) {
            _secondaryExpressionSocket.reset();
        }
        _lastMainResult = result;
        result |= _secondaryExpressionSocket.evaluate();
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public void reset() {
        _primaryExpressionSocket.reset();
        _secondaryExpressionSocket.reset();
    }

    @Override
    public FemaleSocket getChild(int index) throws IllegalArgumentException, UnsupportedOperationException {
        switch (index) {
            case 0:
                return _primaryExpressionSocket;
                
            case 1:
                return _secondaryExpressionSocket;
                
            default:
                throw new IllegalArgumentException(
                        String.format("index has invalid value: %d", index));
        }
    }

    @Override
    public int getChildCount() {
        return 2;
    }

    @Override
    public String getShortDescription() {
        return Bundle.getMessage("ResetOnTrue_Short");
    }
    
    @Override
    public String getLongDescription() {
        return Bundle.getMessage("ResetOnTrue_Long");
    }

    @Override
    public void connected(FemaleSocket socket) {
        // This class doesn't care.
    }

    @Override
    public void disconnected(FemaleSocket socket) {
        // This class doesn't care.
    }

    public void setPrimaryExpressionSocketSystemName(String systemName) {
        _primaryExpressionSocketSystemName = systemName;
    }

    public void setSecondaryExpressionSocketSystemName(String systemName) {
        _secondaryExpressionSocketSystemName = systemName;
    }

    /** {@inheritDoc} */
    @Override
    public void setup() {
        try {
            if ((!_primaryExpressionSocket.isConnected()) && (_primaryExpressionSocketSystemName != null)) {
                _primaryExpressionSocket.connect(
                        InstanceManager.getDefault(DigitalExpressionManager.class)
                                .getBeanBySystemName(_primaryExpressionSocketSystemName));
            }
            if ((!_secondaryExpressionSocket.isConnected()) && (_secondaryExpressionSocketSystemName != null)) {
                _secondaryExpressionSocket.connect(
                        InstanceManager.getDefault(DigitalExpressionManager.class)
                                .getBeanBySystemName(_secondaryExpressionSocketSystemName));
            }
        } catch (SocketAlreadyConnectedException ex) {
            // This shouldn't happen and is a runtime error if it does.
            throw new RuntimeException("socket is already connected");
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public void registerListenersForThisClass() {
    }
    
    /** {@inheritDoc} */
    @Override
    public void unregisterListenersForThisClass() {
    }
    
    /** {@inheritDoc} */
    @Override
    public void disposeMe() {
    }

    private final static Logger log = LoggerFactory.getLogger(ResetOnTrue.class);
}
