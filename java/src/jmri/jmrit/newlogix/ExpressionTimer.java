package jmri.jmrit.newlogix;

import jmri.Expression;
import jmri.NewLogixCategory;
import jmri.implementation.AbstractExpression;

/**
 * This expression is a timer that evaluates to true then a certain time has passed.
 * 
 * @author Daniel Bergqvist 2018
 */
public class ExpressionTimer extends AbstractExpression {

    public ExpressionTimer(String sys, String user) throws BadUserNameException, BadSystemNameException {
        super(sys, user);
    }

    @Override
    public NewLogixCategory getCategory() {
        return NewLogixCategory.COMMON;
    }

    @Override
    public boolean evaluate() {
        // Has timer completed?
        
        return true;    // Mockup code for now.
    }

    @Override
    public void reset() {
        // Reset timer.
    }

}
