package jmri;

/**
 * Manager for NewLogixAction
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public interface ActionManager extends Manager<NewLogixAction> {

    /**
     * Create a new system name for an NewLogixAction.
     *
     * @param newLogix the NewLogix that this expression belongs to
     * @return a new system name
     */
    public String getNewSystemName(NewLogix newLogix);

    /**
     * Add an NewLogixAction.
     *
     * @param action the action to add
     * @throws IllegalArgumentException if the action has an invalid system name
     */
    public void addAction(NewLogixAction action)
            throws IllegalArgumentException;

    /**
     * Locate via user name, then system name if needed. Does not create a new
     * one if nothing found
     *
     * @param name User name or system name to match
     * @return null if no match found
     */
    public NewLogixAction getAction(String name);

    public NewLogixAction getByUserName(String s);

    public NewLogixAction getBySystemName(String s);

    /**
     * Delete NewLogixAction by removing it from the manager. The NewLogixAction must
 first be deactivated so it stops processing.
     *
     * @param x the NewLogixAction to delete
     */
    void deleteAction(NewLogixAction x);
    
}
