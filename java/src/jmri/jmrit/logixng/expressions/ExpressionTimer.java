package jmri.jmrit.logixng.expressions;

import jmri.jmrit.logixng.Category;
import jmri.jmrit.logixng.FemaleSocket;

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

    @Override
    public String getShortDescription() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getLongDescription() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
