package jmri.jmrit.logixng.swing;

import javax.annotation.Nonnull;
import javax.swing.JPanel;
import jmri.jmrit.logixng.Base;
import jmri.jmrit.logixng.MaleSocket;

/**
 * The parent interface for configuring classes with Swing.
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public interface SwingConfiguratorInterface {

    /**
     * Get a configuration panel when a new object is to be created and we don't
     * have it yet.
     * This method initializes the panel with an empty configuration.
     * 
     * @return a panel that configures this object
     * @throws IllegalArgumentException if this class does not support the class
     * with the name given in parameter 'className'
     */
    public JPanel getConfigPanel() throws IllegalArgumentException;
    
    /**
     * Get a configuration panel for an object.
     * This method initializes the panel with the configuration of the object.
     * 
     * @param object the object for which to return a configuration panel
     * @return a panel that configures this object
     * @throws IllegalArgumentException if this class does not support the class
     * with the name given in parameter 'className'
     */
    public JPanel getConfigPanel(@Nonnull Base object) throws IllegalArgumentException;
    
    /**
     * Create a new object with the data entered.
     * This method must also register the object in its manager.
     * 
     * @param systemName system name
     * @return a male socket for the new object
     */
    public MaleSocket createNewObject(@Nonnull String systemName);
    
    /**
     * Create a new object with the data entered.
     * This method must also register the object in its manager.
     * 
     * @param systemName system name
     * @param userName user name
     * @return a male socket for the new object
     */
    public MaleSocket createNewObject(@Nonnull String systemName, @Nonnull String userName);
    
}
