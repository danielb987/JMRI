package jmri.jmrit.newlogix.swing;

import javax.swing.JPanel;

/**
 * The parent interface for the configurator of plugin classes.
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public interface PluginConfiguratorInterface {

    /**
     * Get a configuration panel for this plugin.
     * 
     * @return a panel that configures this plugin
     */
    public JPanel getConfigPanel();
    
    /**
     * Update the configuration.
     * This method is called then the user has editied the configuration and
     * saves the changes.
     * 
     * @param panel the panel that was returned by getConfigPanel()
     */
    public void updateConfigFromPanel(JPanel panel);

}
