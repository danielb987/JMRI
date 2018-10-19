package jmri.jmrit.newlogix;

import java.util.Map;
import java.util.Set;

import jmri.NewLogixCategory;

/**
 * Factory class for NewLogixAction classes.
 * 
 * @author Daniel Bergqvist Copyright 2018
 */
public interface ExpressionPluginFactory {

    /**
     * Get a set of classes that implements the NewLogixActionPlugin interface.
     * 
     * @return a set of entries with category and class
     */
    public Set<Map.Entry<NewLogixCategory, Class<? extends ExpressionPluginInterface>>>
        getExpressionClasses();
    
}
