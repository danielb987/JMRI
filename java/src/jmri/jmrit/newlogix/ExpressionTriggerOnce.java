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

    private Expression childExpression;
    private boolean childLastState = false;
    
    @Override
    public NewLogixCategory getCategory() {
        return NewLogixCategory.OTHER;
    }

    @Override
    public boolean evaluate() {
        if (childExpression.evaluate() && !childLastState) {
            childLastState = true;
            return true;
        }
        childLastState = childExpression.evaluate();
        return false;
    }

    @Override
    public void reset() {
        childLastState = false;
    }

}
