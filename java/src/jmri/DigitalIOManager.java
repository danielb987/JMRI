package jmri;

import java.util.List;
import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Locate a DigitalIO object representing some specific digitalIO on the layout.
 * <P>
 * DigitalIO objects are obtained from a DigitalIOManager, which in turn is
 * generally located from the InstanceManager. A typical call sequence might be:
 * <PRE>
 * DigitalIO digitalIO = InstanceManager.digitalIOManagerInstance().provideDigitalIO("23");
 * </PRE>
 * <P>
 * Each digitalIO has a two names. The "user" name is entirely free form, and can
 * be used for any purpose. The "system" name is provided by the system-specific
 * implementations, and provides a unique mapping to the layout control system
 * (for example LocoNet or NCE) and address within that system.
 * <P>
 * Much of the book-keeping is implemented in the AbstractDigitalIOManager class,
 * which can form the basis for a system-specific implementation.
 * <P>
 * A sample use of the DigitalIOManager interface can be seen in the
 * jmri.jmrit.simpleturnoutctrl.SimpleDigitalIOCtrlFrame class, which provides a
 * simple GUI for controlling a single digitalIO.
 *
 * <P>
 * This file is part of JMRI.
 * <P>
 * JMRI is free software; you can redistribute it and/or modify it under the
 * terms of version 2 of the GNU General Public License as published by the Free
 * Software Foundation. See the "COPYING" file for a copy of this license.
 * <P>
 * JMRI is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * @author Bob Jacobsen Copyright (C) 2001
 * @see jmri.DigitalIO
 * @see jmri.InstanceManager
 * @see jmri.jmrit.simpleturnoutctrl.SimpleDigitalIOCtrlFrame
 */
public interface DigitalIOManager extends ProvidingManager<DigitalIO> {

    /**
     * Get the DigitalIO with the user name, then system name if needed; if that fails, create a
     * new DigitalIO. 
     * If the name is a valid system name, it will be used for the new DigitalIO.
     * Otherwise, the {@link Manager#makeSystemName} method will attempt to turn it
     * into a valid system name.
     * <p>This provides the same function as {@link ProvidingManager#provide}
     * which has a more generic form.
     *
     * @param name User name, system name, or address which can be promoted to
     *             system name
     * @return Never null
     * @throws IllegalArgumentException if DigitalIO doesn't already exist and the
     *                                  manager cannot create the DigitalIO due to
     *                                  an illegal name or name that can't
     *                                  be parsed.
     */
    @Nonnull
    public DigitalIO provideDigitalIO(@Nonnull String name) throws IllegalArgumentException;

    /** {@inheritDoc} */
    @Override
    default public DigitalIO provide(@Nonnull String name) throws IllegalArgumentException { return provideDigitalIO(name); }
    
    /**
     * Get an existing DigitalIO or return null if it doesn't exist. 
     * 
     * Locates via user name, then system name if needed.
     *
     * @param name User name or system name to match
     * @return null if no match found
     */
    @CheckForNull
    public DigitalIO getDigitalIO(@Nonnull String name);

    /**
     * Get the DigitalIO with the given system name or return null if no instance
     * already exists.
     *
     * @param systemName the system name
     * @return requested DigitalIO object or null if none exists
     */
    @CheckForNull
    public DigitalIO getBySystemName(@Nonnull String systemName);

    /**
     * Get the DigitalIO with the given user name or return null if no instance
     * already exists.
     *
     * @param userName the user name
     * @return requested DigitalIO object or null if none exists
     */
    @CheckForNull
    public DigitalIO getByUserName(@Nonnull String userName);

    /**
     * Return a DigitalIO with the specified system and user names. 
     * Note that
     * two calls with the same arguments will get the same instance; there is
     * only one DigitalIO object representing a given physical digitalIO and
     * therefore only one with a specific system or user name.
     * <P>
     * This will always return a valid object reference; a new object will be
     * created if necessary. In that case:
     * <UL>
     * <LI>If a null reference is given for user name, no user name will be
     * associated with the DigitalIO object created; a valid system name must be
     * provided
     * <LI>If both names are provided, the system name defines the hardware
     * access of the desired digitalIO, and the user address is associated with
     * it. The system name must be valid.
     * </UL>
     * Note that it is possible to make an inconsistent request if both
     * addresses are provided, but the given values are associated with
     * different objects. This is a problem, and we don't have a good solution
     * except to issue warnings. This will mostly happen if you're creating
     * DigitalIOs when you should be looking them up.
     *
     * @param systemName the system name
     * @param userName   the user name (optional)
     * @return requested DigitalIO object, newly created if needed
     * @throws IllegalArgumentException if cannot create the DigitalIO; likely due
     *                                  to an illegal name or name that cannot
     *                                  be parsed
     */
    @Nonnull
    public DigitalIO newDigitalIO(@Nonnull String systemName, @Nullable String userName) throws IllegalArgumentException;

    /**
     * A method that determines if it is possible to add a range of digitalIOs in
     * numerical order.
     *
     * @param systemName the starting digitalIO system name; ignored in all known
     *                   implementations
     * @return true if a range of digitalIOs can be added; false otherwise
     */
    public boolean allowMultipleAdditions(@Nonnull String systemName);

    /**
     * Determine if the address supplied is valid and free, if not then it shall
     * return the next free valid address up to a maximum of 10 addresses away
     * from the initial address. Used when adding add a range of DigitalIOs.
     *
     * @param prefix     System prefix used in system name
     * @param curAddress desired hardware address
     * @return the next available address or null if none available
     * @throws jmri.JmriException if unable to provide a digitalIO at the desired
     *                            address due to invalid format for the current
     *                            address or other reasons (some implementations
     *                            do not throw an error, but notify the user via
     *                            other means and return null)
     */
    @CheckForNull
    public String getNextValidAddress(@Nonnull String curAddress, @Nonnull String prefix) throws JmriException;

    /**
     * Get a system name for a given hardware address and system prefix.
     *
     * @param curAddress desired hardware address
     * @param prefix     system prefix used in system name
     * @return the complete digitalIO system name for the prefix and current
     *         address
     * @throws jmri.JmriException if unable to create a system name for the
     *                            given address, possibly due to invalid address
     *                            format
     */
    public String createSystemName(@Nonnull String curAddress, @Nonnull String prefix) throws JmriException;

}
