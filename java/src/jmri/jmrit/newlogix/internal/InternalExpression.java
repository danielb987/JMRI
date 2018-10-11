package jmri.jmrit.newlogix.internal;

import jmri.Expression;
import jmri.NewLogixCategory;
import jmri.implementation.AbstractExpression;

/**
 * Every Expression has an InternalExpression as its parent.
 * 
 * @author Daniel Bergqvist 2018
 */
public class InternalExpression extends AbstractExpression {

    private final Expression _expression;
    
    public InternalExpression(String sys, Expression expression)
            throws BadUserNameException, BadSystemNameException {
        
        super(sys);
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
