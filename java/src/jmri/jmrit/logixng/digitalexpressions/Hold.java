package jmri.jmrit.logixng.digitalexpressions;

import jmri.InstanceManager;
import jmri.jmrit.logixng.Category;
import jmri.jmrit.logixng.FemaleSocket;
import jmri.jmrit.logixng.FemaleSocketListener;
import jmri.jmrit.logixng.DigitalExpressionManager;
import jmri.jmrit.logixng.FemaleDigitalExpressionSocket;
import jmri.jmrit.logixng.MaleDigitalExpressionSocket;

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

    private final FemaleDigitalExpressionSocket _holdExpressionSocket;
    private final FemaleDigitalExpressionSocket _triggerExpressionSocket;
    private boolean _isActive = false;
    
    public Hold(String sys) throws BadUserNameException,
            BadSystemNameException {
        
        super(sys);
        
        _holdExpressionSocket = InstanceManager.getDefault(DigitalExpressionManager.class)
                .createFemaleExpressionSocket(this, "E1");
        _triggerExpressionSocket = InstanceManager.getDefault(DigitalExpressionManager.class)
                .createFemaleExpressionSocket(this, "E2");
    }

    public Hold(String sys, String user) throws BadUserNameException,
            BadSystemNameException {
        
        super(sys, user);
        
        _holdExpressionSocket = InstanceManager.getDefault(DigitalExpressionManager.class)
                .createFemaleExpressionSocket(this, "E1");
        _triggerExpressionSocket = InstanceManager.getDefault(DigitalExpressionManager.class)
                .createFemaleExpressionSocket(this, "E2");
    }

    public Hold(
            String sys,
            String holdExpressionSocketName,
            String triggerExpressionSocketName,
            MaleDigitalExpressionSocket holdExpression,
            MaleDigitalExpressionSocket triggerExpression)
            
            throws BadUserNameException,
            BadSystemNameException {
        
        super(sys);
        
        _holdExpressionSocket = InstanceManager.getDefault(DigitalExpressionManager.class)
                .createFemaleExpressionSocket(
                        this, holdExpressionSocketName, holdExpression);
        _triggerExpressionSocket = InstanceManager.getDefault(DigitalExpressionManager.class)
                .createFemaleExpressionSocket(
                        this, triggerExpressionSocketName, triggerExpression);
    }

    public Hold(
            String sys, String user,
            String holdExpressionSocketName,
            String triggerExpressionSocketName,
            MaleDigitalExpressionSocket holdExpression,
            MaleDigitalExpressionSocket triggerExpression)
            
            throws BadUserNameException,
            BadSystemNameException {
        
        super(sys, user);
        
        _holdExpressionSocket = InstanceManager.getDefault(DigitalExpressionManager.class)
                .createFemaleExpressionSocket(
                        this, holdExpressionSocketName, holdExpression);
        _triggerExpressionSocket = InstanceManager.getDefault(DigitalExpressionManager.class)
                .createFemaleExpressionSocket(
                        this, triggerExpressionSocketName, triggerExpression);
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
        if (_isActive) {
            _isActive = _holdExpressionSocket.evaluate();
        } else {
            _isActive = _holdExpressionSocket.evaluate() && _triggerExpressionSocket.evaluate();
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
        return Bundle.getMessage("Hold",
                _holdExpressionSocket.getName(),
                _triggerExpressionSocket.getName());
    }
    
    @Override
    public String getLongDescription() {
        return getShortDescription();
    }

}
