package jmri.jmrit.logixng;

import javax.annotation.Nonnull;
import jmri.Manager;

/**
 * Manager for StringAction
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public interface StringActionManager extends Manager<MaleStringActionSocket> {

    /**
     * Remember a NamedBean Object created outside the manager.
     * This method creates a MaleStringActionSocket for the action.
     *
     * @param action the bean
     * @return the male socket for this action
     * @throws IllegalArgumentException if the action has an invalid system name
     */
    public MaleStringActionSocket register(@Nonnull StringAction action)
            throws IllegalArgumentException;
    
    /**
     * Create a new system name for an StringAction.
     *
     * @param newLogix the LogixNG that this analog action belongs to
     * @return a new system name
     */
    public String getNewSystemName(LogixNG newLogix);

    public FemaleStringActionSocket createFemaleStringActionSocket(
            FemaleSocketListener listener, String socketName);

    public FemaleStringActionSocket createFemaleStringActionSocket(
            FemaleSocketListener listener, String socketName,
            MaleStringActionSocket maleSocket);

    /*.*
     * Add an Action.
     *
     * @param action the action to add
     * @throws IllegalArgumentException if the action has an invalid system name
     */
//    public void addAction(Action action)
//            throws IllegalArgumentException;

    /*.*
     * Locate via user name, then system name if needed. Does not create a new
     * one if nothing found
     *
     * @param name User name or system name to match
     * @return null if no match found
     */
//    public Action getAction(String name);

//    public Action getByUserName(String s);

//    public Action getBySystemName(String s);

    /*.*
     * Delete Action by removing it from the manager. The Action must first be
     * deactivated so it stops processing.
     *
     * @param x the Action to delete
     */
//    void deleteAction(Action x);
    
}
