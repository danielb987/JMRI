package jmri.jmrit.newlogix;

import jmri.Expression;
import jmri.NewLogixCategory;
import jmri.implementation.AbstractExpression;

/**
 * Evaluates the state of a Turnout.
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public class ExpressionTurnout extends AbstractExpression {

    public ExpressionTurnout(String sys, String user) throws BadUserNameException, BadSystemNameException {
        super(sys, user);
    }

    /** {@inheritDoc} */
    @Override
    public NewLogixCategory getCategory() {
        return NewLogixCategory.ITEM;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isExternal() {
        return true;
    }
    
    /** {@inheritDoc} */
    @Override
    public boolean evaluate() {
        // Do this on the correct thread??
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /** {@inheritDoc} */
    @Override
    public void reset() {
        // Do nothing.
    }

}
