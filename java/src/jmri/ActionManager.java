package jmri;

/**
 * Manager for Action
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public interface ActionManager extends Manager<Action> {

    /**
     * Create a new system name for an Action.
     *
     * @param newLogix the NewLogix that this expression belongs to
     * @return a new system name
     */
    public String getNewSystemName(NewLogix newLogix);

    /**
     * Add an Action.
     *
     * @param action the action to add
     * @throws IllegalArgumentException if the action has an invalid system name
     */
    public void addAction(Action action)
            throws IllegalArgumentException;

    /**
     * Locate via user name, then system name if needed. Does not create a new
     * one if nothing found
     *
     * @param name User name or system name to match
     * @return null if no match found
     */
    public Action getAction(String name);

    public Action getByUserName(String s);

    public Action getBySystemName(String s);

    /**
     * Delete Action by removing it from the manager. The Action must
     * first be deactivated so it stops processing.
     *
     * @param x the Action to delete
     */
    void deleteAction(Action x);
    
}
