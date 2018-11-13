package jmri.jmrit.newlogix;

import jmri.NamedBean;

/**
 * A NewLogix analog action.
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public interface AnalogAction extends NamedBean, Base {

    /**
     * Set an analog value.
     */
    public void setValue(float value);
    
}
