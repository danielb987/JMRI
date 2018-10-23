package jmri.jmrit.newlogix;

import java.util.List;
import java.util.ArrayList;
import jmri.NewLogixCategory;
import jmri.implementation.AbstractExpression;
import jmri.NewLogixExpression;

/**
 * Evaluates to True if all the child expressions evaluate to true.
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public class ExpressionAnd extends AbstractExpression {

    List<NewLogixExpression> children = new ArrayList<>();
    
    public ExpressionAnd(String sys) throws BadUserNameException,
            BadSystemNameException {
        super(sys);
    }

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
        for (NewLogixExpression e : children) {
            if (! e.evaluate()) {
                result = false;
            }
        }
        return result;
    }
    
    /** {@inheritDoc} */
    @Override
    public void reset() {
        for (NewLogixExpression e : children) {
            e.reset();
        }
    }

    @Override
    public NewLogixSocket getChild(int index) throws IllegalArgumentException, UnsupportedOperationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getChildCount() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
