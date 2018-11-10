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
    
    /**
     * Reset the evaluation.
     * This method is called when the closest ancestor Action is activated.
     * 
     * A parent expression must to call reset() on its child when the parent
     * is reset().
     */
    public void reset();
    
}
