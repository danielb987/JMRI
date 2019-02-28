package jmri.jmrit.logixng;

import java.util.Map;

/**
 * The parent interface for plugin AnalogExpression classes.
 * A plugin AnalogExpression class is a class that implements the
 * AnalogExpression interface and can be loaded from a JAR file.
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public interface AnalogExpressionPlugin extends AnalogExpression {

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
    
}
