package jmri.jmrit.logixng;

import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;

/**
 * Manager for DigitalExpressionBean
 * 
 * @author Dave Duchamp       Copyright (C) 2007
 * @author Daniel Bergqvist   Copyright (C) 2018
 */
public interface DigitalExpressionManager extends BaseManager<MaleDigitalExpressionSocket> {

    /**
     * Remember a NamedBean Object created outside the manager.
     * This method creates a MaleActionSocket for the action.
     *
     * @param expression the bean
     * @return the male socket for this expression
     * @throws IllegalArgumentException if the expression has an invalid system name
     */
    public MaleDigitalExpressionSocket registerExpression(@Nonnull DigitalExpressionBean expression)
            throws IllegalArgumentException;
    
    /**
     * Create a new system name for an DigitalExpressionBean.
     * @return a new system name
     */
    public String getAutoSystemName();

    public FemaleDigitalExpressionSocket createFemaleSocket(
            Base parent, FemaleSocketListener listener, String socketName);

    /**
     * Get a set of classes that implements the DigitalAction interface.
     * 
     * @return a set of entries with category and class
     */
    public Map<Category, List<Class<? extends Base>>> getExpressionClasses();

    /**
     * Get a class from the description.
     * 
     * @param descr the description
     * @return the class
     */
    public Class<? extends Base> getClassByDescription(String descr);

    /**
     * {@inheritDoc}
     * 
     * The sub system prefix for the DigitalExpressionManager is
     * {@link #getSystemNamePrefix() } and "DE";
     */
    @Override
    default public String getSubSystemNamePrefix() {
        return getSystemNamePrefix() + "DE";
    }

    /**
     * Delete DigitalExpression by removing it from the manager. The Expression must
     * first be deactivated so it stops processing.
     *
     * @param x the DigitalExpression to delete
     */
    public void deleteDigitalExpression(MaleDigitalExpressionSocket x);

}
