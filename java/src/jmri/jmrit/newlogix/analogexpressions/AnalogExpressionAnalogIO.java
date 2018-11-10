package jmri.jmrit.newlogix.analogexpressions;

import java.util.List;
import java.util.ArrayList;
import jmri.jmrit.newlogix.Category;
import jmri.jmrit.newlogix.expressions.AbstractExpression;
import jmri.jmrit.newlogix.Expression;
import jmri.jmrit.newlogix.FemaleSocket;

/**
 * Evaluates to True if all the child expressions evaluate to true.
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public class AnalogExpressionAnalogIO extends AbstractExpression {

    List<Expression> children = new ArrayList<>();
    
    public AnalogExpressionAnalogIO(String sys) throws BadUserNameException,
            BadSystemNameException {
        super(sys);
    }

    public AnalogExpressionAnalogIO(String sys, String user) throws BadUserNameException,
            BadSystemNameException {
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

    @Override
    public FemaleSocket getChild(int index) throws IllegalArgumentException, UnsupportedOperationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getChildCount() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
