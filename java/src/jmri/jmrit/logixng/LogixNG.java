package jmri.jmrit.logixng;

import jmri.NamedBean;

/**
 * LogixNG.
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public interface LogixNG extends Base, NamedBean {

    /**
     * Get the female socket of this LogixNG.
     */
    public FemaleSocket getFemaleSocket();
    
    /**
     * Execute the LogixNG.
     * Most of the LogixNG's has a ActionDoIf as its action and it's that
     * action that evaluates the expression and decides if it should execute
     * its action.
     */
    public void execute();

}
