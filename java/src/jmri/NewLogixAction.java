package jmri;

/**
 * A NewLogix action.
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public interface NewLogixAction extends NamedBean {

    /**
     * Get the category of this action.
     * @return the category
     */
    public NewLogixCategory getCategory();
    
    /**
     * Is this an external action?
     * Does this action affects on or is dependent on external things, like
     * turnouts and sensors? Timers are considered as internal since they
     * behavies the same on every computer on every layout.
     * @return true if this action is external
     */
    public boolean isExternal();

    /**
     * Start execution of this NewLogixAction.
     * 
     * @return true if this action is not finished.
     */
    public boolean executeStart();
    
    /**
     * Continue execution of this NewLogixAction.
     * This method is called if Type == TRIGGER_ACTION, the previous call to
     * one of the execute???() methods returned True and the expression is
     * still True.
     * 
     * @return true if this action is not finished.
     */
    public boolean executeContinue();
    
    /**
     * Restart the execute of this NewLogixAction.
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
     * This method is called when the NewLogix expression evaluates to False,
     * but the last call to executeStart() returned True.
     */
    public void abort();
    
}
