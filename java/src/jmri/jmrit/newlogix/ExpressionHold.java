package jmri.jmrit.newlogix;

import jmri.Expression;
import jmri.NewLogixCategory;

/**
 * An Expression that keeps its status even if its child expression doesn't.
 * 
 * This expression stays False until both the 'hold' expression and the 'trigger'
 * expression becomes True. It stays true until the 'hold' expression goes to
 * False. The 'trigger' expression can for example be a push button that stays
 * True for a short time.
 * 
 * @author Daniel Bergqvist 2018
 */
public class ExpressionHold implements Expression {

    private Expression _holdExpression;
    private Expression _triggerExpression;
    private boolean _isActive = false;
    
    public ExpressionHold(Expression holdExpression, Expression triggerExpression) {
        _holdExpression = holdExpression;
        _triggerExpression = triggerExpression;
    }

    @Override
    public NewLogixCategory getCategory() {
        return NewLogixCategory.OTHER;
    }
    
    @Override
    public boolean evaluate() {
        if (_isActive) {
            _isActive = _holdExpression.evaluate();
        } else {
            _isActive = _holdExpression.evaluate() && _triggerExpression.evaluate();
        }
        return _isActive;
    }
    
    @Override
    public void reset() {
        _holdExpression.reset();
        _triggerExpression.reset();
    }
    
}
