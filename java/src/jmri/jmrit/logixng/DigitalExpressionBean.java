package jmri.jmrit.logixng;

import jmri.NamedBean;

/**
 * DigitalExpressionBean is used in LogixNG to answer a question that can give the
 answers 'true' or 'false'.
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public interface DigitalExpressionBean extends NamedBean, DigitalExpression {
    
    /**
     * Constant representing an "false" state. It's returned from the method
     * getState() if the method evaluate() returned false the last time it was
     * called.
     */
    public static final int FALSE = 0x02;

    /**
     * Constant representing an "false" state. It's returned from the method
     * getState() if the method evaluate() returned false the last time it was
     * called.
     */
    public static final int TRUE = 0x04;
    
}
