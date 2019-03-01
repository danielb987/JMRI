package jmri.jmrit.logixng;

import javax.annotation.Nonnull;
import jmri.Manager;
import jmri.jmrit.logixng.LogixNG;
import jmri.jmrit.logixng.DigitalAction;

/**
 * Manager for DigitalAction
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public interface DigitalActionManager extends Manager<MaleDigitalActionSocket> {

    /**
     * Remember a NamedBean Object created outside the manager.
     * This method creates a MaleDigitalActionSocket for the action.
     *
     * @param action the bean
     * @return the male socket for this action
     * @throws IllegalArgumentException if the action has an invalid system name
     */
    public MaleDigitalActionSocket register(@Nonnull DigitalAction action)
            throws IllegalArgumentException;
    
    /**
     * Create a new system name for an DigitalAction.
     *
     * @param newLogix the LogixNG that this expression belongs to
     * @return a new system name
     */
    public String getNewSystemName(LogixNG newLogix);

    public FemaleDigitalActionSocket createFemaleActionSocket(
            FemaleSocketListener listener, String socketName);

    public FemaleDigitalActionSocket createFemaleActionSocket(
            FemaleSocketListener listener, String socketName,
            MaleDigitalActionSocket maleSocket);

    /*.*
     * Add an DigitalAction.
     *
     * @param action the action to add
     * @throws IllegalArgumentException if the action has an invalid system name
     */
//    public void addAction(DigitalAction action)
//            throws IllegalArgumentException;

    /*.*
     * Locate via user name, then system name if needed. Does not create a new
     * one if nothing found
     *
     * @param name User name or system name to match
     * @return null if no match found
     */
//    public DigitalAction getAction(String name);

//    public DigitalAction getByUserName(String s);

//    public DigitalAction getBySystemName(String s);

    /*.*
     * Delete DigitalAction by removing it from the manager. The DigitalAction must first be
     * deactivated so it stops processing.
     *
     * @param x the DigitalAction to delete
     */
//    void deleteAction(DigitalAction x);
    
}
