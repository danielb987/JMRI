package jmri.jmrit.logixng.digital.expressions;

import java.util.concurrent.atomic.AtomicBoolean;
import jmri.InstanceManager;
import jmri.jmrit.logixng.Base;
import jmri.jmrit.logixng.Category;
import jmri.jmrit.logixng.FemaleSocket;
import jmri.jmrit.logixng.FemaleSocketListener;
import jmri.jmrit.logixng.DigitalActionManager;
import jmri.jmrit.logixng.DigitalExpressionManager;
import jmri.jmrit.logixng.FemaleDigitalExpressionSocket;
import jmri.jmrit.logixng.MaleDigitalExpressionSocket;
import jmri.jmrit.logixng.SocketAlreadyConnectedException;
import jmri.jmrit.logixng.analog.actions.SetAnalogIO;

/**
 * An Expression that keeps its status even if its child expression doesn't.
 * 
 * This expression stays False until both the 'hold' expression and the 'trigger'
 * expression becomes True. It stays true until the 'hold' expression goes to
 * False. The 'trigger' expression can for example be a push button that stays
 * True for a short time.
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public class Hold extends AbstractDigitalExpression implements FemaleSocketListener {

    private Hold _template;
    private String _holdExpressionSocketSystemName;
    private String _triggerExpressionSocketSystemName;
    private final FemaleDigitalExpressionSocket _holdExpressionSocket;
    private final FemaleDigitalExpressionSocket _triggerExpressionSocket;
    private boolean _isActive = false;
    
    public Hold(String sys) throws BadUserNameException, BadSystemNameException {
        
        super(sys);
        
        _holdExpressionSocket = InstanceManager.getDefault(DigitalExpressionManager.class)
                .createFemaleSocket(this, this, "E1");
        _triggerExpressionSocket = InstanceManager.getDefault(DigitalExpressionManager.class)
                .createFemaleSocket(this, this, "E2");
    }

    public Hold(String sys, String user)
            throws BadUserNameException, BadSystemNameException {
        
        super(sys, user);
        
        _holdExpressionSocket = InstanceManager.getDefault(DigitalExpressionManager.class)
                .createFemaleSocket(this, this, "E1");
        _triggerExpressionSocket = InstanceManager.getDefault(DigitalExpressionManager.class)
                .createFemaleSocket(this, this, "E2");
    }

    public Hold(
            String sys,
            String holdExpressionSocketName,
            String triggerExpressionSocketName,
            MaleDigitalExpressionSocket holdExpression,
            MaleDigitalExpressionSocket triggerExpression)
            
            throws BadUserNameException, BadSystemNameException {
        
        super(sys);
        
        _holdExpressionSocket = InstanceManager.getDefault(DigitalExpressionManager.class)
                .createFemaleExpressionSocket(
                        this, this, holdExpressionSocketName, holdExpression);
        _triggerExpressionSocket = InstanceManager.getDefault(DigitalExpressionManager.class)
                .createFemaleExpressionSocket(
                        this, this, triggerExpressionSocketName, triggerExpression);
    }

    public Hold(
            String sys, String user,
            String holdExpressionSocketName,
            String triggerExpressionSocketName,
            MaleDigitalExpressionSocket holdExpression,
            MaleDigitalExpressionSocket triggerExpression)
            
            throws BadUserNameException, BadSystemNameException {
        
        super(sys, user);
        
        _holdExpressionSocket = InstanceManager.getDefault(DigitalExpressionManager.class)
                .createFemaleExpressionSocket(
                        this, this, holdExpressionSocketName, holdExpression);
        _triggerExpressionSocket = InstanceManager.getDefault(DigitalExpressionManager.class)
                .createFemaleExpressionSocket(
                        this, this, triggerExpressionSocketName, triggerExpression);
    }

    private Hold(Hold template, String sys) {
        super(sys);
        _template = template;
        _holdExpressionSocket = InstanceManager.getDefault(DigitalExpressionManager.class)
                .createFemaleSocket(this, this, _template._holdExpressionSocket.getName());
        _triggerExpressionSocket = InstanceManager.getDefault(DigitalExpressionManager.class)
                .createFemaleSocket(this, this, _template._triggerExpressionSocket.getName());
    }
    
    /** {@inheritDoc} */
    @Override
    public Base getNewObjectBasedOnTemplate(String sys) {
        return new Hold(this, sys);
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
        if (_isActive) {
            _isActive = _holdExpressionSocket.evaluate(isCompleted);
        } else {
            _isActive = _holdExpressionSocket.evaluate(isCompleted) && _triggerExpressionSocket.evaluate(isCompleted);
        }
        return _isActive;
    }
    
    /** {@inheritDoc} */
    @Override
    public void reset() {
        _holdExpressionSocket.reset();
        _triggerExpressionSocket.reset();
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
        return Bundle.getMessage("Hold_Short");
    }
    
    @Override
    public String getLongDescription() {
        return Bundle.getMessage("Hold_Long",
                _holdExpressionSocket.getName(),
                _triggerExpressionSocket.getName());
    }

    public void setHoldActionSocketSystemName(String systemName) {
        _holdExpressionSocketSystemName = systemName;
    }

    public void setTriggerExpressionSocketSystemName(String systemName) {
        _triggerExpressionSocketSystemName = systemName;
    }

    /** {@inheritDoc} */
    @Override
    public void setup() {
        try {
            if ((!_holdExpressionSocket.isConnected()) && (_holdExpressionSocketSystemName != null)) {
                _holdExpressionSocket.connect(
                        InstanceManager.getDefault(DigitalActionManager.class)
                                .getBeanBySystemName(_holdExpressionSocketSystemName));
            }
            if ((!_triggerExpressionSocket.isConnected()) && (_triggerExpressionSocketSystemName != null)) {
                _triggerExpressionSocket.connect(
                        InstanceManager.getDefault(DigitalExpressionManager.class)
                                .getBeanBySystemName(_triggerExpressionSocketSystemName));
            }
        } catch (SocketAlreadyConnectedException ex) {
            // This shouldn't happen and is a runtime error if it does.
            throw new RuntimeException("socket is already connected");
        }
    }

}
