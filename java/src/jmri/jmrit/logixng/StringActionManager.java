package jmri.jmrit.logixng;

import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;

/**
 * Manager for StringActionBean
 * 
 * @author Dave Duchamp       Copyright (C) 2007
 * @author Daniel Bergqvist   Copyright (C) 2018
 */
public interface StringActionManager extends BaseManager<MaleStringActionSocket> {

    /**
     * Remember a NamedBean Object created outside the manager.
     * This method creates a MaleStringActionSocket for the action.
     *
     * @param action the bean
     * @return the male socket for this action
     * @throws IllegalArgumentException if the action has an invalid system name
     */
    public MaleStringActionSocket registerAction(@Nonnull StringActionBean action)
            throws IllegalArgumentException;
    
    /**
     * Create a new system name for an StringActionBean.
     * @return a new system name
     */
    public String getAutoSystemName();

    public FemaleStringActionSocket createFemaleSocket(
            Base parent, FemaleSocketListener listener, String socketName);

    /**
     * Get a set of classes that implements the DigitalAction interface.
     * 
     * @return a set of entries with category and class
     */
    public Map<Category, List<Class<? extends Base>>> getActionClasses();

    /**
     * Get a class from the description.
     * 
     * @param descr the description
     * @return the class
     */
    public Class<? extends Base> getClassByDescription(String descr);

    /**
     * Get a class from the English description.
     * 
     * @param descr the description
     * @return the class
     */
    public Class<? extends Base> getClassByEnglishDescription(String descr);

    /**
     * {@inheritDoc}
     * 
     * The sub system prefix for the StringActionManager is
     * {@link #getSystemNamePrefix() } and "SA";
     */
    @Override
    public default String getSubSystemNamePrefix() {
        return getSystemNamePrefix() + "SA";
    }

    /**
     * Delete StringAction by removing it from the manager. The Action must first be
     * deactivated so it stops processing.
     *
     * @param x the StringAction to delete
     */
    public void deleteStringAction(MaleStringActionSocket x);
    
}
