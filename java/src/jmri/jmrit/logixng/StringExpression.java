package jmri.jmrit.logixng;

import jmri.NamedBean;

/**
 * String expression is used in LogixNG to answer a question that can give
 * a string value as result.
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public interface StringExpression extends NamedBean, Base {
    
    /**
     * Evaluate this expression.
     * 
     * @return the result of the evaluation
     */
    public String evaluate();
    
}
