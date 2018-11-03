package jmri.jmrit.newlogix;

import java.util.Map;
import java.util.Set;

/**
 * Factory class for NewLogixAction classes.
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public interface NewLogixPluginFactory {

    /**
     * Get a set of classes that implements the NewLogixActionPlugin interface.
     * 
     * @return a set of entries with category and class
     */
    public Set<Map.Entry<Category, Class<? extends NewLogixExpressionPlugin>>>
        getExpressionClasses();
    
    /**
     * Get a set of classes that implements the NewLogixActionPlugin interface.
     * 
     * @return a set of entries with category and class
     */
    public Set<Map.Entry<Category, Class<? extends NewLogixActionPlugin>>>
        getActionClasses();
    
}
