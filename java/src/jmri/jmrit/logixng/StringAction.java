package jmri.jmrit.logixng;

import jmri.NamedBean;

/**
 * A LogixNG string action.
 * 
 * @author Daniel Bergqvist Copyright 2019
 */
public interface StringAction extends NamedBean, Base {

    /**
     * Set a string value.
     */
    public void setValue(String value);
    
}
