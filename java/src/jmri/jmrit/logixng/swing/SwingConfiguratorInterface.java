package jmri.jmrit.logixng.swing;

import javax.swing.JPanel;
import jmri.jmrit.logixng.MaleSocket;

/**
 * The parent interface for configuring classes with Swing.
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public interface SwingConfiguratorInterface {

    /**
     * Get a configuration panel for an object.
     * This method initializes the panel with an empty configuration.
     * 
     * @param object the object for which to return a configuration panel
     * @return a panel that configures this object
     * @throws IllegalArgumentException if this class does not support the class
     * with the name given in parameter 'className'
     */
    public JPanel getConfigPanel(Object object) throws IllegalArgumentException;
    
    /**
     * Create a new object with the data entered.
     * This method must also register the object in its manager.
     * 
     * @return a male socket for the new object
     */
    public MaleSocket createNewObject(String systemName, String userName);
    
}
