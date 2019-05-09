package jmri.jmrit.logixng.implementation;

import jmri.Manager;
import jmri.NamedBean;
import jmri.beans.PropertyChangeProvider;
import jmri.beans.VetoableChangeProvider;

/**
 * A namespace has a set of names
 */
public interface Namespace extends PropertyChangeProvider, VetoableChangeProvider {

    /**
     * Register a name in the namespace.
     * This is not possible for the JMRI native namespace, which can only handle
     * named beans.
     * 
     * @param <M> The type of the Manager, for example TurnoutManager
     * @param namespace the namespace
     * @param type the class of the manager, for example TurnoutManager.class
     * @param name the system name or the user name of the bean
     * @throws UnsupportedOperationException if the namespace cannot register the name
     */
    public <M extends Manager> void registerName(
            Namespace namespace, Class<M> type, String name)
            throws UnsupportedOperationException;
    
    /**
     * Get a named bean of the type N.
     * 
     * @param <M> The type of the Manager, for example TurnoutManager
     * @param <N> The type of the NamedBean, for example Turnout
     * @param namespace the namespace
     * @param type the class of the manager, for example TurnoutManager.class
     * @param clazz the class of the named bean, for example Turnout.class
     * @param name the system name or the user name of the bean
     * @return the bean or null if it doesn't exists
     */
    public <M extends Manager, N extends NamedBean> N get(
            Namespace namespace, Class<M> type, Class<N> clazz, String name);
    
    /**
     * Provides a named bean of the type N.
     * 
     * @param <M> The type of the Manager, for example TurnoutManager
     * @param <N> The type of the NamedBean, for example Turnout
     * @param namespace the namespace
     * @param type The class of the manager, for example TurnoutManager.class
     * @param clazz the class of the named bean, for example Turnout.class
     * @param name the system name or the user name of the bean
     * @return the bean or null if it doesn't exists
     */
    public <M extends Manager, N extends NamedBean> N provide(
            Namespace namespace, Class<M> type, Class<N> clazz, String name);

}
