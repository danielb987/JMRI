package jmri.jmrit.newlogix.expressions;

import jmri.jmrit.newlogix.Category;
import jmri.jmrit.newlogix.AbstractExpression;
import jmri.jmrit.newlogix.Expression;
import jmri.jmrit.newlogix.FemaleSocket;

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
public class ExpressionHold extends AbstractExpression {

    private Expression _holdExpression;
    private Expression _triggerExpression;
    private boolean _isActive = false;
    
    public ExpressionHold(String sys, String user, Expression holdExpression,
            Expression triggerExpression) throws BadUserNameException,
            BadSystemNameException {
        
        super(sys, user);
        
        _holdExpression = holdExpression;
        _triggerExpression = triggerExpression;
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
            _isActive = _holdExpression.evaluate();
        } else {
            _isActive = _holdExpression.evaluate() && _triggerExpression.evaluate();
        }
        return _isActive;
    }
    
    /** {@inheritDoc} */
    @Override
    public void reset() {
        _holdExpression.reset();
        _triggerExpression.reset();
    }

    @Override
    public FemaleSocket getChild(int index) throws IllegalArgumentException, UnsupportedOperationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getChildCount() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
