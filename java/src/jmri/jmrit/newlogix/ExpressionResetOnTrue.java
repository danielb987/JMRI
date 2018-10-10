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

    private Expression primaryExpression;
    private Expression secondaryExpression;
    private boolean lastMainResult = false;
    
    @Override
    public NewLogixCategory getCategory() {
        return NewLogixCategory.OTHER;
    }

    @Override
    public boolean evaluate() {
        boolean result = primaryExpression.evaluate();
        if (!lastMainResult && result) {
            secondaryExpression.reset();
        }
        lastMainResult = result;
        result |= secondaryExpression.evaluate();
        return result;
    }

    @Override
    public void reset() {
        primaryExpression.reset();
        secondaryExpression.reset();
    }

}
