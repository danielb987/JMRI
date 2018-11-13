package jmri.jmrit.newlogix;

import jmri.NamedBean;

/**
 * Analog expression is used in NewLogix to answer a question that can give
 * an analog value as result.
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public interface AnalogExpression extends NamedBean, Base {
    
    /**
     * Evaluate this expression.
     * 
     * @return the result of the evaluation
     */
    public float evaluate();
    
}
