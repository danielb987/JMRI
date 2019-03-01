package jmri.jmrit.logixng;

import java.util.Map;
import java.util.Set;

/**
 * Factory class for Action classes.
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public interface LogixNGPluginFactory {

    /**
     * Get a set of classes that implements the ActionPlugin interface.
     * 
     * @return a set of entries with category and class
     */
    public Set<Map.Entry<Category, Class<? extends DigitalExpressionPlugin>>>
        getExpressionClasses();
    
    /**
     * Get a set of classes that implements the ActionPlugin interface.
     * 
     * @return a set of entries with category and class
     */
    public Set<Map.Entry<Category, Class<? extends ActionPlugin>>>
        getActionClasses();
    
}
