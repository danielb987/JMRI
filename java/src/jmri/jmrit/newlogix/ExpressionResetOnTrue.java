package jmri.jmrit.newlogix;

import jmri.Expression;
import jmri.NewLogixCategory;

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
 * @author Daniel Bergqvist 2018
 */
public class ExpressionResetOnTrue implements Expression {

    private Expression _primaryExpression;
    private Expression _secondaryExpression;
    private boolean _lastMainResult = false;
    
    public ExpressionResetOnTrue(Expression primaryExpression, Expression secondaryExpression) {
        _primaryExpression = primaryExpression;
        _secondaryExpression = secondaryExpression;
    }
    
    @Override
    public NewLogixCategory getCategory() {
        return NewLogixCategory.OTHER;
    }

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

    @Override
    public void reset() {
        _primaryExpression.reset();
        _secondaryExpression.reset();
    }

}
