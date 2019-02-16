package jmri.jmrit.logixng;

import java.util.Map;
import jmri.jmrit.logixng.Action;

/**
 * The parent interface for plugin Action classes.
 * A plugin Action class is a class that implements the Action interface and
 can be loaded from a JAR file.
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public interface ActionPlugin extends Action {
    
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
