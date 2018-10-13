package jmri;

/**
 * Manager for Action
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public interface ActionManager extends Manager<Action> {

    /**
     * Create a new Action if the Action does not exist.
     *
     * @param systemName the system name
     * @param userName   the user name
     * @return a new Action or null if unable to create: An error, or the
     * Action already exists
     */
    public Action createNewAction(String systemName, String userName);

    /**
     * For use with User GUI, to allow the auto generation of systemNames, where
     * the user can optionally supply a username.
     *
     * @param userName the user name
     * @return a new NewLogix or null if unable to create
     */
    public Action createNewExpression(String userName);

    /**
     * Locate via user name, then system name if needed. Does not create a new
     * one if nothing found
     *
     * @param name User name or system name to match
     * @return null if no match found
     */
    public Action getExpression(String name);

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
