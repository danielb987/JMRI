package jmri.jmrit.logixng;

import java.util.List;
import jmri.Manager;

/**
 * Manager for LogixNG execution groups.
 * 
 * @author Daniel Bergqvist Copyright 2019
 */
public interface ExecutionGroupManager extends Manager<ExecutionGroup> {

    /**
     * Create a new ExecutionGroup if the ExecutionGroup does not exist.
     *
     * @param systemName the system name
     * @param userName   the user name
     * @return a new ExecutionGroup or null if unable to create
     */
    public ExecutionGroup createExecutionGroup(String systemName, String userName)
            throws IllegalArgumentException;

    /**
     * For use with User GUI, to allow the auto generation of systemNames, where
     * the user can optionally supply a username.
     *
     * @param userName the user name
     * @return a new ExecutionGroup or null if unable to create
     */
    public ExecutionGroup createExecutionGroup(String userName)
            throws IllegalArgumentException;

    /**
     * Locate via user name, then system name if needed. Does not create a new
     * one if nothing found
     *
     * @param name User name or system name to match
     * @return null if no match found
     */
    public ExecutionGroup getExecutionGroup(String name);

    public ExecutionGroup getByUserName(String name);

    public ExecutionGroup getBySystemName(String name);
    
    /**
     * Resolve all the ExecutionGroup trees.
     * <P>
     * This method ensures that everything in the ExecutionGroup tree has a pointer
     * to its parent.
     */
    public void resolveAllTrees();

    /**
     * Activate all ExecutionGroups that are not currently active. This method is
     * called after a configuration file is loaded.
     */
    public void activateAllExecutionGroups();

    /**
     * Delete ExecutionGroup by removing it from the manager. The ExecutionGroup must first
     * be deactivated so it stops processing.
     *
     * @param x the ExecutionGroup to delete
     */
    void deleteExecutionGroup(ExecutionGroup x);

    /**
     * Support for loading ExecutionGroups in a disabled state
     * 
     * @param s true if ExecutionGroup should be disabled when loaded
     */
    public void setLoadDisabled(boolean s);
    
    /**
     * Register a FemaleSocketFactory.
     */
    public void registerFemaleSocketFactory(FemaleSocketFactory factory);
    
    /**
     * Register a FemaleSocketFactory.
     */
    public List<FemaleSocketFactory> getFemaleSocketFactories();
    
}
