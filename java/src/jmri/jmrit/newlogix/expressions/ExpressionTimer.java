package jmri.jmrit.newlogix.expressions;

import jmri.jmrit.newlogix.Category;
import jmri.jmrit.newlogix.Expression;
import jmri.jmrit.newlogix.FemaleSocket;

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
    public Category getCategory() {
        return Category.COMMON;
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

    @Override
    public FemaleSocket getChild(int index) throws IllegalArgumentException, UnsupportedOperationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getChildCount() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
