package jmri.jmrit.logixng;

import jmri.Manager;
import jmri.NamedBean;

/**
 * Instance manager for LogixNG, which allows LogixNG to use templates.
 */
public interface LogixNG_InstanceManager {

    public enum TemplateType {
        /**
         * This object is not a template and is not based on a template.
         */
        STANDALONE,
        
        /**
         * This object is a template.
         */
        TEMPLATE,
        
        /**
         * This object is based on a template.
         */
        BASED_ON_TEMPLATE,
    }
    
    
    /**
     * Get a named bean of the type N.
     * 
     * @param <M> The type of the Manager, for example TurnoutManager
     * @param <N> The type of the NamedBean, for example Turnout
     * @param type The class of the manager, for example TurnoutManager.class
     * @param clazz the class of the named bean, for example Turnout.class
     * @param name the system name or the user name of the bean
     * @return the bean or null if it doesn't exists
     */
    public <M extends Manager, N extends NamedBean> N get(
            Class<M> type, Class<N> clazz, String name);
    
    /**
     * Provides a named bean of the type N.
     * 
     * @param <M> The type of the Manager, for example TurnoutManager
     * @param <N> The type of the NamedBean, for example Turnout
     * @param type The class of the manager, for example TurnoutManager.class
     * @param clazz the class of the named bean, for example Turnout.class
     * @param name the system name or the user name of the bean
     * @return the bean or null if it doesn't exists
     */
    public <M extends Manager, N extends NamedBean> N provide(
            Class<M> type, Class<N> clazz, String name);
    
}
