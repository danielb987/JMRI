package jmri.jmrit.logixng;

import jmri.NamedBean;

/**
 * ConditionalNG.
 * 
 * @author Daniel Bergqvist Copyright 2019
 */
public interface ConditionalNG extends Base, NamedBean {

    /**
     * Get the female socket of this ConditionalNG.
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
