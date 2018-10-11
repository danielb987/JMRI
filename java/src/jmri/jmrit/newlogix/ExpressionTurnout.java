package jmri.jmrit.newlogix;

import jmri.Expression;
import jmri.NewLogixCategory;
import jmri.implementation.AbstractExpression;

/**
 * Evaluates the state of a Turnout.
 * 
 * @author Daniel Bergqvist 2018
 */
public class ExpressionTurnout extends AbstractExpression {

    public ExpressionTurnout(String sys, String user) throws BadUserNameException, BadSystemNameException {
        super(sys, user);
    }

    @Override
    public NewLogixCategory getCategory() {
        return NewLogixCategory.ITEM;
    }

    @Override
    public boolean evaluate() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void reset() {
        // Do nothing.
    }

}
