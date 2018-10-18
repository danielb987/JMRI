package jmri.jmrit.newlogix.internal;

import jmri.NewLogixCategory;
import jmri.implementation.AbstractExpression;
import jmri.NewLogixExpression;

/**
 * Every NewLogixExpression has an InternalExpression as its parent.
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public class InternalExpression extends AbstractExpression {

    private final NewLogixExpression _expression;
    private boolean lastEvaluationResult = false;
    
    public InternalExpression(String sys, NewLogixExpression expression)
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
        lastEvaluationResult = _expression.evaluate();
        return lastEvaluationResult;
    }

    /** {@inheritDoc} */
    @Override
    public void reset() {
        _expression.reset();
    }

    @Override
    public int getState() {
        return lastEvaluationResult ? NewLogixExpression.TRUE : NewLogixExpression.FALSE;
    }

}
