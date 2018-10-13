package jmri.jmrit.newlogix;

import java.util.List;
import java.util.ArrayList;
import jmri.Expression;
import jmri.NewLogixCategory;
import jmri.implementation.AbstractExpression;

/**
 * Evaluates to True if all the child expressions evaluate to true.
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public class ExpressionAnd extends AbstractExpression {

    List<Expression> children = new ArrayList<>();
    
    public ExpressionAnd(String sys, String user) throws BadUserNameException,
            BadSystemNameException {
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
        boolean result = true;
        for (Expression e : children) {
            if (! e.evaluate()) {
                result = false;
            }
        }
        return result;
    }
    
    /** {@inheritDoc} */
    @Override
    public void reset() {
        for (Expression e : children) {
            e.reset();
        }
    }

}
