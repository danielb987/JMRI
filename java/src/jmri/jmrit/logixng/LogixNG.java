package jmri.jmrit.logixng;

import jmri.NamedBean;

/**
 * LogixNG.
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public interface LogixNG extends NamedBean {

    /**
     * Get the female socket of this LogixNG.
     * @return 
     */
    public FemaleSocket getFemaleSocket();
    
    /**
     * Execute the LogixNG.
     * Most of the LogixNG's has a ActionDoIf as its action and it's that
 action that evaluates the expression and decides if it should execute
 its action.
     */
    public void execute();
    
    /**
     * Set enabled status.
     *
     * @param state true if LogixNG should be enabled; false otherwise
     */
    public void setEnabled(boolean state);

    /**
     * Get enabled status.
     *
     * @return true if enabled; false otherwise
     */
    public boolean getEnabled();

}
