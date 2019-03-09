package jmri.jmrit.logixng;

import jmri.NamedBean;

/**
 * Analog expression is used in LogixNG to answer a question that can give
 * an analog value as result.
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public interface AnalogExpression extends NamedBean, Base {
    
    /**
     * Evaluate this expression.
     * 
     * @return the result of the evaluation. The male socket that holds this
     * expression throws an exception if this value is a Float.NaN or an
     * infinite number.
     */
    public float evaluate();
    
}
