package jmri.jmrit.newlogix;

import jmri.Manager;

/**
 * Manager for NewLogix
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public interface NewLogixManager extends Manager<NewLogix> {

    /**
     * Create a new NewLogix if the NewLogix does not exist.
     *
     * @param systemName the system name
     * @param userName   the user name
     * @return a new NewLogix or null if unable to create: An error, or the
     * NewLogix already exists
     */
    public NewLogix createNewNewLogix(String systemName, String userName);

    /**
     * For use with User GUI, to allow the auto generation of systemNames, where
     * the user can optionally supply a username.
     *
     * @param userName the user name
     * @return a new NewLogix or null if unable to create
     */
    public NewLogix createNewNewLogix(String userName);

    /**
     * Locate via user name, then system name if needed. Does not create a new
     * one if nothing found
     *
     * @param name User name or system name to match
     * @return null if no match found
     */
    public NewLogix getNewLogix(String name);

    public NewLogix getByUserName(String name);

    public NewLogix getBySystemName(String name);
    
//    public MaleActionSocket createMaleActionSocket(Action action);

//    public MaleExpressionSocket createMaleExpressionSocket(Expression expression);
    
    public FemaleActionSocket createFemaleActionSocket(
            FemaleSocketListener listener, String socketName);

    public FemaleActionSocket createFemaleActionSocket(
            FemaleSocketListener listener, String socketName,
            MaleActionSocket maleSocket);

    public FemaleExpressionSocket createFemaleExpressionSocket(
            FemaleSocketListener listener, String socketName);

    public FemaleExpressionSocket createFemaleExpressionSocket(
            FemaleSocketListener listener, String socketName,
            MaleExpressionSocket maleSocket);

    /**
     * Activate all NewLogixs that are not currently active. This method is
     * called after a configuration file is loaded.
     */
    public void activateAllNewLogixs();

    /**
     * Delete NewLogix by removing it from the manager. The NewLogix must first
     * be deactivated so it stops processing.
     *
     * @param x the NewLogix to delete
     */
    void deleteNewLogix(NewLogix x);

    /**
     * Support for loading NewLogixs in a disabled state
     * 
     * @param s true if NewLogix should be disabled when loaded
     */
    public void setLoadDisabled(boolean s);
    
}
