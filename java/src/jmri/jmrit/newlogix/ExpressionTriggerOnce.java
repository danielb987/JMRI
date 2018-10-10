package jmri.jmrit.newlogix;

import jmri.Expression;
import jmri.NewLogixCategory;

/**
 * An Expression that returns True only once while its child expression returns
 * True.
 * 
 * The first time the child expression returns True, this expression returns
 * True. After that, this expression returns False until the child expression
 * returns False and again returns True.
 * 
 * @author Daniel Bergqvist 2018
 */
public class ExpressionTriggerOnce implements Expression {

    private Expression _childExpression;
    private boolean _childLastState = false;
    
    public ExpressionTriggerOnce(Expression childExpression) {
        _childExpression = childExpression;
    }
    
    @Override
    public NewLogixCategory getCategory() {
        return NewLogixCategory.OTHER;
    }

    @Override
    public boolean evaluate() {
        if (_childExpression.evaluate() && !_childLastState) {
            _childLastState = true;
            return true;
        }
        _childLastState = _childExpression.evaluate();
        return false;
    }

    @Override
    public void reset() {
        _childLastState = false;
    }

}
