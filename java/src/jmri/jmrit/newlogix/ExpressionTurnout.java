package jmri.jmrit.newlogix;

import jmri.Expression;
import jmri.NewLogixCategory;

/**
 * Evaluates the state of a Turnout.
 * 
 * @author Daniel Bergqvist 2018
 */
public class ExpressionTurnout implements Expression {

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
