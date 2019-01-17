package jmri;

import java.util.Objects;
import javax.annotation.CheckForNull;
import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import jmri.util.com.dictiography.collections.IndexedTreeSet;

/**
 * Interface to get DigitalIO.
 * 
 * This manager cannot create any new DigitalIO. Use the managers for turnouts,
 * sensors, and so on, for that.
 */
public interface DigitalIOManager extends ProvidingManager<DigitalIO> {

    /**
     * Get the DigitalIO with the user name, then system name if needed; if
     * that fails, throws IllegalArgumentException since a DigitalIO cannot be
     * created directly. Create a turnout, sensor, etc, instead.
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
    /**
     * {@inheritDoc}
     * 
     * This method throws IllegalArgumentException if the NamedBean does not
     * exists since a DigitalIO cannot be created directly. Create a turnout,
     * sensor, etc, instead.
     */
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

    /**
     * Return a DigitalIO with the specified system and user names. 
     * Note that
     * two calls with the same arguments will get the same instance; there is
     * only one DigitalIO object representing a given physical digitalio and
     * therefore only one with a specific system or user name.
     * <P>
     * This method throws IllegalArgumentException if the NamedBean does not
     * exists since a DigitalIO cannot be created directly. Create a turnout,
     * sensor, etc, instead.
     * <P>
     * Note that it is possible to make an inconsistent request if both
     * addresses are provided, but the given values are associated with
     * different objects. This is a problem, and we don't have a good solution
     * except to issue warnings. This will mostly happen if you're creating
     * DigitalIO when you should be looking them up.
     *
     * @param systemName the desired system name
     * @param userName   the desired user name
     * @return requested Sensor object
     * @throws IllegalArgumentException if cannot create the Sensor due to e.g.
     *                                  an illegal name or name that can't be
     *                                  parsed.
     */
    @Nonnull
    public DigitalIO newDigitalIO(@Nonnull String systemName, @CheckForNull String userName)
            throws IllegalArgumentException;

    /** {@inheritDoc} */
    @CheckReturnValue
    @CheckForNull
    public DigitalIO getByUserName(@Nonnull String s);

    /** {@inheritDoc} */
    @CheckReturnValue
    @CheckForNull
    public Sensor getBySystemName(@Nonnull String s);

    /**
     * Requests status of all layout sensors under this DigitalIO Manager. This
     * method may be invoked whenever the status of digitalio:s needs to be updated
     * from the layout, for example, when an XML configuration file is read in.
     * 
     * Note that there is no guarantee that the digitalio:s are actually updated.
     */
    public void updateAll();

    public void addManager(ProvidingManager<? extends DigitalIO> m);

}
