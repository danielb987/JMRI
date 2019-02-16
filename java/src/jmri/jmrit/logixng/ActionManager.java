package jmri.jmrit.logixng;

import javax.annotation.Nonnull;
import jmri.Manager;
import jmri.jmrit.logixng.Action;
import jmri.jmrit.logixng.LogixNG;

/**
 * Manager for Action
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public interface ActionManager extends Manager<MaleActionSocket> {

    /**
     * Remember a NamedBean Object created outside the manager.
     * This method creates a MaleActionSocket for the action.
     *
     * @param action the bean
     * @return the male socket for this action
     * @throws IllegalArgumentException if the action has an invalid system name
     */
    public MaleActionSocket register(@Nonnull Action action)
            throws IllegalArgumentException;
    
    /**
     * Create a new system name for an Action.
     *
     * @param newLogix the LogixNG that this expression belongs to
     * @return a new system name
     */
    public String getNewSystemName(LogixNG newLogix);

    public FemaleActionSocket createFemaleActionSocket(
            FemaleSocketListener listener, String socketName);

    public FemaleActionSocket createFemaleActionSocket(
            FemaleSocketListener listener, String socketName,
            MaleActionSocket maleSocket);

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
