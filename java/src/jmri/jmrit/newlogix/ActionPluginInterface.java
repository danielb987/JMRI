package jmri.jmrit.newlogix;

import java.util.Map;
import jmri.NewLogixAction;

/**
 * The parent interface for plugin NewLogixAction classes.
 * A plugin NewLogixAction class is a class that implements the NewLogixAction interface and
 can be loaded from a JAR file.
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public interface ActionPluginInterface extends NewLogixAction {
    
    /**
     * Initialize the object.
     * 
     * @param config the configuration
     */
    public void init(Map<String, String> config);
    
    /**
     * Get the configuration.
     * This method is called then the object is stored in the XML file.
     * 
     * @return the configuration
     */
    public Map<String, String> getConfig();
    
    /**
     * Return the class name of the configurator class.
     * Returns the fully qualified class name of the class that is used to
     * configurate this class. That class needs to implement the interface
     * PluginConfiguratorInterface.
     * 
     * @return the fully qualified name of the configurator class
     */
    public String getConfiguratorClassName();
    
}
