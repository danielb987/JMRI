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
     */
    public void setValue(float value);
    
}
