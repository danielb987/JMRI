package jmri.jmrit.newlogix;

import jmri.NamedBean;

/**
 * A NewLogix analog action.
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public interface AnalogAction extends NamedBean, Base {

    /**
     * Start execution of this Action.
     * 
     * @param value the analog value
     * @return true if this action is not finished.
     */
    public boolean executeStart(float value);
    
    /**
     * Continue execution of this Action.
     * This method is called if Type == TRIGGER_ACTION, the previous call to
     * one of the execute???() methods returned True and the expression is
     * still True.
     * 
     * @param value the analog value
     * @return true if this action is not finished.
     */
    public boolean executeContinue(float value);
    
    /**
     * Restart the execute of this Action.
     * This method is called if Type == TRIGGER_ACTION and the expression has
     * become False and then True again.
     * 
     * If a parent action is restarted, it must restart all its children.
     * 
     * @param value the analog value
     * @return true if this action is not finished.
     */
    public boolean executeRestart(float value);
    
    /**
     * Abort this action.
     * This method is called when the NewLogix expression evaluates to False,
     * but the last call to executeStart() returned True.
     */
    public void abort();
    
}
