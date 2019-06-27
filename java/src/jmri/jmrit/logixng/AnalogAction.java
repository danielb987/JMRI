package jmri.jmrit.logixng;

import jmri.NamedBean;

/**
 * A LogixNG analog action.
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public interface AnalogAction extends NamedBean, Base {

    /**
     * Set an analog value.
     * 
     * @param value the value. The male socket that holds this action ensures
     * that this value is not Double.NaN or an infinite value.
     */
    public void setValue(double value);
    
}
