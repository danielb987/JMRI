package jmri.jmrit.newlogix.expressions;

import jmri.jmrit.newlogix.Category;
import jmri.jmrit.newlogix.Expression;
import jmri.jmrit.newlogix.FemaleSocket;

/**
 * This Expression has two expressions, the primary expression and the secondary
 expression. When the primary expression becomes True after have been False,
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
public class ExpressionResetOnTrue extends AbstractExpression {

    private Expression _primaryExpression;
    private Expression _secondaryExpression;
    private boolean _lastMainResult = false;
    
    public ExpressionResetOnTrue(String sys, String user,
            Expression primaryExpression, Expression secondaryExpression)
            throws BadUserNameException, BadSystemNameException {
        
        super(sys, user);
        
        _primaryExpression = primaryExpression;
        _secondaryExpression = secondaryExpression;
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
        boolean result = _primaryExpression.evaluate();
        if (!_lastMainResult && result) {
            _secondaryExpression.reset();
        }
        _lastMainResult = result;
        result |= _secondaryExpression.evaluate();
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public void reset() {
        _primaryExpression.reset();
        _secondaryExpression.reset();
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
