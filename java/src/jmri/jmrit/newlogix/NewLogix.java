package jmri.jmrit.newlogix;

import jmri.NamedBean;

/**
 * NewLogix.
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public interface NewLogix extends NamedBean {

    /**
     * Get the female socket of this NewLogix.
     * @return 
     */
    public FemaleSocket getFemaleSocket();
    
    /**
     * Execute the NewLogix.
     * Most of the NewLogix's has a ActionDoIf as its action and it's that
     * action that evaluates the expression and decides if it should execute
     * its action.
     */
    public void execute();
    
    /**
     * Set enabled status.
     *
     * @param state true if NewLogix should be enabled; false otherwise
     */
    public void setEnabled(boolean state);

    /**
     * Get enabled status.
     *
     * @return true if enabled; false otherwise
     */
    public boolean getEnabled();

}
