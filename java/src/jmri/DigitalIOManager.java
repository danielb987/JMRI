package jmri;

import javax.annotation.CheckForNull;
import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;

/**
 * Interface for controlling sensors.
 *
 * <hr>
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
 * @author Daniel Bergqvist Copyright (C) 2018
 */
public interface DigitalIOManager extends ProvidingManager<DigitalIO> {

    /**
     * Get the DigitalIO with the user name, then system name if needed; if that
     * fails, throws IllegalArgumentException since a DigitalIO cannot be created
     * directly. Create a turnout, sensor, etc, instead.
     *
     * @param name User name, system name, or address which can be promoted to
     *             system name
     * @return Never null
     * @throws IllegalArgumentException if DigitalIO doesn't already exist
     */
    @Nonnull
    public DigitalIO provideDigitalIO(@Nonnull String name)
            throws IllegalArgumentException;

    @Override
    /** {@inheritDoc} */
    default public DigitalIO provide(@Nonnull String name)
            throws IllegalArgumentException {
        return provideDigitalIO(name);
    }

    /**
     * Get an existing DigitalIO or return null if it doesn't exist. 
     * 
     * Locates via user name, then system name if needed.
     *
     * @param name User name or system name to match
     * @return null if no match found
     */
    @CheckReturnValue
    @CheckForNull
    public DigitalIO getDigitalIO(@Nonnull String name);

    // to free resources when no longer used
    @Override
    public void dispose();

    /** {@inheritDoc} */
    @CheckReturnValue
    @CheckForNull
    public DigitalIO getByUserName(@Nonnull String s);

    /** {@inheritDoc} */
    @CheckReturnValue
    @CheckForNull
    public DigitalIO getBySystemName(@Nonnull String s);

}
