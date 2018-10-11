package jmri.jmrit.newlogix;

import java.util.List;
import java.util.ArrayList;
import jmri.Expression;
import jmri.NewLogixCategory;
import jmri.implementation.AbstractExpression;

/**
 * Evaluates to True if all the child expressions evaluate to true.
 * 
 * @author Daniel Bergqvist 2018
 */
public class ExpressionAnd extends AbstractExpression {

    List<Expression> children = new ArrayList<>();
    
    public ExpressionAnd(String sys, String user) throws BadUserNameException,
            BadSystemNameException {
        super(sys, user);
    }

    @Override
    public NewLogixCategory getCategory() {
        return NewLogixCategory.COMMON;
    }
    
    @Override
    public boolean evaluate() {
        boolean result = true;
        for (Expression e : children) {
            if (! e.evaluate()) {
                result = false;
            }
        }
        return result;
    }
    
    @Override
    public void reset() {
        for (Expression e : children) {
            e.reset();
        }
    }

}
