package jmri.jmrit.logixng;

import jmri.NamedBean;

/**
 * A LogixNG action.
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public interface Action extends NamedBean, Base {

    /**
     * Start execution of this Action.
     * 
     * @return true if this action is not finished.
     */
    public boolean executeStart();
    
    /**
     * Continue execution of this Action if it last time returned true.
     * This method is called if Type == TRIGGER_ACTION, the previous call to
     * one of the execute???() methods returned True and the expression is
     * still True.
     * 
     * @return true if this action is not finished.
     */
    public boolean executeContinue();
    
    /**
     * Restart the execute of this Action.
     * This method is called if Type == TRIGGER_ACTION and the expression has
     * become False and then True again.
     * 
     * If a parent action is restarted, it must restart all its children.
     * 
     * @return true if this action is not finished.
     */
    public boolean executeRestart();
    
    /**
     * Abort this action.
     * This method is called when the LogixNG expression evaluates to False,
     * but the last call to executeStart() returned True.
     */
    public void abort();
    
}
