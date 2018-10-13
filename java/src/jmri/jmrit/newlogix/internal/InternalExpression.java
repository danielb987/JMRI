package jmri.jmrit.newlogix.internal;

import jmri.Expression;
import jmri.NewLogixCategory;
import jmri.implementation.AbstractExpression;

/**
 * Every Expression has an InternalExpression as its parent.
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public class InternalExpression extends AbstractExpression {

    private final Expression _expression;
    
    public InternalExpression(String sys, Expression expression)
            throws BadUserNameException, BadSystemNameException {
        
        super(sys);
        _expression = expression;
    }

    /** {@inheritDoc} */
    @Override
    public NewLogixCategory getCategory() {
        return _expression.getCategory();
    }

    /** {@inheritDoc} */
    @Override
    public boolean isExternal() {
        return false;
    }
    
    /** {@inheritDoc} */
    @Override
    public boolean evaluate() {
        return _expression.evaluate();
    }

    /** {@inheritDoc} */
    @Override
    public void reset() {
        _expression.reset();
    }

}
