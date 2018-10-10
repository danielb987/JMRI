package jmri.jmrit.newlogix.internal;

import jmri.Expression;
import jmri.NewLogixCategory;

/**
 * Every Expression has an InternalExpression as its parent.
 * 
 * @author Daniel Bergqvist 2018
 */
public class InternalExpression implements Expression {

    private final Expression _expression;
    
    public InternalExpression(Expression expression) {
        _expression = expression;
    }
    
    @Override
    public NewLogixCategory getCategory() {
        return _expression.getCategory();
    }

    @Override
    public boolean evaluate() {
        return _expression.evaluate();
    }

    @Override
    public void reset() {
        _expression.reset();
    }

}
