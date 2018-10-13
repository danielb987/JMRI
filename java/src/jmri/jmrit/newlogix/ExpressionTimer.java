package jmri.jmrit.newlogix;

import jmri.Expression;
import jmri.NewLogixCategory;
import jmri.implementation.AbstractExpression;

/**
 * This expression is a timer that evaluates to true then a certain time has passed.
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public class ExpressionTimer extends AbstractExpression {

    public ExpressionTimer(String sys, String user) throws BadUserNameException, BadSystemNameException {
        super(sys, user);
    }

    /** {@inheritDoc} */
    @Override
    public NewLogixCategory getCategory() {
        return NewLogixCategory.COMMON;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isExternal() {
        return false;
    }
    
    /** {@inheritDoc} */
    @Override
    public boolean evaluate() {
        // Has timer completed?
        
        return true;    // Mockup code for now.
    }

    /** {@inheritDoc} */
    @Override
    public void reset() {
        // Reset timer.
    }

}
