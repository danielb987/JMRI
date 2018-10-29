package jmri.jmrit.newlogix.expressions;

import jmri.InstanceManager;
import jmri.jmrit.newlogix.Category;
import jmri.jmrit.newlogix.AbstractExpression;
import jmri.jmrit.newlogix.Expression;
import jmri.jmrit.newlogix.FemaleExpressionSocket;
import jmri.jmrit.newlogix.FemaleSocket;
import jmri.jmrit.newlogix.FemaleSocketListener;
import jmri.jmrit.newlogix.NewLogixManager;

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
public class ExpressionHold extends AbstractExpression implements FemaleSocketListener {

    private FemaleExpressionSocket _holdExpressionSocket;
    private FemaleExpressionSocket _triggerExpressionSocket;
    private boolean _isActive = false;
    
    public ExpressionHold(String sys) throws BadUserNameException,
            BadSystemNameException {
        
        super(sys);
        
        _holdExpressionSocket = InstanceManager.getDefault(NewLogixManager.class)
                .createFemaleExpressionSocket(this, "E1");
        _triggerExpressionSocket = InstanceManager.getDefault(NewLogixManager.class)
                .createFemaleExpressionSocket(this, "E2");
    }

    public ExpressionHold(String sys, String user) throws BadUserNameException,
            BadSystemNameException {
        
        super(sys, user);
        
        _holdExpressionSocket = InstanceManager.getDefault(NewLogixManager.class)
                .createFemaleExpressionSocket(this, "E1");
        _triggerExpressionSocket = InstanceManager.getDefault(NewLogixManager.class)
                .createFemaleExpressionSocket(this, "E2");
    }

    public ExpressionHold(String sys, String holdExpressionSocketName,
            String triggerExpressionSocketName, Expression holdExpression,
            Expression triggerExpression) throws BadUserNameException,
            BadSystemNameException {
        
        super(sys);
        
        _holdExpressionSocket = InstanceManager.getDefault(NewLogixManager.class)
                .createFemaleExpressionSocket(this, holdExpressionSocketName,
                        holdExpression);
        _triggerExpressionSocket = InstanceManager.getDefault(NewLogixManager.class)
                .createFemaleExpressionSocket(this, triggerExpressionSocketName,
                        triggerExpression);
    }

    public ExpressionHold(String sys, String user, String holdExpressionSocketName,
            String triggerExpressionSocketName, Expression holdExpression,
            Expression triggerExpression) throws BadUserNameException,
            BadSystemNameException {
        
        super(sys, user);
        
        _holdExpressionSocket = InstanceManager.getDefault(NewLogixManager.class)
                .createFemaleExpressionSocket(this, holdExpressionSocketName,
                        holdExpression);
        _triggerExpressionSocket = InstanceManager.getDefault(NewLogixManager.class)
                .createFemaleExpressionSocket(this, triggerExpressionSocketName,
                        triggerExpression);
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
    public String toString() {
        return Bundle.getMessage("ExpressionHold",
                _holdExpressionSocket.getName(),
                _triggerExpressionSocket.getName());
    }
    
}
